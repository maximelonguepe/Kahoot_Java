package pootp1;

import java.util.Objects;

/**
 * contient un id et un texte de réponse
 */
public class Reponse extends Option {
    private int idReponse;

    public Reponse() {
    }

    public int getIdReponse() {
        return idReponse;
    }

    public void setIdReponse(int idReponse) {
        this.idReponse = idReponse;
    }

    public Reponse(String texteOption) {
        super(texteOption);
    }

    @Override
    public String toString() {
        return this.getTexteOption();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Reponse reponse = (Reponse) o;
        return idReponse == reponse.idReponse;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), idReponse);
    }
}


