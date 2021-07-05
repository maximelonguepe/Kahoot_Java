package serveur;

import java.sql.SQLException;
import java.util.TimerTask;

public class Relanceur extends TimerTask {

    /**
     * Toutes les 60 secondes il vérifie si il peut lancer la partie ou non pour cela il faut qu'il y ait 2 joueurs de connectés et que la partie ne soit pas commencée
     */
    @Override
    public void run() {
        if(Serveur.getListConnexion().size()>=2 && (!Serveur.isPartieCommencee())){
            Serveur.setPartieCommencee(true);
            System.out.println("Partie commencee");
            try {
                // on commence la partie
                Serveur.commencerPartie();
            } catch (SQLException | InterruptedException throwables) {
                throwables.printStackTrace();
            }

        }
    }

}
