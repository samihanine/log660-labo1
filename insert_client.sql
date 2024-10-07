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
  1003,  -- id_client: Unique identifier
  'Dupont',  -- nom: Last name
  'Élise',  -- prenom: First name
  TO_DATE('1990-05-15', 'YYYY-MM-DD'),  -- date_de_naissance: Date of birth (over 18)
  'elise.dupont3@example.com',  -- courriel: Unique email
  '514-123-4567',  -- telephone: Phone number
  'Passw0rd',  -- mot_de_passe: Password (meets constraints)
  '123 Rue Principale',  -- adresse: Address
  'Montréal',  -- ville: City
  'Québec',  -- province: Province
  'H2X 1Y4',  -- code_postal: Postal code
  'VISA',  -- carte: Card type (valid)
  '4111111111111111',  -- numero: Card number
  '123',  -- cvv: CVV code
  TO_DATE('2027-05-15', 'YYYY-MM-DD'),  -- date_expiration: 2 years from today
  'A'  -- code_abonnement: Abonnement code (ensure 'A001' exists in Abonnement table)
);