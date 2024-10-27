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
import lombok.Setter;

import java.sql.Date;

/**
 * @author Kacou Serge BROU <kacou-serge-bruno.brou.1@ens.etsmtl.ca, brouserge1er@gmail.com>
 */

@Entity
@Table(name = "PERSONNE")
@Setter
public  class Contributor extends Person {

    private String biography;

    private String photo;

    private String birthPlace;

    @Id
    @Column(name = "ID_PERSONNE")
    public int getId() {
        return super.getId();
    }
    @Column(name = "BIOGRAPHIE")
    public String getBiography() {
        return biography;
    }

    @Column(name = "PHOTO")
    public String getPhoto() {
        return photo;
    }

    @Column(name = "LIEU_DE_NAISSANCE")
    public String getBirthPlace() {
        return birthPlace;
    }

    @Override
    @Column(name = "PRENOM")
    public String getFirstName() {
        return super.getFirstName();
    }

    @Override
    @Column(name = "NOM")
    public String getName() {
        return super.getName();
    }

    @Column(name = "DATE_DE_NAISSANCE")
    @Override
    public Date getBirthDate() {
        return super.getBirthDate();
    }
}
