package ca.etsmtl.log660;

import java.io.FileInputStream;
import java.io.IOException;

import java.io.InputStream;

import java.sql.*;

import java.sql.Date;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;



public class LectureBD {
   private Connection connection = null;
   private int clientSize = 0;
   private int personneSize = 0;
   private int filmSize = 0;
   private int idRole = 1;
   private Map<String, Integer> paysSaved = new HashMap<>();
   private int paysId = 1;

   private Map<String, Integer> genresSaved =  new HashMap<>();
   private int genredId = 1;
   private Map<String, Integer> scenaristeSaved = new HashMap<>();
   private int scenaristeId = 1;

   private int personeAdded = 0;
   private PreparedStatement clientStatement = null;
   private PreparedStatement personneStatement = null;


   public class Role {
      public Role(int i, String n, String p) {
         id = i;
         nom = n;
         personnage = p;
      }
      protected int id;
      protected String nom;
      protected String personnage;
   }
   
   public LectureBD() {
      connectionBD();                     
   }
   
   
   public void lecturePersonnes(String nomFichier){

      String sql = "INSERT INTO PERSONNE(ID_PERSONNE, NOM, PRENOM, DATE_DE_NAISSANCE, LIEU_DE_NAISSANCE, " +
              "BIOGRAPHIE, PHOTO) VALUES (?,?,?,?,?,?,?)";

      try {
         personneStatement = connection.prepareStatement(sql);
         XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
         XmlPullParser parser = factory.newPullParser();

         InputStream is = new FileInputStream(nomFichier);
         parser.setInput(is, null);

         int eventType = parser.getEventType();

         String tag = null, 
                nom = null,
                anniversaire = null,
                lieu = null,
                photo = null,
                bio = null;
         
         int id = -1;
         
         while (eventType != XmlPullParser.END_DOCUMENT) 
         {
            if(eventType == XmlPullParser.START_TAG) 
            {
               tag = parser.getName();
               
               if (tag.equals("personne") && parser.getAttributeCount() == 1)
                  id = Integer.parseInt(parser.getAttributeValue(0));
            } 
            else if (eventType == XmlPullParser.END_TAG) 
            {                              
               tag = null;
               
               if (parser.getName().equals("personne") && id >= 0)
               {
                  insertionPersonne(id,nom,anniversaire,lieu,photo,bio);
                                    
                  id = -1;
                  nom = null;
                  anniversaire = null;
                  lieu = null;
                  photo = null;
                  bio = null;
               }
            }
            else if (eventType == XmlPullParser.TEXT && id >= 0) 
            {
               if (tag != null)
               {                                    
                  if (tag.equals("nom"))
                     nom = parser.getText();
                  else if (tag.equals("anniversaire"))
                     anniversaire = parser.getText();
                  else if (tag.equals("lieu"))
                     lieu = parser.getText();
                  else if (tag.equals("photo"))
                     photo = parser.getText();
                  else if (tag.equals("bio"))
                     bio = parser.getText();
               }              
            }
            
            eventType = parser.next();            
         }
         if (personeAdded % 100 != 0){
            personneStatement.executeBatch();
         }
         personneStatement.close();
      }
      catch (XmlPullParserException e) {
          System.out.println(e);   
       }
       catch (IOException e) {
         System.out.println("IOException while parsing " + nomFichier); 
       } catch (SQLException e){
         System.out.println("SQLException while parsing " + nomFichier);
      }
      System.out.println("Personne Size: "+ personneSize);
   }   
   
   public void lectureFilms(String nomFichier){
      try {

         XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
         XmlPullParser parser = factory.newPullParser();

         InputStream is = new FileInputStream(nomFichier);
         parser.setInput(is, null);

         int eventType = parser.getEventType();

         String tag = null, 
                titre = null,
                langue = null,
                poster = null,
                roleNom = null,
                rolePersonnage = null,
                realisateurNom = null,
                resume = null;
         
         ArrayList<String> pays = new ArrayList<String>();
         ArrayList<String> genres = new ArrayList<String>();
         ArrayList<String> scenaristes = new ArrayList<String>();
         ArrayList<Role> roles = new ArrayList<Role>();         
         ArrayList<String> annonces = new ArrayList<String>();
         
         int id = -1,
             annee = -1,
             duree = -1,
             roleId = -1,
             realisateurId = -1;
         
         while (eventType != XmlPullParser.END_DOCUMENT) 
         {
            if(eventType == XmlPullParser.START_TAG) 
            {
               tag = parser.getName();
               
               if (tag.equals("film") && parser.getAttributeCount() == 1)
                  id = Integer.parseInt(parser.getAttributeValue(0));
               else if (tag.equals("realisateur") && parser.getAttributeCount() == 1)
                  realisateurId = Integer.parseInt(parser.getAttributeValue(0));
               else if (tag.equals("acteur") && parser.getAttributeCount() == 1)
                  roleId = Integer.parseInt(parser.getAttributeValue(0));
            } 
            else if (eventType == XmlPullParser.END_TAG) 
            {                              
               tag = null;
               
               if (parser.getName().equals("film") && id >= 0)
               {
                  insertionFilm(id,titre,annee,pays,langue,
                             duree,resume,genres,realisateurNom,
                             realisateurId, scenaristes,
                             roles,poster,annonces);
                                    
                  id = -1;
                  annee = -1;
                  duree = -1;
                  titre = null;                                 
                  langue = null;                  
                  poster = null;
                  resume = null;
                  realisateurNom = null;
                  roleNom = null;
                  rolePersonnage = null;
                  realisateurId = -1;
                  roleId = -1;
                  
                  genres.clear();
                  scenaristes.clear();
                  roles.clear();
                  annonces.clear();  
                  pays.clear();
               }
               if (parser.getName().equals("role") && roleId >= 0) 
               {              
                  roles.add(new Role(roleId, roleNom, rolePersonnage));
                  roleId = -1;
                  roleNom = null;
                  rolePersonnage = null;
               }
            }
            else if (eventType == XmlPullParser.TEXT && id >= 0) 
            {
               if (tag != null)
               {                                    
                  if (tag.equals("titre"))
                     titre = parser.getText();
                  else if (tag.equals("annee"))
                     annee = Integer.parseInt(parser.getText());
                  else if (tag.equals("pays"))
                     pays.add(parser.getText());
                  else if (tag.equals("langue"))
                     langue = parser.getText();
                  else if (tag.equals("duree"))                 
                     duree = Integer.parseInt(parser.getText());
                  else if (tag.equals("resume"))                 
                     resume = parser.getText();
                  else if (tag.equals("genre"))
                     genres.add(parser.getText());
                  else if (tag.equals("realisateur"))
                     realisateurNom = parser.getText();
                  else if (tag.equals("scenariste"))
                     scenaristes.add(parser.getText());
                  else if (tag.equals("acteur"))
                     roleNom = parser.getText();
                  else if (tag.equals("personnage"))
                     rolePersonnage = parser.getText();
                  else if (tag.equals("poster"))
                     poster = parser.getText();
                  else if (tag.equals("annonce"))
                     annonces.add(parser.getText());                  
               }              
            }
            
            eventType = parser.next();            
         }
      }
      catch (XmlPullParserException e) {
          System.out.println(e);   
      }
      catch (IOException e) {
         System.out.println("IOException while parsing " + nomFichier); 
      }
      System.out.println("Film Size: "+ filmSize);
   }
   
   public void lectureClients(String nomFichier){
      String sql = "INSERT INTO CLIENT " +
              "(ID_CLIENT, NOM, PRENOM, DATE_DE_NAISSANCE, COURRIEL, TELEPHONE, MOT_DE_PASSE, ADRESSE, VILLE, PROVINCE, CODE_POSTAL, CARTE, NUMERO, CVV, DATE_EXPIRATION, CODE_ABONNEMENT) " +
              "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

      //String sql = "{call Ajouter_Client(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)} ";
      try {
         clientStatement = connection.prepareStatement(sql);
         //clientCallableStatement = connection.prepareCall(sql);
         XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
         XmlPullParser parser = factory.newPullParser();

         InputStream is = new FileInputStream(nomFichier);
         parser.setInput(is, null);

         int eventType = parser.getEventType();               

         String tag = null, 
                nomFamille = null,
                prenom = null,
                courriel = null,
                tel = null,
                anniv = null,
                adresse = null,
                ville = null,
                province = null,
                codePostal = null,
                carte = null,
                noCarte = null,
                motDePasse = null,
                forfait = null;                                 
         
         int id = -1,
             expMois = -1,
             expAnnee = -1;
         
         while (eventType != XmlPullParser.END_DOCUMENT) 
         {
            if(eventType == XmlPullParser.START_TAG) 
            {
               tag = parser.getName();
               
               if (tag.equals("client") && parser.getAttributeCount() == 1)
                  id = Integer.parseInt(parser.getAttributeValue(0));
            } 
            else if (eventType == XmlPullParser.END_TAG) 
            {                              
               tag = null;
               
               if (parser.getName().equals("client") && id >= 0)
               {
                  insertionClient(id,nomFamille,prenom,courriel,tel,
                             anniv,adresse,ville,province,
                             codePostal,carte,noCarte, 
                             expMois,expAnnee,motDePasse,forfait);               
                                    
                  nomFamille = null;
                  prenom = null;
                  courriel = null;               
                  tel = null;
                  anniv = null;
                  adresse = null;
                  ville = null;
                  province = null;
                  codePostal = null;
                  carte = null;
                  noCarte = null;
                  motDePasse = null; 
                  forfait = null;
                  
                  id = -1;
                  expMois = -1;
                  expAnnee = -1;
               }
            }
            else if (eventType == XmlPullParser.TEXT && id >= 0) 
            {         
               if (tag != null)
               {                                    
                  if (tag.equals("nom-famille"))
                     nomFamille = parser.getText();
                  else if (tag.equals("prenom"))
                     prenom = parser.getText();
                  else if (tag.equals("courriel"))
                     courriel = parser.getText();
                  else if (tag.equals("tel"))
                     tel = parser.getText();
                  else if (tag.equals("anniversaire"))
                     anniv = parser.getText();
                  else if (tag.equals("adresse"))
                     adresse = parser.getText();
                  else if (tag.equals("ville"))
                     ville = parser.getText();
                  else if (tag.equals("province"))
                     province = parser.getText();
                  else if (tag.equals("code-postal"))
                     codePostal = parser.getText();
                  else if (tag.equals("carte"))
                     carte = parser.getText();
                  else if (tag.equals("no"))
                     noCarte = parser.getText();
                  else if (tag.equals("exp-mois"))                 
                     expMois = Integer.parseInt(parser.getText());
                  else if (tag.equals("exp-annee"))                 
                     expAnnee = Integer.parseInt(parser.getText());
                  else if (tag.equals("mot-de-passe"))                 
                     motDePasse = parser.getText();  
                  else if (tag.equals("forfait"))                 
                     forfait = parser.getText(); 
               }              
            }
            
            eventType = parser.next();            
         }
         clientStatement.close();
      }
      catch (XmlPullParserException e) {
          System.out.println(e);   
      }
      catch (IOException e) {
         System.out.println("IOException while parsing " + nomFichier); 
      } catch (SQLException e){
         throw new RuntimeException();
      }

   }   
   
   private void insertionPersonne(int id, String nom, String anniv, String lieu, String photo, String bio) {      
      // On insere la personne dans la BD

      // Définir la requête SQL d'insertion
      personeAdded++;
      try {
         personneStatement.setInt(1,id);
         personneStatement.setString(2,nom);
         personneStatement.setString(3,nom);
         personneStatement.setString(4,anniv);
         personneStatement.setString(5,lieu);
         personneStatement.setString(6,bio);
         personneStatement.setString(7, photo);
         personneStatement.addBatch();
         if (personeAdded % 100 == 0){
            personneStatement.executeBatch();
         }
      } catch (SQLException e){
         System.out.println("Personne " + personneSize+" " + e.getMessage());
      }

      personneSize++;

   }
   
   private void insertionFilm(int id, String titre, int annee,
                           ArrayList<String> pays, String langue, int duree, String resume,
                           ArrayList<String> genres, String realisateurNom, int realisateurId,
                           ArrayList<String> scenaristes,
                           ArrayList<Role> roles, String poster,
                           ArrayList<String> annonces) {


      System.out.println(filmSize + " / 629");

      String sqlInsertFilms = "INSERT INTO FILM (ID_FILM, TITRE, ANNEE_DE_SORTIE, LANGUE_ORIGINALE, DUREE_EN_MINUTES, " +
              "RESUME_SCENARIO, AFFICHE, REALISATEUR_ID) VALUES (?,?,?,?,?,?,?,?)";

      // Films
      try (PreparedStatement filmStatement = connection.prepareStatement(sqlInsertFilms)) {
         filmStatement.setInt(1, id);
         filmStatement.setString(2, titre);
         filmStatement.setInt(3, annee);
         filmStatement.setString(4, langue);
         filmStatement.setInt(5, duree);
         filmStatement.setString(6, resume);
         filmStatement.setString(7, poster);
         filmStatement.setInt(8, realisateurId);
         filmStatement.executeUpdate();
      } catch (SQLException e){
         System.out.println("Film "  + e.getMessage());
         return;
      }


      // Roles
      String sqlInsertRole = "INSERT INTO ROLE (PERSONNAGE, ACTEUR_ID, FILM_ID) VALUES (?,?,?)";

      try ( PreparedStatement extraDataStatement = connection.prepareStatement(sqlInsertRole)) {

         for (Role role: roles) {
               extraDataStatement.setString(1, role.personnage);
               extraDataStatement.setInt(2, role.id);
               extraDataStatement.setInt(3, id);
               extraDataStatement.addBatch();
         }
         extraDataStatement.executeBatch();
      } catch (SQLException e){
         System.out.println("Role " + e.getMessage());
      }

      //Annonces
      try(PreparedStatement extraDataStatement = connection.prepareStatement("INSERT INTO LIENVERSLABANDEANNONCE (LIEN, FILM_ID) VALUES (?, ?)");) {
         for (String annonce: annonces) {
            extraDataStatement.setString(1, annonce);
            extraDataStatement.setInt(2, id);
            extraDataStatement.addBatch();
         }
         if(!annonces.isEmpty()){
            extraDataStatement.executeBatch();
         }
      } catch (SQLException e){
         System.out.println("Lien Annonce " +  e.getMessage());
      }

      // Scenariste
      try(PreparedStatement extraDataStatement = connection.prepareStatement("INSERT INTO SCENARISTE (ID_SCENARISTE, NOM) VALUES (?, ?)")) {
         boolean addScenariste = false;
         for (String scenariste: scenaristes) {
            int idS = scenaristeSaved.getOrDefault(scenariste, 0);
            if(idS == 0){
               idS = scenaristeId + 1;
               scenaristeSaved.put(scenariste, idS);
               scenaristeId++;
               addScenariste = true;
               extraDataStatement.setInt(1, idS);
               extraDataStatement.setString(2, scenariste);
               extraDataStatement.addBatch();
            }

         }
         if (addScenariste) {
            extraDataStatement.executeBatch();
         }
      } catch (SQLException e){
         System.out.println("scenariste " + e.getMessage());
      }

      // film scenariste
      try ( PreparedStatement extraDataStatement = connection.prepareStatement("INSERT INTO  FILMSCENARISTE (FILM_ID, SCENARISTE_ID) VALUES (?,?)");) {
         for (String scenariste: scenaristes) {
            extraDataStatement.setInt(1, id);
            extraDataStatement.setInt(2, scenaristeSaved.getOrDefault(scenariste, 0));
            extraDataStatement.addBatch();
         }
         extraDataStatement.executeBatch();
      }  catch (SQLException e){
         System.out.println("film scenariste "  + e.getMessage());
      }



      // genres
      try (PreparedStatement extraDataStatement = connection.prepareStatement("INSERT INTO GENRE (ID_GENRE, NOM) VALUES (?, ?)")) {
         boolean addgenre = false;
         if(id == 102798){
            System.out.println("");
         }
         for (String genre: genres) {
            int idG = genresSaved.getOrDefault(genre, 0);
            if(idG == 0){
               idG = genredId;
               genresSaved.put(genre, idG);
               genredId++;
               addgenre = true;
               extraDataStatement.setInt(1, idG);
               extraDataStatement.setString(2, genre);
               extraDataStatement.addBatch();
            }
         }
         if (addgenre) {
            extraDataStatement.executeBatch();
         }
      } catch (SQLException e){
         System.out.println("genre  " + e.getMessage());
         System.out.println();
      }

      // film genre
      try(PreparedStatement extraDataStatement = connection.prepareStatement("INSERT INTO FILMGENRE (FILM_ID, GENRE_ID) VALUES  (?, ?)")) {
         for (String genre: genres) {
            extraDataStatement.setInt(1, id);
            extraDataStatement.setInt(2, genresSaved.getOrDefault(genre, 0));
            extraDataStatement.addBatch();
         }
         extraDataStatement.executeBatch();
      }  catch (SQLException e){
         System.out.println("film genre " + e.getMessage());
      }



      // pays
      try(PreparedStatement extraDataStatement = connection.prepareStatement("INSERT INTO PAYSDEPRODUCTION (ID_PAYS, NOM) VALUES (?, ?)")) {
         boolean addPays = false;
         for (String p: pays) {
            int idP = paysSaved.getOrDefault(p, 0);
            if(idP == 0){
               idP = paysId;
               paysSaved.put(p, idP);
               paysId++;
               addPays = true;
               extraDataStatement.setInt(1, idP);
               extraDataStatement.setString(2, p);
               extraDataStatement.addBatch();
            }

         }
         if (addPays) {
            extraDataStatement.executeBatch();
         }
      } catch (SQLException e){
         System.out.println("Pays " + e.getMessage());
      }

      // film pays
      try(PreparedStatement extraDataStatement = connection.prepareStatement("INSERT INTO FILMPAYSDEPRODUCTION (ID_FILM, PAYSDEPRODUCTION_ID) VALUES (? , ?)")) {
         for (String p: pays) {
            extraDataStatement.setInt(1, id);
            extraDataStatement.setInt(2, paysSaved.getOrDefault(p, 0));
            extraDataStatement.addBatch();
         }
         extraDataStatement.executeBatch();
      }  catch (SQLException e){
         System.out.println("film pays "  + e.getMessage());
      }


      try(PreparedStatement extraDataStatement = connection.prepareStatement("INSERT INTO COPIEFILM (NUMERO_DE_CODE, EST_RETOURNER, FILM_ID) VALUES (?, ?, ?)")) {
         Random random = new Random();
         int randomNumber = random.nextInt(10) + 1;
         for (int i = 1; i <= randomNumber; i++) {
            extraDataStatement.setString(1, id+"_"+i);
            extraDataStatement.setInt(2, 0);
            extraDataStatement.setInt(3, id);
            extraDataStatement.addBatch();
         }
         extraDataStatement.executeBatch();
      }  catch (SQLException e){
         System.out.println("film copie  " + e.getMessage());
      }




      filmSize++;
   }



   /*

   private void insertionFilm(int id, String titre, int annee,
                           ArrayList<String> pays, String langue, int duree, String resume,
                           ArrayList<String> genres, String realisateurNom, int realisateurId,
                           ArrayList<String> scenaristes,
                           ArrayList<Role> roles, String poster,
                           ArrayList<String> annonces) {
      String sqlInsertFilms = "INSERT INTO FILM (ID_FILM, TITRE, ANNEE_DE_SORTIE, LANGUE_ORIGINALE, DUREE_EN_MINUTES, " +
              "RESUME_SCENARIO, AFFICHE, REALISATEUR_ID) VALUES (?,?,?,?,?,?,?,?)";

      try (PreparedStatement filmStatement = connection.prepareStatement(sqlInsertFilms)) {
         filmStatement.setInt(1, id);
         filmStatement.setString(2, titre);
         filmStatement.setInt(3, annee);
         filmStatement.setString(4, langue);
         filmStatement.setInt(5, duree);
         filmStatement.setString(6, resume);
         filmStatement.setString(7, poster);
         filmStatement.setInt(8, realisateurId);
         filmStatement.executeUpdate();
      } catch (SQLException e){
         System.out.println("Film " + filmSize+" " + e.getMessage());
         return;
      }

      String sqlInsertRole = "INSERT INTO ROLE (PERSONNAGE, ACTEUR_ID, FILM_ID) VALUES (?,?,?)";
      for (Role role: roles) {
         try (PreparedStatement roleStatement = connection.prepareStatement(sqlInsertRole)) {
            roleStatement.setString(1, role.personnage);
            roleStatement.setInt(2, role.id);
            roleStatement.setInt(3, id);
            roleStatement.executeUpdate();
         } catch (SQLException e){
            System.out.println("Role " + e.getMessage());

         }
      }

      String sqlInsertAnnonce = "INSERT INTO LIENVERSLABANDEANNONCE (LIEN, FILM_ID) VALUES (?, ?)";
      for (String annonce: annonces) {
         try (PreparedStatement annonceStatement = connection.prepareStatement(sqlInsertAnnonce)) {
            annonceStatement.setString(1, annonce);
            annonceStatement.setInt(2, id);
            annonceStatement.executeUpdate();
         } catch (SQLException e){
            System.out.println("Annonce " + e.getMessage());
         }
      }

      // insertion des scenaristes
      String sqlInsertScenariste = "INSERT INTO SCENARISTE (ID_SCENARISTE, NOM) VALUES (?, ?)";
      String sqlInsertFilmScenariste = "INSERT INTO FILMSCENARISTE (FILM_ID, SCENARISTE_ID) VALUES (?, ?)";
      for (String scenariste: scenaristes) {
         int idS = scenaristeSaved.getOrDefault(scenariste, 0);
         if(idS == 0){
            try (PreparedStatement scenaristeStatement = connection.prepareStatement(sqlInsertScenariste)) {
               scenaristeStatement.setInt(1, scenaristeId);
               scenaristeStatement.setString(2, scenariste);
               scenaristeStatement.executeUpdate();
               idS = scenaristeId;
               scenaristeSaved.put(scenariste, idS);
               scenaristeId++;
            } catch (SQLException e){
               System.out.println("Annonce " + e.getMessage());
               continue;
            }
         }
         try (PreparedStatement scenaristeStatement = connection.prepareStatement(sqlInsertFilmScenariste)) {
            scenaristeStatement.setInt(2, idS);
            scenaristeStatement.setInt(1, id);
            scenaristeStatement.executeUpdate();
         } catch (SQLException e){
            System.out.println("Film scenariste Statement" + e.getMessage());
         }
      }

      // insertion du genre
      String sqlInsertGenre = "INSERT INTO GENRE (ID_GENRE, NOM) VALUES (?, ?)";
      String sqlInsertFilmGenre = "INSERT INTO FILMGENRE (FILM_ID, GENRE_ID) VALUES (?,?)";
      for (String genre: genres) {
         int idG = genresSaved.getOrDefault(genre, 0);
         if(idG == 0){
            try (PreparedStatement genreStatement = connection.prepareStatement(sqlInsertGenre)) {
               genreStatement.setInt(1, genredId);
               genreStatement.setString(2, genre);
               genreStatement.executeUpdate();
               idG = genredId;
               genresSaved.put(genre, idG);
               genredId++;
            } catch (SQLException e){
               System.out.println("Genre " + e.getMessage());
               continue;
            }
         }
         try (PreparedStatement FilmGenreStatement = connection.prepareStatement(sqlInsertFilmGenre)) {
            FilmGenreStatement.setInt(2, idG);
            FilmGenreStatement.setInt(1, id);
            FilmGenreStatement.executeUpdate();
         } catch (SQLException e){
            System.out.println("Film Genre Statement" + e.getMessage());
         }
      }

      // insertion des pays
      String sqlInsertPays = "INSERT INTO PAYSDEPRODUCTION (ID_PAYS, NOM) VALUES (?, ?)";
      String sqlInsertFilmPays = "INSERT INTO FILMPAYSDEPRODUCTION (ID_FILM, PAYSDEPRODUCTION_ID) VALUES (? , ?)";
      for (String p: pays) {
         int idP = paysSaved.getOrDefault(p, 0);
         if(idP == 0){
            try (PreparedStatement paysStatement = connection.prepareStatement(sqlInsertPays)) {
               paysStatement.setInt(1, paysId);
               paysStatement.setString(2, p);
               paysStatement.executeUpdate();
               idP = paysId;
               paysSaved.put(p, idP);
               paysId++;
            } catch (SQLException e){
               System.out.println("Pays " + e.getMessage());
               continue;
            }
         }
         try (PreparedStatement filmPaysStatement = connection.prepareStatement(sqlInsertFilmPays)) {
            filmPaysStatement.setInt(2, idP);
            filmPaysStatement.setInt(1, id);
            filmPaysStatement.executeUpdate();
         } catch (SQLException e){
            System.out.println("Film Pays Statement" + e.getMessage());
         }
      }


      filmSize++;

   }

   * */


   private void insertionClient(int id, String nomFamille, String prenom,
                             String courriel, String tel, String anniv,
                             String adresse, String ville, String province,
                             String codePostal, String carte, String noCarte,
                             int expMois, int expAnnee, String motDePasse,
                             String forfait) {

      System.out.println("No: " + clientSize);

      try{
         LocalDate expireDate = LocalDate.of(expAnnee, expMois, 1).plusMonths(1).minusDays(1);
         LocalDate annivDate = LocalDate.parse(anniv, DateTimeFormatter.ofPattern("yyyy-MM-dd"));


         clientStatement.setInt(1, id);
         clientStatement.setString(2, nomFamille);
         clientStatement.setString(3, prenom);
         clientStatement.setDate(4, Date.valueOf(annivDate));
         clientStatement.setString(5, courriel);
         clientStatement.setString(6, tel);
         clientStatement.setString(7, motDePasse);
         clientStatement.setString(8, adresse);
         clientStatement.setString(9, ville);
         clientStatement.setString(10, province);
         clientStatement.setString(11, codePostal);
         clientStatement.setString(12, carte);
         clientStatement.setString(13, noCarte);
         clientStatement.setString(14, "123");
         clientStatement.setDate(15, Date.valueOf(expireDate));
         clientStatement.setString(16, forfait);

         clientStatement.execute();


      } catch (SQLException | DateTimeException e) {
         System.out.println("Clients ID: "+ id + " -- "+ e.getMessage());
      }

      clientSize++;
   }

   private void connectionBD() {
      Properties props = new Properties();
      try(InputStream input = LectureBD.class.getClassLoader().getResourceAsStream("config.properties")) {
         Class.forName("oracle.jdbc.driver.OracleDriver");
         props.load(input);
         String url = props.getProperty("db.url");
         String user = props.getProperty("db.username");
         String password = props.getProperty("db.password");

         // Établir la connexion
         connection = DriverManager.getConnection(url, user, password);
         System.out.println("Connexion réussie !");
      } catch (SQLException e) {
         System.out.println("Erreur de connexion : " + e.getMessage());
      } catch (IOException | ClassNotFoundException e) {
          throw new RuntimeException(e);
      }
   }

   public static void main(String[] args) {
      long startTime = System.currentTimeMillis();
      LectureBD lecture = new LectureBD();
      System.out.println("Personnes ...");
      lecture.lecturePersonnes(args[0]);
      System.out.println("Films ...");
      lecture.lectureFilms(args[1]);
      System.out.println("Client ...");
      lecture.lectureClients(args[2]);
      long endtTime = System.currentTimeMillis();
      // 10. Convertir la durée en minutes et secondes
      long minutes = TimeUnit.MILLISECONDS.toMinutes(endtTime - startTime);
      long seconds = TimeUnit.MILLISECONDS.toSeconds(endtTime - startTime) - TimeUnit.MINUTES.toSeconds(minutes);

      // 11. Afficher la durée en minutes et secondes
      System.out.println("Durée d'exécution : " + minutes + " minutes et " + seconds + " secondes");

   }
}
