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
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Date;
import java.util.Collection;
import java.util.List;

/**
 * @author Kacou Serge BROU <kacou-serge-bruno.brou.1@ens.etsmtl.ca, brouserge1er@gmail.com>
 */
@Entity
@Table(name = "CLIENT")
@Setter
public class User  extends Person implements UserDetails {
    private String password;
    private String email;
    private String phone;
    private String address;
    private String city;
    private String state;
    private String zip;
    private CarteType carteType;
    private String carteNumber;
    private Date dateExpire;
    private String subscribe;

    @Transient
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID_CLIENT")
    public int getId() {
        return super.getId();
    }

    @Column(name = "COURRIEL")
    public String getEmail() {
        return email;
    }

    @Column(name = "TELEPHONE")
    public String getPhone() {
        return phone;
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

    @Column(name = "ADRESSE")
    public String getAddress() {
        return address;
    }

    @Column(name = "VILLE")
    public String getCity() {
        return city;
    }

    @Column(name = "PROVINCE")
    public String getState() {
        return state;
    }

    @Column(name = "CODE_POSTAL")
    public String getZip() {
        return zip;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "CARTE")
    public CarteType getCarteType() {
        return carteType;
    }

    @Column(name = "NUMERO")
    public String getCarteNumber() {
        return carteNumber;
    }

    @Column(name = "DATE_EXPIRATION")
    public Date getDateExpire() {
        return dateExpire;
    }

    @Column(name = "CODE_ABONNEMENT")
    public String getSubscribe() {
        return subscribe;
    }

    @Column(name = "MOT_DE_PASSE")
    @Override
    public String getPassword() {
        return password;
    }

    @Transient
    @Override
    public String getUsername() {
        return email;
    }


}
