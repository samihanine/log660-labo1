package ca.etsmtl.log660.labo2.repository.film;
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
import ca.etsmtl.log660.labo2.models.FilmCopy;
import ca.etsmtl.log660.labo2.models.User;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Query;
import jakarta.persistence.StoredProcedureQuery;
import jakarta.persistence.TypedQuery;
import org.hibernate.Session;
import org.hibernate.exception.GenericJDBCException;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author Kacou Serge BROU <kacou-serge-bruno.brou.1@ens.etsmtl.ca, brouserge1er@gmail.com>
 */

@Repository
class DefaultFilmRepository implements FilmRepository {

    private final EntityManagerFactory entityManagerFactory;

    public DefaultFilmRepository(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    private Session getSession() {
        return entityManagerFactory.createEntityManager().unwrap(Session.class);
    }

    @Override
    public void generateFakeRents() {
        try (Session session = getSession()) {
            session.beginTransaction();

            // Fetch all users
            List<User> users = session.createQuery("FROM User", User.class).getResultList();
            if (users.isEmpty()) {
                throw new IllegalStateException("No users found!");
            }

            // Fetch all films
            List<Integer> filmIds = session.createQuery("SELECT f.id FROM Film f", Integer.class).getResultList();
            if (filmIds.isEmpty()) {
                throw new IllegalStateException("No films found!");
            }

            Random random = new Random();
            int rentalCount = 0;
            int maxRentals = 3000;

            for (User user : users) {
                if (rentalCount >= maxRentals) {
                    break;
                }
                try {
                    // Select a random film
                    int filmId = filmIds.get(random.nextInt(filmIds.size()));
                    // Use the rentFilm method to create a reservation
                    rentFilmAtRandomTime(filmId, user);
                    rentalCount++;
                } catch (RuntimeException e) {
                    if (e.getMessage().equals("NO_AVAILABLE_COPY")) {
                        System.out.println("No available copy for the selected film.");
                    } else if (e.getMessage().equals("CHECK_MAX_RESERVATIONS")) {
                        System.out.println("User " + user.getId() + " has reached the maximum number of reservations.");
                    } else {
                        e.printStackTrace();
                    }
                }
            }

            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getNumberOfRentals(Integer age, String month, String province, String weekday) {
        try (Session session = getSession()) {
            // Start building the SQL query
            StringBuilder sqlBuilder = new StringBuilder();
            sqlBuilder.append("SELECT COUNT(*) FROM FACT_LOCATION fl ");
            sqlBuilder.append("JOIN DIM_CLIENT dc ON fl.client_id = dc.id_client ");
            sqlBuilder.append("JOIN DIM_DATE dd ON fl.temps_id = dd.id_reservation WHERE 1=1");

            // Dynamically add filters
            if (age != null) {
                sqlBuilder.append(" AND dc.age = :age");
            }
            if (month != null) {
                sqlBuilder.append(" AND dd.mois = :month");
            }
            if (province != null) {
                sqlBuilder.append(" AND dc.province = :province");
            }
            if (weekday != null) {
                sqlBuilder.append(" AND TRIM(UPPER(dd.jour_de_la_semaine)) = :weekday");
            }

            // Create the native query
            Query query = session.createNativeQuery(sqlBuilder.toString());

            // Set parameters
            if (age != null) {
                query.setParameter("age", age);
            }
            if (month != null) {
                query.setParameter("month", month);
            }
            if (province != null) {
                query.setParameter("province", province);
            }
            if (weekday != null) {
                query.setParameter("weekday", weekday.trim().toUpperCase());
            }

            // Execute the query and return the count
            Number count = (Number) query.getSingleResult();
            return count.intValue();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public void rentFilmAtRandomTime(int id, User currentUser) {
        try (Session session = getSession()) {
            FilmCopy copy = getAvailableCopy(session, id);
            if (copy == null) {
                throw new RuntimeException("NO_AVAILABLE_COPY");
            }

            // Generate a random date between 2010-01-01 and 2024-12-31
            Date randomDate = generateRandomDateBetween(2010, 2024);

            StoredProcedureQuery query = session.createStoredProcedureQuery("Faire_Reservation");

            query.registerStoredProcedureParameter("p_date_de_reservation", Date.class, jakarta.persistence.ParameterMode.IN);
            query.registerStoredProcedureParameter("p_client_id", Integer.class, jakarta.persistence.ParameterMode.IN);
            query.registerStoredProcedureParameter("p_copieFilm_numero_de_code", String.class, jakarta.persistence.ParameterMode.IN);

            query.setParameter("p_date_de_reservation", randomDate);
            query.setParameter("p_copieFilm_numero_de_code", copy.getId());
            query.setParameter("p_client_id", currentUser.getId());

            query.execute();

        } catch (GenericJDBCException e) {
            if (e.getMessage().toUpperCase().contains("CHECK_MAX_RESERVATIONS")) {
                throw new RuntimeException("CHECK_MAX_RESERVATIONS");
            }
        }
    }

    private Date generateRandomDateBetween(int startYear, int endYear) {
        long start = java.sql.Date.valueOf(startYear + "-01-01").getTime();
        long end = java.sql.Date.valueOf(endYear + "-12-31").getTime();
        long randomMillis = ThreadLocalRandom.current().nextLong(start, end);
        return new Date(randomMillis);
    }

    @Override
    public Set<Film> getFilms(String title, String genre, String actor, String director, String language,
                              String country, int startYear, int endYear, int page, int pageSize) {
        try (Session session = getSession()) {
            TypedQuery<Film> query = (TypedQuery<Film>) createQuery(session, title, genre, actor, director, language, country, startYear, endYear, false);
            return new HashSet<>(query
                    .setFirstResult((page - 1) * pageSize)
                    .setMaxResults(pageSize)
                    .getResultList());
        } catch (Exception e) {
            return Set.of();
        }
    }

    @Override
    public Long getCountFilm(String title, String genre, String actor, String director, String language,
                             String country, int startYear, int endYear) {
        try (Session session = getSession()) {
            TypedQuery<Long> query = (TypedQuery<Long>) createQuery(session, title, genre, actor, director, language, country, startYear, endYear, true);
            return query.getSingleResult();
        } catch (Exception e) {
            return 0L;
        }
    }


    @Override
    public Film getFilmById(int id) {
        Film film = null;
        try (Session session = getSession()) {
            TypedQuery<Film> query = session.createQuery(" from Film f  where f.id = :id", Film.class);

            query.setParameter("id", id);

            film = query.getSingleResult();

        } catch (Exception e) {
            return null;
        }
        return film;
    }

    @Override
    public void rentFilm(int id, User currentUser) {

        try (Session session = getSession()) {

            FilmCopy copy = getAvailableCopy(session, id);

            if (copy == null) {
                throw new RuntimeException("NO_AVAILABLE_COPY");
            }

            StoredProcedureQuery query = session.createStoredProcedureQuery("Faire_Reservation");

            query.registerStoredProcedureParameter("p_date_de_reservation", Date.class, jakarta.persistence.ParameterMode.IN);
            query.registerStoredProcedureParameter("p_client_id", Integer.class, jakarta.persistence.ParameterMode.IN);
            query.registerStoredProcedureParameter("p_copieFilm_numero_de_code", String.class, jakarta.persistence.ParameterMode.IN);


            query.setParameter("p_date_de_reservation", new Date(System.currentTimeMillis()));
            query.setParameter("p_copieFilm_numero_de_code", copy.getId());
            query.setParameter("p_client_id", currentUser.getId());

            query.execute();

        } catch (GenericJDBCException e) {
            if (e.getMessage().toUpperCase().contains("CHECK_MAX_RESERVATIONS")) {
                throw new RuntimeException("CHECK_MAX_RESERVATIONS");
            }
        }
    }

    @Override
    public void returnFilm(String idCopy, User currentUser) {
        try (Session session = getSession()) {
            StoredProcedureQuery query = session.createStoredProcedureQuery("RETURN_FILM");

            query.registerStoredProcedureParameter("p_client_id", Integer.class, jakarta.persistence.ParameterMode.IN);
            query.registerStoredProcedureParameter("p_copie_film_numero", String.class, jakarta.persistence.ParameterMode.IN);
            query.registerStoredProcedureParameter("p_date", Date.class, jakarta.persistence.ParameterMode.IN);

            query.setParameter("p_date", new Date(System.currentTimeMillis()));
            query.setParameter("p_copie_film_numero", idCopy);
            query.setParameter("p_client_id", currentUser.getId());

            query.execute();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private FilmCopy getAvailableCopy(Session session, int idFilm) {
        try {

            TypedQuery<FilmCopy> query = session.createQuery(" from FilmCopy f  where f.film.id = :id and f.available order by f.id", FilmCopy.class);

            query.setParameter("id", idFilm);

            return query.getResultList().stream().findFirst().orElse(null);

        } catch (Exception e) {
            return null;
        }
    }

    private Query createQuery(Session session, String title, String genre, String actor, String director, String language,
                              String country, int startYear, int endYear, boolean isCount) {

        String titleKey = "title";
        String genreKey = "genre";
        String actorKey = "actor";
        String directorKey = "director";

        String queryTitle = contains("f.title", titleKey, title);
        String queryActor = contains("a.name", actorKey, actor);
        String queryDirector = contains("f.director.name", directorKey, director);

        StringBuilder stringBuilder = new StringBuilder(isCount ? "select count(*) from Film f  " : "from Film f ");


        if (!genre.isEmpty()) {
            stringBuilder.append(" join f.genres g ");
        }
        if (!actor.isEmpty()) {
            stringBuilder.append(" join f.roles.actor a ");
        }
        if (!director.isEmpty()) {
            stringBuilder.append(" join f.director d ");
        }
        if (!country.isEmpty()) {
            stringBuilder.append(" join f.countries c ");
        }

        stringBuilder.append(" where f.year >= :minDate and f.year <= :maxDate");

        if (!title.isEmpty()) {
            stringBuilder.append(" and ").append(queryTitle);
        }

        if (!language.isEmpty()) {
            stringBuilder.append(" and upper(f.language) = :lang");
        }

        if (!country.isEmpty()) {
            stringBuilder.append(" and upper(c.name) = :country");
        }

        if (!genre.isEmpty()) {
            stringBuilder.append(" and upper(g.name) = :genre");
            // stringBuilder.append(" and ").append(queryGenre);
        }

        if (!actor.isEmpty()) {
            stringBuilder.append(" and ").append(queryActor);
        }

        if (!director.isEmpty()) {
            stringBuilder.append(" and ").append(queryDirector);
        }


        Query query = isCount ? session.createQuery(stringBuilder.toString(), Long.class) : session.createQuery(stringBuilder.toString(), Film.class);


        query.setParameter("minDate", startYear);
        query.setParameter("maxDate", endYear);
        if (!language.isEmpty()) {
            query.setParameter("lang", language.toUpperCase());
        }
        if (!country.isEmpty()) {
            query.setParameter("country", country.toUpperCase());
        }

        if (!title.isEmpty()) {
            setParameters(query, titleKey, title);
        }

        if (!genre.isEmpty()) {
            query.setParameter("genre", genre.toUpperCase());
        }
        if (!actor.isEmpty()) {
            setParameters(query, actorKey, actor);
        }
        if (!director.isEmpty()) {
            setParameters(query, directorKey, director);
        }

        return query;

    }

    private String contains(String field, String key, String value) {
        StringBuilder querytitle = new StringBuilder("upper(").append(field).append(") like '%%'");
        String[] values = value.trim().toUpperCase().split("\\s+");
        int i = 0;
        for (String v : values) {
            if (!v.isEmpty()) {
                i++;
                querytitle.append(" and upper(").append(field).append(") like CONCAT('%',:").append(key).append(i).append(",'%')");
            }
        }

        return querytitle.toString();
    }

    private void setParameters(Query query, String key, String value) {
        String[] values = value.trim().toUpperCase().split("\\s+");
        int i = 0;
        for (String v : values) {
            if (!v.isEmpty()) {
                i++;
                query.setParameter(key + i, v);
            }
        }
    }

}
