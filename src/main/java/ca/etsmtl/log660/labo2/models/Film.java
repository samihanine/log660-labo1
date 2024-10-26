package ca.etsmtl.log660.labo2.models;
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

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Kacou Serge BROU <kacou-serge-bruno.brou.1@ens.etsmtl.ca, brouserge1er@gmail.com>
 */

@Entity
@Table(name = "FILM")
@Getter
@Setter
public class Film {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID_FILM")
    private int id;

    @Column(name = "TITRE")
    private String title;

    @Column(name = "ANNEE_DE_SORTIE")
    private int year;

    @Column(name = "LANGUE_ORIGINALE")
    private String language;

    @Column(name = "DUREE_EN_MINUTES")
    private int duration;

    @Column(name = "AFFICHE")
    private String poster;

    @Column(name = "RESUME_SCENARIO")
    private String resume;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "FILMGENRE",
            joinColumns = @JoinColumn(name = "FILM_ID"),
            inverseJoinColumns = @JoinColumn(name = "GENRE_ID")
    )
    private Set<Genre> genres = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "REALISATEUR_ID")
    private Contributor director;
/*
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "ROLE",
            joinColumns = @JoinColumn(name = "FILM_ID"),
            inverseJoinColumns = @JoinColumn(name = "ACTEUR_ID")
    )
    private Set<Contributor> actors = new HashSet<>();*/

    @OneToMany(mappedBy = "film", fetch = FetchType.LAZY)
    private Set<Role> roles = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "FILMSCENARISTE",
            joinColumns = @JoinColumn(name = "FILM_ID"),
            inverseJoinColumns = @JoinColumn(name = "SCENARISTE_ID")
    )
    private Set<Scriptwriter> scriptwriters = new HashSet<>();


    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "FILMPAYSDEPRODUCTION",
            joinColumns = @JoinColumn(name = "ID_FILM"),
            inverseJoinColumns = @JoinColumn(name = "PAYSDEPRODUCTION_ID")
    )
    private Set<Country> countries = new HashSet<>();


    @OneToMany(mappedBy = "film", fetch = FetchType.LAZY)
    private Set<Trailer> trailers = new HashSet<>();

    private void setID(int id) {
        this.id = id;
    }

}
