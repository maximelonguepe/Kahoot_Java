package client;

import pootp1.Joueur;

import java.io.Serializable;

/**
 * Cette classe est importante : elle va transporter nos informations
 * D'ou implémentation de l'interfance Serializable qui va nous permettre de faire transiter notre message entre le serveur et le client
 *
 */
public class Message implements Serializable {
    /**
     * nous avons un expéditeur et un message
     */
    private Joueur expediteur;
    private String message;

    public Joueur getExpediteur() {
        return expediteur;
    }

    public void setExpediteur(Joueur expediteur) {
        this.expediteur = expediteur;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * constructeur de Message
     * @param expediteur
     * @param message
     */

    public Message(Joueur expediteur, String message) {
        this.expediteur = expediteur;
        this.message = message;
    }

    @Override
    public String toString() {
        return "Message{" +
                "expediteur=" + expediteur +
                ", message='" + message + '\'' +
                '}';
    }
}
