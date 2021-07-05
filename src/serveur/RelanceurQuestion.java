package serveur;

import client.ConnexionClient;
import client.Message;
import org.joda.time.LocalDateTime;
import org.joda.time.Period;
import pootp1.Joueur;

import java.util.TimerTask;

/**
 * ce timer agit toutes les unes secondes il est déclaré dans la classe Serveur
 */
public class RelanceurQuestion extends TimerTask {


    @Override
    public void run() {
        //On envoie un message a tous les joueurs pour réinitialiser leurs progress bar de temps
        Connexion.distribuerMessage(new Message(Serveur.getJoueur(),"PG1"));
        // on parcoure les joueurs enregistré par le serveur
        for (Joueur joueur : Serveur.getJoueurList()) {
            // si la réponse du joueur n'est pas nulle c'est qu'il vient juste de répondre (1 s d'intervalle)
            if (joueur.getReponse() != null) {
                //on récupere la réponse donnée par le joueur ca sera l'equivaleur de Serveur.bonneReponse + 1 (si la réponse est bonne)
                int indiceReponce = Integer.parseInt(joueur.getReponse());
                //on instancie une nouvelle date
                LocalDateTime differenceTemps = LocalDateTime.now();
                //utilisation de la bibliotheque joda pour la gestion des dates.
                Period period = new Period(Serveur.getDateDebutQuestion(), differenceTemps);
                // on fait la différence de secondes entre  la date de début de question (qui est instancié dans la classe Serveur et celle qui vient detre définie)
                int diff = Math.abs(period.getSeconds());
                //Retourne l'indice du tableau relatif au joueur en cours
                int indiceTableau = Connexion.equivalentTableau(joueur);
                // si le temps n'est pas écoulé et que c'est la bonne réponse
                if (diff <= Serveur.getNombreSecondesQuestion() && indiceReponce == Serveur.getBonneReponse() + 1) {
                    // alors on augmente les points du joueur
                    // Les points sont calculé en fonction du temps comme ci dessous :
                    //joueur.getScore() + Serveur.getNombreSecondesQuestion() - diff+1
                    // au plus le temps passera au moins le joueur aura de points

                    Serveur.getJoueurList().get(indiceTableau).setScore(joueur.getScore() + Serveur.getNombreSecondesQuestion() - diff+1);

                }
                Serveur.getJoueurList().get(indiceTableau).setReponse(null);

                //System.out.println(Serveur.getJoueurList());


            }
        }
    }
}
