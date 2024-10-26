package ca.etsmtl.log660.labo2.repository.basedata;
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

import ca.etsmtl.log660.labo2.models.*;
import jakarta.persistence.EntityManagerFactory;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Kacou Serge BROU <kacou-serge-bruno.brou.1@ens.etsmtl.ca, brouserge1er@gmail.com>
 */

@Repository
public class DefaultBaseDataRepository implements BaseDataRepository {

    private final EntityManagerFactory entityManagerFactory;

    public DefaultBaseDataRepository(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    private Session getSession() {
        return entityManagerFactory.createEntityManager().unwrap(Session.class);
    }



    @Override
    public Set<Role> getRolesByFilmId(int filmId) {
        try(Session session = getSession()) {
            return new HashSet<>(session.createQuery("select r from Role r where r.film.id = :filmId", Role.class)
                    .setParameter("filmId", filmId)
                    .getResultList());
        } catch (Exception e) {
            return new HashSet<>();
        }
    }

    @Override
    public Set<Genre> getGenresByFilmId(int filmId) {
        try (Session session = getSession()) {
            return new HashSet<>(session.createQuery("select f.genres from Film f where f.id = :filmId", Genre.class)
                    .setParameter("filmId", filmId)
                    .getResultList());
        } catch (Exception e) {
            return new HashSet<>();

        }
    }

    @Override
    public List<Genre> getGenres() {
        try (Session session = getSession()) {
            return session.createQuery("select g from Genre g order by g.name", Genre.class)
                    .getResultList();
        } catch (Exception e) {
            return List.of();

        }
    }

    @Override
    public Contributor getContributorById(int contributorId) {
        try(Session session = getSession()) {
            return session.find(Contributor.class, contributorId);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Contributor getDirectorByFilmId(int filmId) {
        try(Session session = getSession()) {
            return session.createQuery("select f.director from Film f where f.id = :filmId", Contributor.class)
                    .setParameter("filmId", filmId)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Set<Trailer> getTrailersByFilmId(int filmId) {
        try(Session session = getSession()) {
            return new HashSet<>(session.createQuery("select f.trailers from Film f where f.id = :filmId", Trailer.class)
                    .setParameter("filmId", filmId)
                    .getResultList());
        } catch (Exception e) {
            return new HashSet<>();
        }
    }

    @Override
    public Set<Scriptwriter> getScriptwritersByFilmId(int filmId) {
        try(Session session = getSession()) {
            return new HashSet<>(session.createQuery("select f.scriptwriters from Film f where f.id = :filmId", Scriptwriter.class)
                    .setParameter("filmId", filmId)
                    .getResultList());
        } catch (Exception e) {
            return new HashSet<>();

        }
    }

    @Override
    public Set<Country> getCountriesByFilmId(int filmId) {
        try(Session session = getSession()) {
            return new HashSet<>(session.createQuery("select c from Film f join f.countries c where f.id = :filmId", Country.class)
                    .setParameter("filmId", filmId)
                    .getResultList());
        } catch (Exception e) {
            return new HashSet<>();
        }
    }

    @Override
    public List<Country> getCountries() {
        try(Session session = getSession()) {
            return session.createQuery("select c from Country c order by c.name", Country.class)
                    .getResultList();
        } catch (Exception e) {
            return  List.of();
        }
    }

    @Override
    public List<String> getLanguages() {
        try(Session session = getSession()) {
            return session.createQuery("select distinct f.language from Film f where f.language IS not null order by f.language", String.class)
                    .getResultList();
        } catch (Exception e) {
            return  List.of();
        }
    }


}
