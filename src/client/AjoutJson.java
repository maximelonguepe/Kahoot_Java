package client;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * cette classe permet l'ajout d'un fichier json au répertoire courant.
 * On peut y accéder depuis la page Admin de la classe Admin
 */
public class AjoutJson extends JDialog {
    private JPanel contentPane;
    private JTextField emplacement;
    private JButton ajouter;
    private JTextArea zoneErreur;


    public AjoutJson() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(ajouter);
        zoneErreur.setEditable(false);
        ajouter.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mv();
            }
        });
    }

    public static void main() {
        AjoutJson dialog = new AjoutJson();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }

    /**
     * méthode qui permet de modifier l'emplacement d'un fichier et de le mettre dans le répertoire courant.
     * si cela n'est pas possible ecrit un message d'erreur
     */
    private void mv() {
        File fichier = new File(emplacement.getText());
        File rep = new File("./");
        if(!fichier.renameTo(new File(rep, fichier.getName()))){
            zoneErreur.setText("Erreur lors du déplacement du fichier");
        }

    }
}
