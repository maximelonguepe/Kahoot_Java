package pootp1;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * contient l'ensemble des requetes sql êtant utilisé dans le jeu
 */
public class RequeteKahoot {
    private static Connection connect;
    private static String url = "jdbc:mysql://192.168.1.9:3306/kahoot2?zeroDateTimeBehavior=CONVERT_TO_NULL&serverTimezone=UTC";
    private static String user = "toto";
    private static String mdp = "tata";

    /**
     * Permet de se connecter a la bdd
     * @throws SQLException
     */
    public RequeteKahoot() throws SQLException {
        connect = DriverManager.getConnection(url, user, mdp);

    }
    /**
     *
     * @return la liste des catégories dans la bdd
     * @throws SQLException
     */

    public List<Categorie> getListeCategories() throws SQLException {
        List<Categorie> categories = new ArrayList<Categorie>();

        String requete = "SELECT * FROM categorie";
        Statement stmt = connect.createStatement();
        ResultSet res = stmt.executeQuery(requete);
        while (res.next()) {
            //System.out.println(res.getString("texteCategorie"));
            Categorie categorie = new Categorie(res.getString("texteCategorie"));
            categorie.setIdCategorie(res.getInt("idCATEGORIE"));
            categories.add(categorie);
        }
        res.close();
        stmt.close();
        return categories;
    }



//non utilisé
    public int getNbJoueurs() throws SQLException {
        int nombreJoueurs = 0;
        //connect = DriverManager.getConnection(url, user, mdp);
        String requete = "SELECT Count(*) as nombreJoueurs from joueur";
        Statement stmt = connect.createStatement();
        ResultSet res = stmt.executeQuery(requete);
        while (res.next()) {
            //System.out.println(res.getString("texteCategorie"));
            nombreJoueurs = (res.getInt("nombreJoueurs"));
            //categories.add(categorie);
        }
        res.close();
        stmt.close();
        //return categories;
        return nombreJoueurs;
    }

//non utilisé
    public Joueur getJoueur(int unIdJoueur) throws SQLException {
        String requete = "SELECT login, mdp FROM joueur " + "WHERE idJOUEUR = ?";
        PreparedStatement pstmt = connect.prepareStatement(requete);
        pstmt.setInt(1, unIdJoueur);
        ResultSet res = pstmt.executeQuery();
        Joueur joueur = new Joueur();
        while (res.next()) {

            joueur.setLogin((res.getString("login")));
            joueur.setMdp(res.getString("mdp"));

        }
        res.close();
        pstmt.close();
        return joueur;

    }

    /**
     *
     * @param idCategorie
     * @return les questions relatives à l'id de la catégorie renseigné
     * @throws SQLException
     */
    public List<Question> getQuestions(int idCategorie) throws SQLException {
        String requete = "SELECT id_question,id_bonne_reponse, texteQuestion FROM question " + "WHERE id_categorie = ?";
        PreparedStatement pstmt = connect.prepareStatement(requete);
        pstmt.setInt(1, idCategorie);
        ResultSet res = pstmt.executeQuery();
        List<Question> listQuestions = new ArrayList<Question>();
        while (res.next()) {
            Question question = new Question((res.getString("texteQuestion")));
            question.setIdBonneReponse(res.getInt("id_bonne_reponse"));
            question.setIdQuestion(res.getInt("id_question"));

            listQuestions.add(question);

        }
        res.close();
        pstmt.close();
        return listQuestions;

    }

    /**
     *
     * @param idQuestion
     * @return les reponses relatives à l'id de la question renseigné
     * @throws SQLException
     */
    public List<Reponse> getReponses(int idQuestion) throws SQLException {
        String requete = "SELECT reponse.ID_REPONSE, reponse.texteREPONSE from propositions, reponse WHERE propositions.ID_REPONSE = reponse.ID_REPONSE and ID_QUESTION=?";
        PreparedStatement pstmt = connect.prepareStatement(requete);
        pstmt.setInt(1, idQuestion);
        ResultSet res = pstmt.executeQuery();
        List<Reponse> listeReponse = new ArrayList<Reponse>();
        while (res.next()) {
            Reponse reponse = new Reponse((res.getString("texteREPONSE")));
            reponse.setIdReponse(res.getInt("ID_REPONSE"));
            listeReponse.add(reponse);

        }
        res.close();
        pstmt.close();
        return listeReponse;
    }

    /**
     * permet de verifier si l'id et le mdo sont bons
     * @param login
     * @param mdp
     * @return l'id du joueur
     * @throws SQLException
     */
    public int login(String login, String mdp) throws SQLException {
        String requete = "SELECT idJOUEUR,login,mdp from joueur WHERE login=? and mdp=?";
        PreparedStatement pstmt = connect.prepareStatement(requete);
        pstmt.setString(1, login);
        pstmt.setString(2, mdp);
        ResultSet res = pstmt.executeQuery();
        List<Reponse> listeReponse = new ArrayList<Reponse>();

        int id = -1;
        while (res.next()) {

            id = res.getInt(1);

        }
        res.close();
        pstmt.close();
        return id;
    }

    /**
     * permet d'ajouter un joueur en bdd
     * @param joueur
     * @return l'id du joueur créé
     * @throws SQLException
     */

    public int addJoueur(Joueur joueur) throws SQLException {
        try {
            String requete = "INSERT INTO joueur (login,mdp) VALUES (?,?)";
            PreparedStatement pstmt = connect.prepareStatement(requete, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, joueur.getLogin());
            pstmt.setString(2, joueur.getMdp());
            pstmt.executeUpdate();
            ResultSet res = pstmt.getGeneratedKeys();
            int id = 0;
            if (res.next()) {
                id = res.getInt(1);

            }
            res.close();
            pstmt.close();
            return id;

        } catch (SQLException se) {
            //Handle errors for JDBC
        } catch (Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * permet de crée une partie
     * @return l'id de la partie créée
     * @throws SQLException
     */
    public int addPartie() throws SQLException {
        String requete = "INSERT INTO partie (PARTIE_FINIE) VALUES (0)";
        PreparedStatement pstmt = connect.prepareStatement(requete, Statement.RETURN_GENERATED_KEYS);
        //pstmt.setInt(1, joueur.getIdJoueur());

        pstmt.executeUpdate();
        ResultSet res = pstmt.getGeneratedKeys();
        int id = 0;
        if (res.next()) {
            id = res.getInt(1);

        }
        res.close();
        pstmt.close();
        return id;

    }


    /**
     * classe la partie comme étant finie
     * @param idPartie
     * @return
     * @throws SQLException
     */

    public int finirPartie(int idPartie) throws SQLException {
        String requete = "UPDATE partie SET PARTIE_FINIE=1 WHERE id_partie=?";
        PreparedStatement pstmt = connect.prepareStatement(requete, Statement.RETURN_GENERATED_KEYS);
        pstmt.setInt(1, idPartie);

        pstmt.executeUpdate();
        ResultSet res = pstmt.getGeneratedKeys();
        int id = 0;
        if (res.next()) {
            id = res.getInt(1);

        }
        res.close();
        pstmt.close();
        return id;
    }

    /**
     * deconnecter tous les joueurs (en bdd)
     * @throws SQLException
     */

    public void deconnecterJoueurs() throws SQLException {
        String requete = "UPDATE joueur SET connecte=0";
        PreparedStatement pstmt = connect.prepareStatement(requete, Statement.RETURN_GENERATED_KEYS);
        pstmt.executeUpdate();
        ResultSet res = pstmt.getGeneratedKeys();
        res.close();
        pstmt.close();

    }

    /**
     * permet de connecter un joueur en bdd
     * @param idJoueur
     * @throws SQLException
     */
    public void connecterJoueur(int idJoueur)throws SQLException{
        String requete = "UPDATE joueur SET connecte=1 WHERE idJOUEUR=?";
        PreparedStatement pstmt = connect.prepareStatement(requete, Statement.RETURN_GENERATED_KEYS);
        pstmt.setInt(1, idJoueur);
        pstmt.executeUpdate();
        ResultSet res = pstmt.getGeneratedKeys();
        res.close();
        pstmt.close();
    }

    /**
     * vérifie si un joueur est connécté ou non grace a son id
     * @param idJoueur
     * @return
     * @throws SQLException
     */
    public boolean isConnecte(int idJoueur) throws SQLException {

        String requete = "SELECT connecte FROM joueur WHERE idJoueur=? and connecte=1";
        PreparedStatement pstmt = connect.prepareStatement(requete);
        pstmt.setInt(1, idJoueur);
        ResultSet res = pstmt.executeQuery();
        while (res.next()) {

            return true;

        }
        res.close();
        pstmt.close();

        return false;
    }

    /**
     * ajouter un joueur a une partie
     * @param idJoueur
     * @param idPartie
     * @return
     * @throws SQLException
     */
    public int addJoueurHasPartie(int idJoueur, int idPartie) throws SQLException {
        String requete = "INSERT INTO joueur_has_partie (id_joueur,id_partie) VALUES (?,?)";
        PreparedStatement pstmt = connect.prepareStatement(requete, Statement.RETURN_GENERATED_KEYS);
        pstmt.setInt(1, idJoueur);
        pstmt.setInt(2,idPartie);
        pstmt.executeUpdate();
        ResultSet res = pstmt.getGeneratedKeys();
        int id = 0;
        if (res.next()) {
            id = res.getInt(1);

        }
        res.close();
        pstmt.close();
        return id;
    }

    /**
     * en fin de partie on set le score de chaque joueur
     * @param joueur
     * @param idPartie
     * @return
     * @throws SQLException
     */
    public int setScoreJoueur(Joueur joueur, int idPartie) throws SQLException {
        String requete = "UPDATE joueur_has_partie SET score=? WHERE id_joueur=? and id_partie=?";
        PreparedStatement pstmt = connect.prepareStatement(requete, Statement.RETURN_GENERATED_KEYS);
        pstmt.setInt(1, (int) joueur.getScore());
        pstmt.setInt(2, joueur.getIdJoueur());
        pstmt.setInt(3, idPartie);
        pstmt.executeUpdate();
        ResultSet res = pstmt.getGeneratedKeys();
        int id = 0;
        if (res.next()) {
            id = res.getInt(1);

        }
        res.close();
        pstmt.close();
        return id;
    }

    /**
     * ajouter les questions faites dans une partie
     * @param idPartie
     * @param question
     * @return
     * @throws SQLException
     */
    public boolean addHasPartie(int idPartie, Question question) throws SQLException {
        String requete = "INSERT INTO question_has_partie (id_question,id_partie) VALUES (?,?)";
        PreparedStatement pstmt = connect.prepareStatement(requete, Statement.RETURN_GENERATED_KEYS);
        pstmt.setInt(2, idPartie);
        pstmt.setInt(1, question.getIdQuestion());
        pstmt.executeUpdate();
        ResultSet res = pstmt.getGeneratedKeys();
        int id = 0;
        boolean b1 = false;
        if (res.next()) {
            b1 = true;
        }
        res.close();
        pstmt.close();
        return b1;
    }

    /**
     * permet d'ajouter une catégorie
     * @param texteCATEGORIE
     * @return
     * @throws SQLException
     */

    public int addCategorie(String texteCATEGORIE) throws SQLException {


        String requete = "INSERT INTO categorie(texteCATEGORIE) values (?)";
        PreparedStatement pstmt = connect.prepareStatement(requete, Statement.RETURN_GENERATED_KEYS);
        pstmt.setString(1, texteCATEGORIE);
        pstmt.executeUpdate();
        ResultSet res = pstmt.getGeneratedKeys();
        //Categorie categorie = new Categorie();
        int idCategorie = -1;
        while (res.next()) {
            idCategorie = (res.getInt(1));
        }
        res.close();
        pstmt.close();
        return idCategorie;
    }

    /**
     * permet d'ajouter une réponse
     * @param texteREPONSE
     * @return
     * @throws SQLException
     */
    public int addReponse(String texteREPONSE) throws SQLException {
        String requete = "INSERT INTO reponse(texteREPONSE) values (?)";
        PreparedStatement pstmt = connect.prepareStatement(requete, Statement.RETURN_GENERATED_KEYS);
        pstmt.setString(1, texteREPONSE);

        pstmt.executeUpdate(); //update pour un insert
        ResultSet res = pstmt.getGeneratedKeys();
        int idReponse = -1;
        while (res.next()) {
            idReponse = (res.getInt(1));
        }
        res.close();
        pstmt.close();
        //System.out.println("id de reponse : " + idReponse);
        return idReponse;
    }

    // à partir de son texte, d’un objet bonneReponse de la classe Reponse,
    // d’un objet Catégorie de la classe Categorie
    public int addQuestion(String texteQUESTION, Reponse reponse, Categorie categorie) throws SQLException {
        String requete = "INSERT INTO question(id_bonne_reponse,id_categorie,texteQUESTION) values (?,?,?)";
        PreparedStatement pstmt = connect.prepareStatement(requete, Statement.RETURN_GENERATED_KEYS);
        pstmt.setInt(1, reponse.getIdReponse());
        pstmt.setInt(2, categorie.getIdCategorie());
        pstmt.setString(3, texteQUESTION);
        pstmt.executeUpdate();
        ResultSet res = pstmt.getGeneratedKeys();
        int id = -1;
        if (res.next()) {
            id = res.getInt(1); //id question
        }
        res.close();
        pstmt.close();
        return id;
    }


    // Créer une méthode addProposition qui permet d’ajouter une proposition de réponse à une question
    // à partir d’un objet Question et d’un objet Reponse
    private boolean addProposition(Question question, Reponse reponse) throws SQLException {
        String requete = "INSERT INTO propositions(id_question,id_reponse) values (?,?)";
        PreparedStatement pstmt = connect.prepareStatement(requete, Statement.RETURN_GENERATED_KEYS);
        pstmt.setInt(1, question.getIdQuestion());
        pstmt.setInt(2, reponse.getIdReponse());
        pstmt.executeUpdate();
        ResultSet res = pstmt.getGeneratedKeys();
        boolean bool = false;
        if (res.next()) {
            bool = true;
        }
        res.close();
        pstmt.close();
        return bool;
    }

    /**
     * permet de verifier si une categorie est deja presente ou non
     * @param categorie
     * @return
     */
    private boolean categorieEstPresente(Categorie categorie){
        try {
            for(Categorie categorie1 : getListeCategories()){
                if(categorie.getTexteCategorie().equals(categorie1.getTexteCategorie())) {
                    return true;
                }
            }
        } catch (SQLException throwables) {

        }
        return false;
    }
    /**
     * permet a partir d'un nom de fichier de l'importer en bdd
     * @param nomDuFichier
     */
    public void importJson(String nomDuFichier) {

        JSONObject o, o2;
        Categorie categorie;

        JSONParser jsonP2 = new JSONParser();
        try {

            JSONObject json2 = (JSONObject) jsonP2.parse(new FileReader(nomDuFichier));

            JSONObject tabQuizz = (JSONObject) json2.get("quizz");
            JSONObject tabFr1 = (JSONObject) tabQuizz.get("fr");
            JSONArray tabFrDebutant = (JSONArray) tabFr1.get("débutant");
            categorie = new Categorie((String) json2.get("thème"));
            RequeteKahoot requeteKahoot = new RequeteKahoot();
            // on vérifie que la catégorie n'est pas déja présente pour eviter les erreurs sql
            if(!categorieEstPresente(categorie)){
                // on ajoute la catégorie
                int idcategorie = requeteKahoot.addCategorie(categorie.getTexteCategorie());
                categorie.setIdCategorie(idcategorie);
                // Lecture des questions en français et niveau débutant
                Iterator iterator = tabFrDebutant.iterator();
                while (iterator.hasNext()) {

                    o = (JSONObject) iterator.next();
                    Question q = new Question((String) o.get("question"));

                    JSONArray tabPropositions = (JSONArray) o.get("propositions");
                    Reponse r = new Reponse((String) o.get("réponse"));
                    int idReponse = requeteKahoot.addReponse(r.getTexteOption());
                    r.setIdReponse(idReponse);
                    int idQuestion = requeteKahoot.addQuestion(q.getTexteOption(), r, categorie);
                    q.setIdQuestion(idQuestion);

                    for (int i = 0; i < tabPropositions.size(); i++) {
                        String s1 = (String) tabPropositions.get(i);
                        String s2 = r.getTexteOption();
                        if (s1.equals(s2)) /* Cette réponse est la bonne réponse r créée ci-dessus */
                            requeteKahoot.addProposition(q, r);
                        else {
                            Reponse p = new Reponse(s1);
                            int idP = requeteKahoot.addReponse(p.getTexteOption());
                            p.setIdReponse(idP);
                            requeteKahoot.addProposition(q, p);
                        }

                    }

                }

            }

            //Initialisation de la liste des questions et réponses

        } catch (
                IOException | ParseException | SQLException e) {
            e.printStackTrace();
        }
    }

}
