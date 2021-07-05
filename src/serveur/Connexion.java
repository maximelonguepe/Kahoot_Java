package serveur;

import client.Message;
import pootp1.Joueur;
import pootp1.RequeteKahoot;

import java.io.*;
import java.net.Socket;
import java.sql.SQLException;
import java.util.List;


public class Connexion extends Thread {

    Socket socket;

    private ObjectInputStream ois;
    private ObjectOutputStream oos;

    public ObjectInputStream getOis() {
        return ois;
    }
    // c'est ici ou on recoit les message
    public ObjectOutputStream getOos() {
        return oos;
    }



    @Override
    public void run() {
        while (true) {
            try {
                Object line = ois.readObject();
                if (line != null) {
                    //System.out.println((Message) line);
                    //si c'est un message provenant du serveur alors il envoie le message a tous les clients
                    if (((Message) line).getExpediteur().getLogin().equals("serveur")) {

                        distribuerMessage(line);


                    }
                    //si c'est un message partant d'un client
                    else {
                        // si il y a une demande de conenexion de la part d'un joueur.
                        if (((Message) line).getMessage().equals("Demande connexion partie")) {
                            // on ajoute le joueur a la liste du serveur
                            Serveur.ajoutListe(((Message) line).getExpediteur());
                            RequeteKahoot requeteKahoot=new RequeteKahoot();
                            // on passe l'utilisateur comme connecté pour qu'il ne puisse pas se connecter deux fois
                            requeteKahoot.connecterJoueur(((Message) line).getExpediteur().getIdJoueur());
                            // si c'est un message venant d'un joueur n'ayant pas répondu alors on le prend en compte
                        } else if ((((Message) line).getMessage().equals("1") || ((Message) line).getMessage().equals("2") || ((Message) line).getMessage().equals("3") || ((Message) line).getMessage().equals("4"))&&!((Message) line).getExpediteur().isaRepondu()) {
                            int indiceTableau=equivalentTableau(((Message) line).getExpediteur());
                            //on ajoute le message a l'utilisateur
                            Serveur.getJoueurList().get(indiceTableau).setReponse(((Message) line).getMessage());
                            // on set a repondu a true
                            Serveur.getJoueurList().get(indiceTableau).setaRepondu(true);

                            System.out.println(Serveur.getJoueurList());
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    public static int equivalentTableau(Joueur joueur) {

        for (int i = 0; i < Serveur.getJoueurList().size(); i++) {
            if (Serveur.getJoueurList().get(i).equals(joueur)) {
                return i;
            }
        }
        return -1;

    }

    /**
     * permet d'envoyer un message a toutes les connexions.
     * @param message
     */
    public static synchronized void distribuerMessage(Object message) {
        synchronized (Serveur.getListConnexion()) {
            List<Connexion> liste = Serveur.getListConnexion();
            for (Connexion con : liste) {
                try {
                    con.getOos().writeObject(message);
                    con.getOos().flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }



    public Connexion(Socket socket) throws IOException {
        this.socket = socket;
        OutputStream output = socket.getOutputStream();
        oos = new ObjectOutputStream(output);
        InputStream is = socket.getInputStream();
        ois = new ObjectInputStream(is);

    }

    public void close() {
        try {
            ois.close();
            oos.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

