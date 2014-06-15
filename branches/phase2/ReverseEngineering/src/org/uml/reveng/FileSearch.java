/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uml.reveng;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.io.FilenameUtils;

/**
 *
 * @author Milan Djoric
 */
public class FileSearch {

    private static String traženaEkstenzija;
    private static List<String> pronađeniFajlovi = new ArrayList<String>();

    public static List<String> pretražiDirektorijum(File directory, String ekstenzijaZaPretragu) {
        pronađeniFajlovi.clear();
        System.out.println(directory);
        List<String> ukupnePutanje = new ArrayList<String>();
        traženaEkstenzija = ekstenzijaZaPretragu;
        if (directory.isDirectory()) {
            //Rekurzivno pretražuje fajl strkturu
            pretrazi(directory);
            //Dodaj u listu sve pronađene putanje fajlova
            ukupnePutanje.addAll(pronađeniFajlovi);
        } else {
            System.out.println(directory.getAbsoluteFile() + " nije direktorijum!");
        }
        return ukupnePutanje;
    }

    private static void pretrazi(File mestoPretrage) {
        if (mestoPretrage.isDirectory()) {
            System.out.println("Pretražujem direktorijum: " + mestoPretrage.getAbsoluteFile());
            //proveri dozvolu za čitanje	
            if (mestoPretrage.canRead()) {
                //proveri sve fajlove u tom direktorijumu
                for (File kandidatFajl : mestoPretrage.listFiles()) {
                    //ako jeste direktorijum, uđi i traži dublje
                    if (kandidatFajl.isDirectory()) {
                        pretrazi(kandidatFajl);
                    } else {
                        //ako je pronađen fajl, proveri da li mu se ekstenzija poklapa sa traženom...
                        String ekstenzijaKandidata = FilenameUtils.getExtension(kandidatFajl.getAbsolutePath());
                        System.out.println(kandidatFajl.getAbsoluteFile().toString());
                        if (traženaEkstenzija.equals(ekstenzijaKandidata)) {
                            //... i dodaj je u listu putanja pronađenih fajlova
                            pronađeniFajlovi.add(kandidatFajl.getAbsoluteFile().toString());
                        }
                    }
                }
            } else {
                System.out.println(mestoPretrage.getAbsoluteFile() + " - Nemam pristup!");
            }
        }
    }
}
