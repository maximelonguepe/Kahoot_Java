package pootp1;

import java.io.Serializable;
import java.util.Objects;

/**
 * classe joueur
 * elle est importante et necessaire pour envoyer un message a un destinataire
 * il a un login,mdp,id
 * reponse va permettre lors d'un clic sur l'interface Client d'envoyer un message au serveur.
 * le champ aRepondu permet d'éviter le multi appui lors d'une question
 */
public class Joueur implements Serializable, Comparable<Joueur> {
    private String login;
    private String mdp;
    private int idJoueur;
    private double score=0;
    private String reponse=null;
    private boolean aRepondu=false;

    public String getReponse() {
        return reponse;
    }

    public void setReponse(String reponse) {
        this.reponse = reponse;
    }

    public int getIdJoueur() {
        return idJoueur;
    }

    public void setIdJoueur(int idJoueur) {
        this.idJoueur = idJoueur;
    }

    public Joueur(String login, String mdp) {
        this.login = login;
        this.mdp = mdp;
    }

    public boolean isaRepondu() {
        return aRepondu;
    }

    public void setaRepondu(boolean aRepondu) {
        this.aRepondu = aRepondu;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public Joueur()  {
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getMdp() {
        return mdp;
    }

    public void setMdp(String mdp) {
        this.mdp = mdp;
    }

    @Override
    public String toString() {
        return "Joueur{" +
                "login='" + login + '\'' +
                ", score=" + score +
                ", reponse='" + reponse + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Joueur joueur = (Joueur) o;
        return idJoueur == joueur.idJoueur;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idJoueur);
    }

    /**
     * permet de trier un tableau de joueurs par leur score
     * @param that
     * @return
     */
    @Override
    public int compareTo(Joueur that) {
        if (this.getScore() != that.getScore()) {
            return (this.getScore() < that.getScore() ? -1 : 1);
        }

        return 0;
    }
}