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

import ca.etsmtl.log660.labo2.controller.dto.SubData;
import ca.etsmtl.log660.labo2.service.BaseDataService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Kacou Serge BROU <kacou-serge-bruno.brou.1@ens.etsmtl.ca, brouserge1er@gmail.com>
 */

@RestController
@RequestMapping("/data")
public class BaseDataController {

    private final BaseDataService baseDataService;

    public BaseDataController(BaseDataService baseDataService) {
        this.baseDataService = baseDataService;
    }

    @GetMapping("/countries")
    public ResponseEntity<List<SubData>> getCountries() {
        return ResponseEntity.ok(baseDataService
                .getCountries()
                .stream()
                .map(country -> SubData.builder()
                        .id(country.getId())
                        .name(country.getName())
                        .build())
                .collect(Collectors.toList()));
    }


    @GetMapping("/genres")
    public ResponseEntity<List<SubData>> getGenres() {
        return ResponseEntity.ok(baseDataService
                .getGenres()
                .stream()
                .map(genre -> SubData.builder()
                        .id(genre.getId())
                        .name(genre.getName())
                        .build())
                .collect(Collectors.toList()));
    }

    @GetMapping("/languages")
    public ResponseEntity<List<String>> getLanguages() {
        return ResponseEntity.ok(baseDataService
                .getLanguages());
    }
}
