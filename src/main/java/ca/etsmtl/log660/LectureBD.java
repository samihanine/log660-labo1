package ca.etsmtl.log660;

import java.io.FileInputStream;
import java.io.IOException;

import java.io.InputStream;

import java.sql.*;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Properties;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;



public class LectureBD {
   private Connection connection = null;
   private int clientSize = 0;
   private int personneSize = 0;
   private int filmSize = 0;

   private PreparedStatement clientStatement = null;

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
      try {
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
      }
      catch (XmlPullParserException e) {
          System.out.println(e);   
       }
       catch (IOException e) {
         System.out.println("IOException while parsing " + nomFichier); 
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

      personneSize++;

   }
   
   private void insertionFilm(int id, String titre, int annee,
                           ArrayList<String> pays, String langue, int duree, String resume,
                           ArrayList<String> genres, String realisateurNom, int realisateurId,
                           ArrayList<String> scenaristes,
                           ArrayList<Role> roles, String poster,
                           ArrayList<String> annonces) {         
      // On le film dans la BD
      filmSize++;

   }
   
   private void insertionClient(int id, String nomFamille, String prenom,
                             String courriel, String tel, String anniv,
                             String adresse, String ville, String province,
                             String codePostal, String carte, String noCarte,
                             int expMois, int expAnnee, String motDePasse,
                             String forfait) {
      try{
         LocalDate expireDate = LocalDate.of(expAnnee, expMois, 1).plusMonths(1).minusDays(1);
         LocalDate annivDate = LocalDate.parse(anniv, DateTimeFormatter.ofPattern("yyyy-MM-dd"));


         /*abbiv
          p_id_client INT,p_nom VARCHAR2,p_prenom VARCHAR2,
          p_date_de_naissance DATE,p_courriel VARCHAR2,p_telephone VARCHAR2,
          p_mot_de_passe VARCHAR2,p_adresse VARCHAR2,p_ville VARCHAR2,
          p_province VARCHAR2,p_code_postal VARCHAR2,p_carte VARCHAR2,
          p_numero VARCHAR2,
          p_cvv VARCHAR2,
          p_date_expiration DATE,
          p_code_abonnement VARCHAR2
         * */
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
         System.out.println("Clients Size: "+ clientSize + " -- "+ e.getMessage());
      }

      clientSize++;
   }

   /*private void insertionClient(int id, String nomFamille, String prenom,
                             String courriel, String tel, String anniv,
                             String adresse, String ville, String province,
                             String codePostal, String carte, String noCarte,
                             int expMois, int expAnnee, String motDePasse,
                             String forfait) {
      try{
         LocalDate expireDate = LocalDate.of(expAnnee, expMois, 1).plusMonths(1).minusDays(1);
         LocalDate annivDate = LocalDate.parse(anniv, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

         clientCallableStatement.setInt(1, id);
         clientCallableStatement.setString(2, nomFamille);
         clientCallableStatement.setString(3, prenom);
         clientCallableStatement.setDate(4, Date.valueOf(annivDate));
         clientCallableStatement.setString(5, courriel);
         clientCallableStatement.setString(6, tel);
         clientCallableStatement.setString(7, motDePasse);
         clientCallableStatement.setString(8, adresse);
         clientCallableStatement.setString(9, ville);
         clientCallableStatement.setString(10, province);
         clientCallableStatement.setString(11, codePostal);
         clientCallableStatement.setString(12, carte);
         clientCallableStatement.setString(13, noCarte);
         clientCallableStatement.setString(14, "123");
         clientCallableStatement.setDate(15, Date.valueOf(expireDate));
         clientCallableStatement.setString(16, forfait);

         clientCallableStatement.execute();


      } catch (SQLException | DateTimeException e) {
         System.out.println("Clients Size: "+ clientSize + " -- "+ e.getMessage());
      }

      clientSize++;
   }*/


   
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
      LectureBD lecture = new LectureBD();
      lecture.lecturePersonnes(args[0]);
      lecture.lectureFilms(args[1]);
      lecture.lectureClients(args[2]);

   }
}
