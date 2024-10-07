-- Insert for Débutant Plan
INSERT INTO Abonnement (
  code,
  forfait,
  cout,
  reservation_max_copies,
  duree_max_jours
) VALUES (
  'D',                   -- code: Débutant
  'Débutant',            -- forfait: Débutant
  5.0,                   -- cout: 5$ / mois
  1,                     -- reservation_max_copies: 1 film
  10                     -- duree_max_jours: 10 jours
);

-- Insert for Intermédiaire Plan
INSERT INTO Abonnement (
  code,
  forfait,
  cout,
  reservation_max_copies,
  duree_max_jours
) VALUES (
  'I',                   -- code: Intermédiaire
  'Intermédiaire',       -- forfait: Intermédiaire
  10.0,                  -- cout: 10$ / mois
  5,                     -- reservation_max_copies: 5 films
  30                     -- duree_max_jours: 30 jours
);

-- Insert for Avancé Plan
INSERT INTO Abonnement (
  code,
  forfait,
  cout,
  reservation_max_copies,
  duree_max_jours
) VALUES (
  'A',                   -- code: Avancé
  'Avancé',              -- forfait: Avancé
  15.0,                  -- cout: 15$ / mois
  10,                    -- reservation_max_copies: 10 films
  9999                   -- duree_max_jours: illimitée (represented as 9999)
);
