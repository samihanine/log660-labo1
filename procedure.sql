CREATE OR REPLACE PROCEDURE Ajouter_Client(
    p_id_client INT,
    p_nom VARCHAR2,
    p_prenom VARCHAR2,
    p_date_de_naissance DATE,
    p_courriel VARCHAR2,
    p_telephone VARCHAR2,
    p_mot_de_passe VARCHAR2,
    p_adresse VARCHAR2,
    p_ville VARCHAR2,
    p_province VARCHAR2,
    p_code_postal VARCHAR2,
    p_carte VARCHAR2,
    p_numero VARCHAR2,
    p_cvv VARCHAR2,
    p_date_expiration DATE,
    p_code_abonnement VARCHAR2
)
IS
BEGIN
    INSERT INTO Client (
        id_client,
        nom,
        prenom,
        date_de_naissance,
        courriel,
        telephone,
        mot_de_passe,
        adresse,
        ville,
        province,
        code_postal,
        carte,
        numero,
        cvv,
        date_expiration,
        code_abonnement
    ) VALUES (
        p_id_client,
        p_nom,
        p_prenom,
        p_date_de_naissance,
        p_courriel,
        p_telephone,
        p_mot_de_passe,
        p_adresse,
        p_ville,
        p_province,
        p_code_postal,
        p_carte,
        p_numero,
        p_cvv,
        p_date_expiration,
        p_code_abonnement
    );
END;

CREATE OR REPLACE PROCEDURE Faire_Reservation(
    p_id_reservation INT,
    p_date_de_reservation DATE,
    p_date_de_retour_prevue DATE,
    p_client_id INT,
    p_copieFilm_numero_de_code VARCHAR2
)
IS
BEGIN
    INSERT INTO Reservation (
        id_reservation,
        date_de_reservation,
        date_de_retour_prevue,
        client_id,
        copieFilm_numero_de_code
    ) VALUES (
        p_id_reservation,
        p_date_de_reservation,
        p_date_de_retour_prevue,
        p_client_id,
        p_copieFilm_numero_de_code
    );
END;
