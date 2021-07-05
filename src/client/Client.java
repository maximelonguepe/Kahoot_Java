package client;

import pootp1.Joueur;
import serveur.Serveur;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.Socket;

public class Client extends JDialog {
    private static final String host = "192.168.1.9";
    private static final int port = 60000;
    private Connexion connexion;
    private Ecouteur ecouteur;
    private JPanel contentPane;
    private JButton envoi;
    private JTextArea zoneQuestion;
    private JButton button1;
    private JButton a2Button;
    private JButton a3Button;
    private JButton a4Button;
    private JProgressBar progressBar;
    private JProgressBar tempsRestant;
    private Joueur joueur;
    private int idConnexion;
    private static int connexionEnCours=0;

    /**
     * On associe un joueur au client
     * @return le joueur
     */
    public Joueur getJoueur() {
        return joueur;
    }

    /**
     * permet de set le joueur
     * @param joueur
     */
    public void setJoueur(Joueur joueur) {
        this.joueur = joueur;
    }

    /**
     * constructeur de la classe Client
     * @param joueur le joueur associé au Client celui ci est instancié lors de la phase de connexion dans la classe ConnexionClient
     */
    public Client(Joueur joueur) {
        //on set les progress bar
        progressBar.setMinimum(0);
        progressBar.setMaximum(10);
        tempsRestant.setMinimum(0);
        // on set le nombre de secondes maximum pour la progress bar
        // le nombre max par question est instancié dans la classe Serveur
        tempsRestant.setMaximum(Serveur.getNombreSecondesQuestion());

        this.joueur=joueur;
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(envoi);
        // on empeche le joueur d'écrire dans la zone de texte
        zoneQuestion.setEditable(false);
        try {
            // on instancie une nouvelle connexion au serveur
            connexion=new Connexion(new Socket(host, port));
            // on instancie l'écouteur avec les differentes zones que l'on veut voir rempli par l'écouteur
            ecouteur=new Ecouteur(zoneQuestion,connexion,progressBar,tempsRestant);
            //on démarre le thread de l'ecouteur
            ecouteur.start();
            //on demande une conenxion au serveur
            // le cas est géré dans la classe Connexion du pacckage serveur
            connexion.getOos().writeObject(new Message(joueur,"Demande connexion partie"));
            connexion.getOos().flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        /**
         * ici on définit ce qu'il se passe quand on clique sur les differents boutons du swing
         */
        button1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onEnvoie1();
            }
        });        a2Button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onEnvoie2();
            }
        });        a3Button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onEnvoie3();
            }
        });        a4Button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onEnvoie4();
            }
        });
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });
    }

    /**
     * c'est le "main" du client il est lancé via la classe ConnexionClient
     * @param joueur
     */

    public static void main(Joueur joueur) {
        Client dialog = new Client(joueur);
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }

    public JProgressBar getProgressBar() {
        return progressBar;
    }



    private void onCancel() {
        // add your code here if necessary

        connexion.close();
        dispose();

    }

    /**
     * On envoie un message 1 si l'utilisateur a clique sur 1 etc...
     * Cas géré dans la classe Connexion du package serveur
     */

    private void onEnvoie1() {


        try {
            connexion.getOos().writeObject(new Message(joueur,"1"));
            connexion.getOos().flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    private void onEnvoie2() {


        try {
            connexion.getOos().writeObject(new Message(joueur,"2"));
            connexion.getOos().flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void onEnvoie3() {


        try {
            connexion.getOos().writeObject(new Message(joueur,"3"));
            connexion.getOos().flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void onEnvoie4() {


        try {
            connexion.getOos().writeObject(new Message(joueur,"4"));
            connexion.getOos().flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
