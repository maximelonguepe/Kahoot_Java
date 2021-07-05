package client;

import pootp1.TestBDD;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

/**
 * cette classe est la "page d'accueil de la partie administration du kahoot"
 */
public class Admin extends JDialog {
    private JPanel contentPane;
    private JButton buttonAjoutJson;
    private JButton majBDD;
    private JButton buttonOK;

    public Admin() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonAjoutJson);
        buttonAjoutJson.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                pageAjoutJson();
            }
        });
        majBDD.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                majBDD();
            }
        });
    }

    public static void main() {
        Admin dialog = new Admin();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }

    /**
     * redirige vers la page permettant l'ajout d'un json
     */
    private void pageAjoutJson(){
        this.setSize(0,0);
        AjoutJson.main();
    }

    /**
     * permet de lancer la mise a jour de la bdd en fonction des fichiers presents dans le répertoire courant.
     */
    private void majBDD(){
        try {
            TestBDD.main();
        } catch (SQLException throwables) {

        }
    }
}
