package pootp1;

import java.io.File;
import java.io.FileFilter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * permet d'enregistrer en BDD tous les fichiers json présents dans le repertoire courant
 */

public class TestBDD {
    public static void main() throws SQLException {
        RequeteKahoot requeteKahoot = new RequeteKahoot();

        // le repertoire courant
        File repertoire = new File(".");
        // listage des fichiers finissant par json présenrs dans le repertoire courant
        File[] lesFichiers = repertoire.listFiles(new FileFilter() {
            public boolean accept(File file) {
                return file.getName().endsWith(".json");
            }
        });

        for (File f : lesFichiers) {
            requeteKahoot.importJson(f.getName());
        }
    }
}
