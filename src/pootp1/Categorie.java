package pootp1;

import java.util.Objects;

/**
 * classe Categorie : va permettre d'avoir une catégorie de quizz pendant le quizz
 * elle a aussi un id pour la recuperation en bdd
 */
public class Categorie {
    private String texteCategorie;
    private int idCategorie;

    public Categorie(String texteCategorie) {
        this.texteCategorie = texteCategorie;
    }

    public int getIdCategorie() {
        return idCategorie;
    }

    public Categorie(int idCategorie) {
        this.idCategorie = idCategorie;
    }

    public Categorie() {
    }

    public String getTexteCategorie() {
        return texteCategorie;
    }

    public void setTexteCategorie(String texteCategorie) {
        this.texteCategorie = texteCategorie;
    }

    public void setIdCategorie(int idCategorie) {
        this.idCategorie = idCategorie;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Categorie)) return false;
        Categorie categorie = (Categorie) o;
        return getTexteCategorie().equals(categorie.getTexteCategorie());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTexteCategorie());
    }

    @Override
    public String toString() {
        return "Categorie{" +
                "texteCategorie='" + texteCategorie + '\'' +
                ", idCategorie=" + idCategorie +
                '}';
    }
}
