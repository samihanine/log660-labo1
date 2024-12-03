package ca.etsmtl.log660.labo2.service;
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

import ca.etsmtl.log660.labo2.models.Film;
import ca.etsmtl.log660.labo2.models.User;
import ca.etsmtl.log660.labo2.repository.basedata.BaseDataRepository;
import ca.etsmtl.log660.labo2.repository.film.FilmRepository;
import ca.etsmtl.log660.labo2.repository.user.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * @author Kacou Serge BROU <kacou-serge-bruno.brou.1@ens.etsmtl.ca, brouserge1er@gmail.com>
 */

@Service
class DefaultFilmService implements FilmService {

    private final FilmRepository filmRepository;
    private final BaseDataRepository baseDataRepository;
    private final UserRepository userRepository;


    DefaultFilmService(FilmRepository filmRepository, BaseDataRepository baseDataRepository, UserRepository userRepository) {
        this.filmRepository = filmRepository;
        this.baseDataRepository = baseDataRepository;
        this.userRepository = userRepository;
    }


    @Override
    public Film getFilmById(int id) {
        Film film = filmRepository.getFilmById(id);
        film.setRoles(baseDataRepository.getRolesByFilmId(id));
        film.setGenres(baseDataRepository.getGenresByFilmId(id));
        film.setDirector(baseDataRepository.getDirectorByFilmId(id));
        film.setTrailers(baseDataRepository.getTrailersByFilmId(id));
        film.setScriptwriters(baseDataRepository.getScriptwritersByFilmId(id));
        film.setCountries(baseDataRepository.getCountriesByFilmId(id));
        return film;
    }

    @Override
    public Set<Film> getFilmsByKeyword(String title, String genre, String actor, String director, String language, String country, int startYear, int endYear, int page, int pageSize) {
        return filmRepository.getFilms(title, genre, actor, director, language, country, startYear, endYear, page, pageSize);
    }

    @Override
    public void rentFilm(int id, String email) {
        User currentUser = userRepository.findByEmail(email);
        filmRepository.rentFilm(id, currentUser);
    }

    @Override
    public long getCountFilm(String title, String genre, String actor, String director, String language, String country, int startYear, int endYear) {
        return filmRepository.getCountFilm(title, genre, actor, director, language, country, startYear, endYear);
    }

    @Override
    public void returnFilm(String idCopy, String name) {
        User currentUser = userRepository.findByEmail(name);
        filmRepository.returnFilm(idCopy, currentUser);
    }

    @Override
    public void generateFakeRents() {
        filmRepository.generateFakeRents();
    }

    @Override
    public int numberOfRentals(Integer age, String month, String province, String weekday) {
        return filmRepository.getNumberOfRentals(age, month, province, weekday);
    }


    @Override
    public List<Film> getRecommendations(int id, String userEmail) {
        User currentUser = userRepository.findByEmail(userEmail);
        return filmRepository.getRecommendations(id, currentUser);
    }

    @Override
    public double getRating(int idFilm) {
        return filmRepository.getRating(idFilm);
    }
}
