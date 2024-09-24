CREATE TABLE Client (
  id_client NUMBER(38) PRIMARY KEY,
  courriel VARCHAR(255) UNIQUE,
  telephone VARCHAR(255),
  mot_de_passe VARCHAR(255),
  adresse VARCHAR(255),
  ville VARCHAR(255),
  province VARCHAR(255),
  code_postal VARCHAR(255),
  carte VARCHAR(15) CHECK (carte IN ('VISA', 'MasterCard', 'Amex')),
  numero VARCHAR(255),
  cvv VARCHAR(255),
  dateExpiration DATE
);

CREATE TABLE Employee (
  id_employee NUMBER(38) PRIMARY KEY,
  courriel VARCHAR(255) UNIQUE,
  telephone VARCHAR(255),
  mot_de_passe VARCHAR(255),
  adresse VARCHAR(255),
  ville VARCHAR(255),
  province VARCHAR(255),
  code_postal VARCHAR(255)
);

CREATE TABLE Réalisateur (
  id_realisateur NUMBER(38) PRIMARY KEY,
  nom VARCHAR(255),
  prenom VARCHAR(255),
  date_de_naissance DATE,
  lieu_de_naissance VARCHAR(255),
  biographie VARCHAR(255),
  photo VARCHAR(255)
);

CREATE TABLE Film (
  id_film NUMBER(38) PRIMARY KEY,
  titre VARCHAR(255),
  anneeDeSortie NUMBER(38),
  langue_originale VARCHAR(255),
  duree_en_minutes NUMBER(38),
  resume_scenario VARCHAR(255),
  affiche VARCHAR(255),
  realisateur_id NUMBER(38) NOT NULL REFERENCES Réalisateur(id_realisateur)
);

CREATE TABLE Abonnement (
  code CHAR PRIMARY KEY,
  forfait VARCHAR(255),
  cout FLOAT,
  reservation_max_copies NUMBER(38),
  duree_max_jours NUMBER(38) NOT NULL
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

CREATE TABLE Acteur (
  id_acteur NUMBER(38) PRIMARY KEY,
  nom VARCHAR(255),
  prenom VARCHAR(255),
  date_de_naissance DATE,
  lieu_de_naissance VARCHAR(255),
  biographie VARCHAR(255),
  photo VARCHAR(255)
);

CREATE TABLE Role (
  id_role NUMBER(38) PRIMARY KEY,
  personnage VARCHAR(255),
  acteur_id NUMBER(38) NOT NULL REFERENCES Acteur(id_acteur),
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
