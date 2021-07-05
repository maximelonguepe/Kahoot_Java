package pootp1;

import java.util.Objects;

/**
 * classe "mère"de Question et Reponse
 */
public class Option implements InformationGenerale{
    public Option() {
    }

    private int noOption;
    private String texteOption;
    //private static int nbOptions = 0;

    public Option(String texteOption) {
        //this.noOption = nbOptions;
        this.texteOption = texteOption;
        //this.nbOptions++;
    }

    public void finalize() { // deprecated
        System.out.println("Objet détruit");
        //nbOptions--;
    }


    public String getTexteOption() {
        return texteOption;
    }

    public void setTexteOption(String texteOption) {
        this.texteOption = texteOption;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Option)) return false;
        Option option = (Option) o;
        return Objects.equals(getTexteOption(), option.getTexteOption());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTexteOption());
    }

    @Override
    public String toString() {
        return "Option{" +
                ", texteOption='" + texteOption + '\'' +
                '}';
    }

    @Override
    public boolean estUneQuestion() {
        if (this.getTexteOption().indexOf('?') >= 0)
        {
            return true;
        }
        return false;
    }
}
