package client;

import pootp1.Joueur;
import pootp1.RequeteKahoot;
import serveur.Serveur;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * permet de se connecter a l'interface de jeu ou a l'interface admin
 */

public class ConnexionClient extends JDialog {
    private JPanel contentPane;
    private JButton connexion;
    private JTextField login;
    private JPasswordField mdp;
    private JTextArea zoneErreur;
    private Joueur joueur1;
    private String choix;

    public ConnexionClient(String choix) {

        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(connexion);
        zoneErreur.setEditable(false);
        connexion.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                connexion();
            }
        });
        this.choix = choix;

    }

    public static void main(String choix) {
        ConnexionClient dialog = new ConnexionClient(choix);
        dialog.pack();
        dialog.setVisible(true);

        System.exit(0);
    }

    private void connexion() {

        RequeteKahoot requeteKahoot = null;
        try {
            requeteKahoot = new RequeteKahoot();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        try {
            //on teste si les logs sont bons
            int test = requeteKahoot.login(login.getText(), new String(mdp.getPassword()));
            //si les logs sont bons
            if (test != -1) {
                // on instancie un joueur
                Joueur joueur = new Joueur(login.getText(), new String(mdp.getPassword()));
                joueur.setIdJoueur(test);
                joueur1 = joueur;
                //si l'utilisateur n'est pas déja connecté
                if (!requeteKahoot.isConnecte(test)) {
                    this.setSize(0, 0);
                    // si le choix est de se connecter au jeu alors on dirige vers client sinon on dirige vers Admin
                    if (choix.equals("connexion jeu")) Client.main(joueur);
                    if (choix.equals("connexion admin")) Admin.main();
                } else zoneErreur.setText("Erreur joueur déja connecté");


            } else {
                zoneErreur.setText("Erreur mot de passe ou login incorrect");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    @Override
    public String toString() {
        return "ConnexionClient{" +
                "joueur1=" + joueur1 +
                '}';
    }
}
