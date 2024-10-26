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

import java.util.Date;

/**
 * @author Kacou Serge BROU <kacou-serge-bruno.brou.1@ens.etsmtl.ca, brouserge1er@gmail.com>
 */

@Entity
@Table(name="RESERVATION")
@Setter
public class Rent {

    private int id;
    private FilmCopy filmCopy;
    private User client;
    private Date rentDate;
    private Date returnDate;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID_RESERVATION")
    public int getId() {
        return id;
    }

    @ManyToOne
    @JoinColumn(name = "COPIEFILM_NUMERO_DE_CODE")
    public FilmCopy getFilmCopy() {
        return filmCopy;
    }

    @ManyToOne
    @JoinColumn(name = "CLIENT_ID")
    public User getClient() {
        return client;
    }

    @Column(name = "DATE_DE_RESERVATION")
    public Date getRentDate() {
        return rentDate;
    }

    @Column(name = "DATE_DE_RETOUR_PREVUE")
    public Date getReturnDate() {
        return returnDate;
    }

    private void setId(int id) {
        this.id = id;
    }
}
