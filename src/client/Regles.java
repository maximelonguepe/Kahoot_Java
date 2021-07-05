package client;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * affichage des regles
 */
public class Regles extends JDialog {
    private JPanel contentPane;

    private JTextPane texteRegle;
    private JButton creerUnCompteButton;
    private JButton seConnecterButton;
    private JButton CreerUnCompteButton;

    private void pageConnexion(){
        this.setSize(0,0);
        ConnexionClient.main("connexion jeu");
    }


    private void pageCreationCompte(){
        this.setSize(0,0);
        CreationCompte.main();
    }

    public Regles() {
        setContentPane(contentPane);
        seConnecterButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                pageConnexion();
            }
        });
        creerUnCompteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                pageCreationCompte();
            }
        });

        setModal(true);
        texteRegle.setEditable(false);
        texteRegle.setText("Règles kahoot \n " +
                "Voici les règles du kahoot : \n" +
                "- les joueurs se connectent via leur login et mdp Attention un utilisateur ne peut se connecter qu'une seule fois sur une meme partie\n" +
                "- Il faut attendre 60 secondes après que le 2ème joueur se soit connecté pour acceder a l'interface de choix de catégorie\n"+
                "- Une liste de catégorie est affichée il faut attendre que tout le monde ait répondu pour pouvoir passer aux questions\n" +
                "- La catégorie est suivie de 10 questions auxquelles les joueurs doivent répondre via un des 4 boutons numérotés dans l'ordre des questions\n" +
                "- Le joueur a 10 secondes par question. Attention : le score est proportionnel au temps que le joueur met pour répondre\n" +
                "- A la fin des 10 questions le score est affiché sur l'écran de chaque joueur\n" +
                "");

    }

    public static void main() {
        Regles dialog = new Regles();
        dialog.pack();
        dialog.setVisible(true);


        System.exit(0);
    }


}
