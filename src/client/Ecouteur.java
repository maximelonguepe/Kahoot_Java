package client;

import serveur.Serveur;

import javax.swing.*;
import java.io.IOException;

/**
 * Cet écouteur va permettre d'écrire dans les différentes zones de notre classe client
 * Il est déclaré dans la classe Client et est en permanence en attente d'un message de la classe Connexion du package serveur
 */

public class Ecouteur extends Thread {

    private JTextArea zoneMessage;
    private Connexion connexion;
    private JProgressBar progressBar;
    private JProgressBar progressBar1;

    /**
     * constructeur par données cela va permettre d'écrire dans différentes zones de l'interface swing par la suite
     *
     * @param zoneMessage
     * @param connexion
     * @param progressBar
     * @param progressBar1
     */
    public Ecouteur(JTextArea zoneMessage, Connexion connexion, JProgressBar progressBar, JProgressBar progressBar1) {
        this.zoneMessage = zoneMessage;
        this.connexion = connexion;
        this.progressBar = progressBar;
        this.progressBar1 = progressBar1;
    }

    @Override
    public void run() {
        while (!this.currentThread().isInterrupted()) {
            try {//phase bloquante
                Message msg = (Message) connexion.getOis().readObject();
                if (msg != null) {
                    //si on doit nettoyer l'écran alors la zone de texte est set comme vide.
                    if (msg.getMessage().equals("nettoyer ecran")) {
                        zoneMessage.setText("");
                    } else if (msg.getMessage().equals("PG")) {
                        // si le message concerne une augmentation de la progress bar des questions on l'incrémente
                        incrementerProgressBar();
                    } else if (msg.getMessage().equals("PG1")) {
                        //si le message concerne la progress bar de temps on l'incrémente
                        incrementerProgressBar1();
                    } else if (msg.getMessage().equals("PG1:0")) {
                        // si le message concerne la progress bar de temps concernant sa remise a 0 on la set a 0
                        reinitTemps();
                    } else {
                        // sinon on affiche juste le message
                        zoneMessage.append(msg.getMessage() + "\n");
                    }
                }

            } catch (java.net.SocketException e) {
                this.interrupt();
                e.printStackTrace();
            } catch (IOException e) {
                this.interrupt();
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        this.interrupt();
    }

    /**
     * permet d'incrémenter la barre de progression des questions
     */
    private void incrementerProgressBar() {
        progressBar.setValue(progressBar.getValue() + 1);


    }

    /**
     * permet d'incrémenter la barre de progression du temps
     */
    private void incrementerProgressBar1() {
        progressBar1.setValue(progressBar1.getValue() + 1);
    }

    /**
     * permet de reinitialiser le temps restant pour une question.
     */
    private void reinitTemps() {
        progressBar1.setValue(0);
    }
}
