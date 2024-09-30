CREATE TABLE Abonnement (
  code CHAR PRIMARY KEY,
  forfait VARCHAR(255),
  cout FLOAT,
  reservation_max_copies NUMBER(38),
  duree_max_jours NUMBER(38) NOT NULL
);

CREATE TABLE Client (
  id_client NUMBER(38) PRIMARY KEY,
  nom VARCHAR(255) NOT NULL,
  prenom VARCHAR(255) NOT NULL,
  date_de_naissance DATE NOT NULL,
  courriel VARCHAR(255) NOT NULL UNIQUE,
  telephone VARCHAR(255) NOT NULL,
  mot_de_passe VARCHAR(255) NOT NULL,
  adresse VARCHAR(255) NOT NULL,
  ville VARCHAR(255) NOT NULL,
  province VARCHAR(255) NOT NULL,
  code_postal VARCHAR(255) NOT NULL,
  carte VARCHAR(15) CHECK (carte IN ('VISA', 'MasterCard', 'Amex')) NOT NULL,
  numero VARCHAR(255) NOT NULL,
  cvv VARCHAR(255) NOT NULL,
  date_expiration DATE NOT NULL,
  code_abonnement CHAR REFERENCES Abonnement(code),
    CONSTRAINT chk_mot_de_passe CHECK (
    LENGTH(mot_de_passe) >= 5 AND
    REGEXP_LIKE(mot_de_passe, '.*[A-Za-z].*') AND
    REGEXP_LIKE(mot_de_passe, '.*[0-9].*')
  )
);

CREATE TABLE Employee (
  id_employee NUMBER(38) PRIMARY KEY,
  nom VARCHAR(255),
  prenom VARCHAR(255),
  date_de_naissance DATE,
  courriel VARCHAR(255) UNIQUE NOT NULL,
  telephone VARCHAR(255) NOT NULL,
  mot_de_passe VARCHAR(255) NOT NULL,
  adresse VARCHAR(255) NOT NULL,
  ville VARCHAR(255) NOT NULL,
  province VARCHAR(255) NOT NULL,
  code_postal VARCHAR(255) NOT NULL,
  matricule VARCHAR(7) NOT NULL UNIQUE
);

CREATE TABLE Personne (
  id_personne NUMBER(38) PRIMARY KEY,
  nom VARCHAR(255),
  prenom VARCHAR(255),
  date_de_naissance DATE,
  lieu_de_naissance VARCHAR(255),
  biographie VARCHAR(255),
  photo VARCHAR(255)
);

CREATE TABLE Film (
  id_film NUMBER(38) PRIMARY KEY,
  titre VARCHAR(255) NOT NULL,
  annee_de_sortie NUMBER(38),
  langue_originale VARCHAR(255),
  duree_en_minutes NUMBER(38),
  resume_scenario VARCHAR(255) NOT NULL,
  affiche VARCHAR(255),
  realisateur_id NUMBER(38) NOT NULL REFERENCES Personne(id_personne)
);

CREATE TABLE Scenariste (
  id_scenariste NUMBER(38) PRIMARY KEY,
  nom VARCHAR(255)
);

CREATE TABLE FilmScenariste (
  film_id NUMBER(38) NOT NULL REFERENCES Film(id_film),
  scenariste_id NUMBER(38) NOT NULL REFERENCES Scenariste(id_scenariste),
  CONSTRAINT pk_FilmScenariste PRIMARY KEY (film_id, scenariste_id)
);

CREATE TABLE Genre (
  id_genre NUMBER(38) PRIMARY KEY,
  nom VARCHAR(255)
);

CREATE TABLE FilmGenre (
  film_id NUMBER(38) NOT NULL REFERENCES Film(id_film),
  genre_id NUMBER(38) NOT NULL REFERENCES Genre(id_genre),
  CONSTRAINT pk_FilmGenre PRIMARY KEY (film_id, genre_id)
);

CREATE TABLE Role (
  id_role NUMBER(38) PRIMARY KEY,
  personnage VARCHAR(255),
  acteur_id NUMBER(38) NOT NULL REFERENCES Personne(id_personne),
  film_id NUMBER(38) NOT NULL REFERENCES Film(id_film)
);

CREATE TABLE PaysDeProduction (
  id_pays NUMBER(38) PRIMARY KEY,
  nom VARCHAR(255)
);

CREATE TABLE FilmPaysDeProduction (
  id_film NUMBER(38) NOT NULL REFERENCES Film(id_film),
  paysDeProduction_id NUMBER(38) NOT NULL REFERENCES PaysDeProduction(id_pays),
  CONSTRAINT pk_FilmPaysDeProduction PRIMARY KEY (id_film, paysDeProduction_id)
);

CREATE TABLE CopieFilm (
  numero_de_code VARCHAR(255) PRIMARY KEY,
  est_retourner NUMBER(1),
  film_id NUMBER(38) NOT NULL REFERENCES Film(id_film)
);

CREATE TABLE Reservation (
  id_reservation NUMBER(38) PRIMARY KEY,
  date_de_reservation DATE,
  date_de_retour_prevue DATE,
  client_id NUMBER(38) NOT NULL REFERENCES Client(id_client),
  copieFilm_numero_de_code VARCHAR(255) NOT NULL REFERENCES CopieFilm(numero_de_code)
);

CREATE TABLE LienVersLaBandeAnnonce (
  id_lien NUMBER(38) PRIMARY KEY,
  lien VARCHAR(255),
  film_id NUMBER(38) NOT NULL REFERENCES Film(id_film)
);

CREATE OR REPLACE TRIGGER check_max_reservations
BEFORE INSERT ON Reservation
FOR EACH ROW
DECLARE
    v_reservation_max_copies NUMBER;
    v_current_rentals NUMBER;
BEGIN
    -- Get the max number of reservations
    SELECT a.reservation_max_copies
    INTO v_reservation_max_copies
    FROM Client c
    JOIN Abonnement a ON c.code_abonnement = a.code
    WHERE c.id_client = :NEW.client_id;

    -- Count the current active reservations
    SELECT COUNT(*)
    INTO v_current_rentals
    FROM Reservation r
    JOIN CopieFilm cf ON r.copieFilm_numero_de_code = cf.numero_de_code
    WHERE r.client_id = :NEW.client_id
    AND cf.est_retourner = 0;

    -- Check if the client exceeds the max reservations allowed
    IF v_current_rentals >= v_reservation_max_copies THEN
        RAISE_APPLICATION_ERROR(-20001, 'The client has reached the maximum number of allowed rentals.');
    END IF;
END;

CREATE OR REPLACE TRIGGER check_movie_availability
BEFORE INSERT ON Reservation
FOR EACH ROW
DECLARE
    v_est_retourner NUMBER(1);
BEGIN
    -- Check if the movie copy is available for rental
    SELECT est_retourner
    INTO v_est_retourner
    FROM CopieFilm
    WHERE numero_de_code = :NEW.copieFilm_numero_de_code;

    -- Raise an error if the movie copy is not available (i.e., not returned)
    IF v_est_retourner = 0 THEN
        RAISE_APPLICATION_ERROR(-20002, 'The selected movie copy is not available for rental.');
    END IF;
END;

CREATE OR REPLACE TRIGGER trg_check_credit_card_expiration
BEFORE INSERT OR UPDATE ON Client
FOR EACH ROW
BEGIN
  IF :NEW.date_expiration < TRUNC(SYSDATE) THEN
    RAISE_APPLICATION_ERROR(-20001, 'Expiration date must be today or in the future.');
  END IF;
END;

CREATE OR REPLACE TRIGGER trg_check_age
BEFORE INSERT OR UPDATE ON Client
FOR EACH ROW
BEGIN
  -- Calculate the age in years
  IF TRUNC(MONTHS_BETWEEN(SYSDATE, :NEW.date_de_naissance) / 12) < 18 THEN
    RAISE_APPLICATION_ERROR(-20002, 'Client must be at least 18 years old.');
  END IF;
END;