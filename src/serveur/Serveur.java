package serveur;

import client.Message;
import org.joda.time.LocalDateTime;
import pootp1.*;

import java.io.IOException;
import java.net.ServerSocket;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.*;

public class Serveur extends Thread {

    private static final int port = 60000;
    private ServerSocket serverSocket;
    private static List<Connexion> listConnexion;
    private static Joueur joueur;
    private static boolean partieCommencee = false;
    private LocalDateTime dateDebutAttente = null;
    private Message message = null;
    private static List<Joueur> joueurList = new ArrayList<Joueur>();
    private static final int nbMaxJoueurs = 2;
    boolean passerQuestionSuivante;
    private static boolean timerQuestion = false;
    private static LocalDateTime dateDebutQuestion;
    private static final int nombreSecondesQuestion = 15;
    private static int bonneReponse;
    private static boolean timerPartie = false;
    private static Timer timer;
    private static List<Connexion> connexionAttente;


    /**
     * Le serveur a en attribut afin d'envoyer des messages à tous les clients via la classe Connexion
     *
     * @return le joueur associé au serveur
     */
    public static Joueur getJoueur() {
        return joueur;
    }

    /**
     * seter du joueur
     *
     * @param joueur
     */
    public static void setJoueur(Joueur joueur) {
        Serveur.joueur = joueur;
    }

    /**
     * Constructeur du serveur
     * on associe au serveur son numéro de port via la classe ServerSocket.
     * On instancie le joueur associé au serveur
     *
     * @throws IOException
     */
    public Serveur() throws IOException {
        this.serverSocket = new ServerSocket(port);
        listConnexion = new ArrayList<>();
        joueur = new Joueur("serveur", "serveur");
        joueur.setIdJoueur(0);
    }

    /**
     * @return un booleen si la partie est commencée ou non.
     */

    public static boolean isPartieCommencee() {
        return partieCommencee;
    }

    /**
     * setter de partieCommencee
     *
     * @param partieCommencee
     */
    public static void setPartieCommencee(boolean partieCommencee) {
        Serveur.partieCommencee = partieCommencee;
    }

    /**
     * Fermeture du socket d'écoute du serveur.
     */

    private void fermerSocketEcoute() {
        try {
            serverSocket.close();
        } catch (IOException e) {
            System.err.println("Erreur lors de la fermeture de socket d'écoute");
            e.printStackTrace();
        }
    }

    /**
     * @return la liste des connexions
     */
    public static synchronized List<Connexion> getListConnexion() {
        return listConnexion;
    }


    /**
     * Fonction run du thread
     * C'est ici qu'on lance les differents timer permettant de lancer la partie ou de vérifier si un joueur a repondu.
     * On attend aussi la connexion des joueurs
     */
    @Override
    public void run() {
        try {

            RequeteKahoot requeteKahoot = new RequeteKahoot();//permet d'éxecuter les requetes sql
            requeteKahoot.deconnecterJoueurs();//permet de deconnecter tous les joueurs afin de pouvoir se connecter au lancement d'une nouvelle partie
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        System.out.println("Lancement du serveur");
        try {
            while (true) {
                // si le nombre des joueurs et insuffisant et que la partie n'est pas commencée alors la partie est lancée
                if (Serveur.getJoueurList().size() >= 1 && (!partieCommencee)) {

                    // si il n'y a pas de timer d'attente de joueur créé alors on en crée un : il dure 60 secondes
                    // on a ainsi un thread qui va attendre les joueurs et qui va se relancer toutes les 60 secondes.
                    if (!timerPartie) {
                        timer = new Timer();
                        //permet de lancer le timer
                        timer.schedule(new Relanceur(), 30000, 30000);
                        //permet de mettre de lancer une unique fois le timer
                        timerPartie = true;

                    }


                }
                // phase bloquante durant laquelle on attend une connexion de la part du client
                Connexion con = new Connexion(serverSocket.accept());
                synchronized (listConnexion) {
                    // si la partie n'est pas commencée alors on accepte des nouvelles connexion en les ajoutant a la liste des connexions sinon elles sont mise en attente
                    if (!partieCommencee) {
                        listConnexion.add(con);//ajout de la connexion
                        System.out.println("Connexion bien prise en compte");
                        con.start();//démarrage du thread de la connexion
                        // affichage sur l'écran des joueurs le nombre de joueurs connecté a chaque nouveau joueur connecté
                        Connexion.distribuerMessage(new Message(joueur, "Nouvel utilisateur connecte vous êtes " + Serveur.getListConnexion().size() + " joueurs connectés"));

                    } else {
                        connexionAttente.add(con);//connexion mise en attente
                        System.out.println("Connexion mise en attente");

                    }


                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            fermerSocketEcoute();
        }
    }

    /**
     * main : instanciation du serveur et lancement de son thread
     *
     * @param args
     * @throws IOException
     */

    public static void main(String[] args) throws IOException {
        Serveur serv = new Serveur();
        serv.start();

    }

    /**
     * @return la liste des joueurs connectés
     */
    public static List<Joueur> getJoueurList() {
        return joueurList;
    }

    public static void setJoueurList(List<Joueur> joueurList) {
        Serveur.joueurList = joueurList;
    }

    /**
     * permet d'ajouter un joueur a la liste
     *
     * @param joueur le joueur a ajouter
     */

    public static void ajoutListe(Joueur joueur) {

        joueurList.add(joueur);
    }

    /**
     * retourne le temps qu'a un joueur pour répondre a une question.
     * Par défaut il est défini à 15 secondes
     *
     * @return
     */

    public static int getNombreSecondesQuestion() {
        return nombreSecondesQuestion;
    }

    /**
     * A partir du timer de 60 secondes la fonction commencerPartie est lancée.
     *
     * @throws SQLException
     * @throws InterruptedException
     */
    public static void commencerPartie() throws SQLException, InterruptedException {
        // permet d'afficher sur l'écran de tous les joueurs que la partie a commencé
        Connexion.distribuerMessage(new Message(joueur, "Partie commencée"));
        //permet d'executer des requetes sql
        RequeteKahoot requeteKahoot = new RequeteKahoot();
        //on créé une nouvelle partie et on stock son id dans idPartie
        int idPartie = requeteKahoot.addPartie();
        //On ajoute les joueurs dans la table joueur_has_partie
        //cela permet nottament de stocker leur score en fin de partie
        for (Joueur joueur : joueurList) {
            requeteKahoot.addJoueurHasPartie(joueur.getIdJoueur(), idPartie);
        }
        // on liste les categories
        List<Categorie> categorieList = requeteKahoot.getListeCategories();
        //il faut que la liste des cotégories soit supérieure ou egale a 4
        //
        if (categorieList.size() >= 4) {
            //on mélange les catégories afin de ne pas toujours avoir les memes
            Collections.shuffle(categorieList);
            //on envoie un message aux joueurs.
            Connexion.distribuerMessage(new Message(joueur, "Veuillez sélectionner la catégorie que vous souhaitez : "));
            System.out.println(joueurList);

            //affichage des differents categorie
            for (int i = 0; i < 4; i++) {
                // on met un int entre 1 et 4 pour que l'utilisateur puisse choisir la catégorie quil souhaite
                Connexion.distribuerMessage(new Message(joueur, Integer.toString(i + 1) + " - " + categorieList.get(i).getTexteCategorie()));
            }
            //tant que tous les joueurs n'ont pas répondu alors on ne passe pas a la suite
            while (reponseUnanime() != joueurList.size()) {
            }

            System.out.println("reponse unanime :" + reponseUnanime());


            // cela correspond à l'index dans le tableau des catégorie de la catégorie choisie par les joueurs
            int posTableau = positionMaxReponse();
            //on instancie la catégorie choisie
            Categorie categorieChoisie = categorieList.get(Integer.parseInt(String.valueOf(posTableau)));
            System.out.println(categorieChoisie);
            // cela permet de vider l'écran du joueur
            Connexion.distribuerMessage(new Message(joueur, "nettoyer ecran"));
            //on affiche aux joueurs la catégorie choisie
            Connexion.distribuerMessage(new Message(joueur, "Catégorie choisie : " + categorieChoisie.getTexteCategorie()));
            // on récupere les questions liées à la catégorie
            List<Question> questionList = requeteKahoot.getQuestions(categorieChoisie.getIdCategorie());
            // on met a null les reponses de tous les joueurs.
            setNullJoueurs();
            // on mélange les questions
            Collections.shuffle(questionList);


            for (Question question : questionList) {
                // PG : progress bar : permet d'incrémenter la premiere progress bar sur le nombre de question
                Connexion.distribuerMessage(new Message(joueur, "PG"));
                //permet de réinitialiser la seconde barre
                Connexion.distribuerMessage(new Message(joueur, "PG1:0"));
                //on set tous les joueurs comme quoi ils n'ont pas répondu (permet d'éviter le multi-appui)
                setAReponduFalse();
                //on nettoi l'écran
                Connexion.distribuerMessage(new Message(joueur, "nettoyer ecran"));
                // on ajoute la question en bdd
                requeteKahoot.addHasPartie(idPartie, question);
                //on affiche le texte de la question
                Connexion.distribuerMessage(new Message(joueur, question.getTexteOption()));
                //on récupere les reponses relatives à la question
                List<Reponse> reponseList = requeteKahoot.getReponses(question.getIdQuestion());
                // on instancie une date avant chaque question pour permettre de calculer les points
                dateDebutQuestion = LocalDateTime.now();
                //on mélange les reponses
                Collections.shuffle(reponseList);
                //on récupere l'index de la bonne reponse
                bonneReponse = indiceBonneReponse(question, reponseList);

                // on affiche les reponses
                for (int i = 0; i < reponseList.size(); i++) {
                    Connexion.distribuerMessage(new Message(joueur, Integer.toString(i + 1) + " - " + reponseList.get(i)));
                }
                // On lance un timer qui va a la fois actualiser la deuxieme barre (niveau temps restant)
                // Mais aussi vérifier toutes les secondes si les joueurs ont répondu ou non
                // on le lance une unique fois
                if (!timerQuestion) {
                    Timer timer1;
                    timer1 = new Timer();
                    // on lance le timer
                    timer1.schedule(new RelanceurQuestion(), 1000, 1000);
                    timerQuestion = true;
                }

                //Phase d'attente : par défaut 15 secondes
                //On divise le temps en 100 temps d'attente
                //si tous les joueurs ont repondu alors on passe a la question suivante

                int i = 0;
                while (i < 100 && !aReponduQuestion()) {

                    Thread.sleep(nombreSecondesQuestion * 10);
                    i++;

                }

                Connexion.distribuerMessage(new Message(joueur, "nettoyer ecran"));
                // on affiche et laisse affiché la bonne réponse pendant 3 secondes
                Connexion.distribuerMessage(new Message(joueur, "La bonne réponse était " + reponseList.get(bonneReponse)));
                Thread.sleep(3000);

            }
            //Phase de fin de partie
            // on trie les joueurs selon leurs score et on les affiche
            // on peut les trier grace a compareTo
            Collections.sort(joueurList);
            for (Joueur joueur1 : joueurList) {
                Connexion.distribuerMessage(new Message(joueur, joueur1.getLogin() + " " + joueur1.getScore()));
            }
            // on attend 5 secondes
            Thread.sleep(5000);
            //on nettoie lecran
            Connexion.distribuerMessage(new Message(joueur, "nettoyer ecran"));
            partieCommencee = false;


            timer.cancel();
            timer.purge();
            timerPartie = false;
            // on met a jour le score de tous les joueurs dans la bdd
            for (Joueur joueur1 : joueurList) {
                requeteKahoot.setScoreJoueur(joueur1, idPartie);
            }
            // on termine la partie.
            requeteKahoot.finirPartie(idPartie);
        }


    }

    private static boolean aReponduQuestion() {
        for (Joueur joueur1 : joueurList) {
            if (!joueur1.isaRepondu()) {
                return false;
            }
        }
        return true;
    }

    /**
     * retourne l'indice dans le tableau de la bonne reponse
     *
     * @return
     */
    public static int getBonneReponse() {
        return bonneReponse;
    }

    /**
     * @return la date de début de la question
     */
    public static LocalDateTime getDateDebutQuestion() {
        return dateDebutQuestion;
    }

    /**
     * @return l'indice du maximum dans le tableau
     */
    public static int positionMaxReponse() {
        int max = 0;
        int j = 0;
        List<Integer> list = nombreReponse();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) > max) {
                max = list.get(i);
                j = i;
            }
        }
        return j;

    }

    /**
     * @return le nombre de joueurs ayant une réponse non nulle.
     */
    public static int reponseUnanime() {
        int i = 0;
        for (Joueur joueur : joueurList) {
            if (joueur.getReponse() != null) {
                i++;
            }
        }
        return i;
    }

    /**
     * met la reponse de tous les joueurs a null
     */
    public static void setNullJoueurs() {
        for (Joueur joueur : joueurList) {
            if (joueur.getReponse() != null) {
                joueur.setReponse(null);
            }
        }

    }

    /**
     * permet de lister toutes les reponses qu'ont entré les joueurs
     *
     * @return
     */
    public static List<String> listerReponses() {
        List<String> listeReponses = new ArrayList<String>();
        for (Joueur joueur : joueurList) {
            listeReponses.add(joueur.getReponse());
        }
        return listeReponses;
    }

    /**
     * tableau d'int avec le nombre de personne ayant répondu en valeur et en indice l'equivalent en reponse
     *
     * @return
     */
    public static List<Integer> nombreReponse() {
        List<Integer> listeInt = new ArrayList<Integer>();
        int i1 = 0;
        int i2 = 0;
        int i3 = 0;
        int i4 = 0;

        for (String s : listerReponses()) {
            if (s.equals("1")) {
                i1++;
            }
            if (s.equals("2")) {
                i2++;
            }
            if (s.equals("3")) {
                i3++;
            }
            if (s.equals("4")) {
                i4++;
            }
        }
        listeInt.add(i1);
        listeInt.add(i2);
        listeInt.add(i3);
        listeInt.add(i4);
        return listeInt;
    }

    /**
     * retourne l'id de la bonne réponse parmi les réponses
     *
     * @param question
     * @param list
     * @return
     */
    private static int indiceBonneReponse(Question question, List<Reponse> list) {

        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getIdReponse() == question.getIdBonneReponse()) {
                return i;
            }
        }
        return -1;
    }

    /**
     * permet de set l'attribut de tous les joueurs connéctés a false.
     */
    private static void setAReponduFalse() {
        for (Joueur joueur : joueurList) {
            joueur.setaRepondu(false);
        }
    }


}
