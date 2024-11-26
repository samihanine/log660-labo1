package ca.etsmtl.log660.labo2.controller;
/*
 * Copyright (c)
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * Neither the name of the BKSB project nor the names of its contributors may
 * be used to endorse or promote products derived from this software without
 * specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

import ca.etsmtl.log660.labo2.controller.dto.FilmDetailDto;
import ca.etsmtl.log660.labo2.controller.dto.FilmDto;
import ca.etsmtl.log660.labo2.controller.dto.RoleDto;
import ca.etsmtl.log660.labo2.controller.dto.SubData;
import ca.etsmtl.log660.labo2.models.*;
import ca.etsmtl.log660.labo2.service.FilmService;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Kacou Serge BROU <kacou-serge-bruno.brou.1@ens.etsmtl.ca, brouserge1er@gmail.com>
 */

@RestController
@RequestMapping("/films")
@CrossOrigin
public class FilmController {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(FilmController.class);
    private final FilmService filmService;

    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping("")
    public ResponseEntity<Map<String, Object>> getFilms(@RequestParam(required = false, defaultValue = "") String title,
                                                        @RequestParam(required = false, defaultValue = "0") int startYear,
                                                        @RequestParam(required = false, defaultValue = "9999") int endYear,
                                                        @RequestParam(required = false, defaultValue = "") String country,
                                                        @RequestParam(required = false, defaultValue = "") String genre,
                                                        @RequestParam(required = false, defaultValue = "") String director,
                                                        @RequestParam(required = false, defaultValue = "") String actor,
                                                        @RequestParam(required = false, defaultValue = "") String language,
                                                        @RequestParam(required = false, defaultValue = "1") int page,
                                                        @RequestParam(required = false, defaultValue = "30") int pageSize) {


        Set<Film> films = filmService.getFilmsByKeyword(title, genre, actor, director, language, country,
                startYear, endYear, page, pageSize);

        Map<String, Object> response = new HashMap<>();

        response.put("films", films.stream().map(film -> FilmDto.builder()
                .id(film.getId())
                .title(film.getTitle())
                .year(film.getYear())
                .build()).collect(Collectors.toSet()));

        response.put("page", page);
        response.put("pageSize", pageSize);
        long totalItems = filmService.getCountFilm(title, genre, actor, director, language, country, startYear, endYear);
        int totalPages = (int) Math.ceil((double) totalItems / pageSize);
        response.put("total", totalItems);
        response.put("totalPages", totalPages);

        return ResponseEntity
                .ok(response);
    }

    @GetMapping("/{id}")
    public FilmDetailDto getFilm(@PathVariable int id) {

        Film film = filmService.getFilmById(id);

        if (film == null) {
            return null;
        }

        return FilmDetailDto.builder()
                .id(film.getId())
                .title(film.getTitle())
                .year(film.getYear())
                .language(film.getLanguage())
                .duration(film.getDuration())
                .poster(film.getPoster())
                .resume(film.getResume())
                .genres(film.getGenres().stream().map(Genre::getName).toArray(String[]::new))
                .countries(film.getCountries().stream().map(Country::getName).toArray(String[]::new))

                .director(SubData.builder()
                        .id(film.getDirector().getId())
                        .name(film.getDirector().getName())
                        .build())

                .roles(film.getRoles().stream().map(role ->
                        RoleDto.builder()
                                .id(role.getActor().getId())
                                .name(role.getActor().getName())
                                .pseudo(role.getPseudo()).build()).toArray(RoleDto[]::new))
                .trailers(film.getTrailers().stream().map(Trailer::getUrl).toArray(String[]::new))
                .writers(film.getScriptwriters().stream().map(Scriptwriter::getName).toArray(String[]::new))

                .build();
    }

    @PostMapping("/rental/{id}")
    public ResponseEntity<Map<String, String>> rentFilm(@PathVariable int id, Authentication authentication) {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Film rented successfully");

        try {
            filmService.rentFilm(id, authentication.getName());
        } catch (Exception e) {
            response.put("code", e.getMessage());
            if (e.getMessage().equals("CHECK_MAX_RESERVATIONS")) {
                response.put("message", "You have reached the maximum number of rentals");
            }

            if (e.getMessage().equals("NO_AVAILABLE_COPY")) {
                response.put("message", "Film not available for rent");
            }
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(response);
        }
        return ResponseEntity
                .ok(response);
    }

    @PostMapping("/return/{idCopy}")
    public ResponseEntity<Map<String, String>> returnFilm(@PathVariable String idCopy, Authentication authentication) {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Film returned successfully");

        try {
            filmService.returnFilm(idCopy, authentication.getName());
        } catch (Exception e) {
            response.put("message", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(response);
        }
        return ResponseEntity
                .ok(response);
    }

    @PostMapping("/generate-fake-rentals")
    public ResponseEntity<Map<String, String>> generateFakeRentals() {
        logger.info("Starting the generation of fake rentals.");
        Map<String, String> response = new HashMap<>();
        try {
            filmService.generateFakeRents();
            response.put("message", "Fake rentals generated successfully.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("message", "Error generating fake rentals.");
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/rentals-count")
    public ResponseEntity<Map<String, String>> numberOfRentals(
            @RequestParam(required = false) Integer age,
            @RequestParam(required = false) String month,
            @RequestParam(required = false) String province,
            @RequestParam(required = false) String weekday) {

        Map<String, String> response = new HashMap<>();

        try {
            // Call the service layer to get the stats
            int count = filmService.numberOfRentals(age, month, province, weekday);
            response.put("count", String.valueOf(count));
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("message", "Error fetching stats");
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
