package client;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Interface utilisateur "mère elle permet de diriger vers les autres interfaces"
 */
public class ChoixCreationOuConnexion extends JDialog {
    private JPanel contentPane;
    private JButton creerCompte;
    private JButton connexionButton;
    private JButton regles;
    private JButton buttonAdmin;


    public ChoixCreationOuConnexion() {
        setContentPane(contentPane);
        setModal(true);
        //setSize(500,500);
        connexionButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                pageConnexion();
            }
        });
        creerCompte.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                pageCreationCompte();
            }
        });

        regles.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                pageRegles();
            }
        });

        buttonAdmin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                pageAdminConnexion();
            }
        });
    }


    public static void main(String[] args) {
        ChoixCreationOuConnexion dialog = new ChoixCreationOuConnexion();
        dialog.pack();
        dialog.setVisible(true);
        //dialog.setSize(500,500);
        System.exit(0);
    }

    /**
     * ici on fait la distinction d'une connexion jeu et d'une connexion admin : la connexion jeu debouche sur le jeu (Client) alors qu'une connexion admin découle vers Admin
     */
    private void pageConnexion(){
        this.setSize(0,0);
        ConnexionClient.main("connexion jeu");
    }

    private void pageAdminConnexion(){
        this.setSize(0,0);
        ConnexionClient.main("connexion admin");
    }
    //elle découle vers la page de création de compte qui découle vers la connexion.
    private void pageCreationCompte(){
        this.setSize(0,0);
        CreationCompte.main();
    }
    // page de regles
    private void pageRegles(){
        this.setSize(0,0);
        Regles.main();
    }
}
