package client;

import pootp1.Joueur;
import pootp1.RequeteKahoot;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class CreationCompte extends JDialog {
    private JPanel contentPane;
    private JButton creerCompte;
    private JTextField login;
    private JPasswordField mdp;
    private JTextArea zoneErreur;

    public CreationCompte() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(creerCompte);
        creerCompte.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                creerCompte();
            }
        });
        zoneErreur.setEditable(false);
    }

    public static void main() {
        CreationCompte dialog = new CreationCompte();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }

    private void creerCompte()  {
        RequeteKahoot requeteKahoot= null;
        try {
            requeteKahoot = new RequeteKahoot();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        Joueur joueur=new Joueur(login.getText(),new String(mdp.getPassword()));
        try {
            // on essaie d'ajouter en base un nouveau joueur
            int test=requeteKahoot.addJoueur(joueur);
            // si ca réussi
            if (test!=-1){
                this.setSize(0,0);
                // on se connecte de base au jeu
                ConnexionClient.main("connexion jeu");
            }else {
                //sinon on affiche une erreur a l'utilisateur
                zoneErreur.setText("Erreur login déjà utilisé");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }
}
