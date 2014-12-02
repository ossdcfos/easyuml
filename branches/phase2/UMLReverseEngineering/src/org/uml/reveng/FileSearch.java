package org.uml.reveng;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.io.FilenameUtils;

/**
 * Utile class that recursively searches the selected project for Java files.
 *
 * @author Milan Djoric
 */
public class FileSearch {

    private static String requiredExtension;
    private static List<String> foundFiles = new ArrayList<String>();

    /**
     * Initiates search at the given file parh for the given file types (default
     * .java).
     *
     * @param directory to be searched
     * @param extensionToSearchFor extension of files that need to be found
     * @return List of found files's path
     */
    public static List<String> searchTheDirectory(File directory, String extensionToSearchFor) {
        //Clear the static list of found files
        foundFiles.clear();
        System.out.println(directory);
        List<String> allPaths = new ArrayList<String>();
        //Sets the static required extension of files to be searched
        requiredExtension = extensionToSearchFor;
        if (directory.isDirectory()) {
            //Recursively searches the directory
            search(directory);
            //Add all to list of found files
            allPaths.addAll(foundFiles);
        } else {
            System.out.println(directory.getAbsoluteFile() + " not a directory!");
        }
        return allPaths;
    }

    /**
     * Recursively searches the selected folder for files with previously defined extension (default .java).
     *
     * @param pathToSearchAt
     */
    private static void search(File pathToSearchAt) {
        if (pathToSearchAt.isDirectory()) {
            System.out.println("Searching the directory: " + pathToSearchAt.getAbsoluteFile());
            //Check the read permission	
            if (pathToSearchAt.canRead()) {
                //Check all files inside the directory
                for (File candidateFile : pathToSearchAt.listFiles()) {
                    //If it is a directory, enter it and search deeper
                    if (candidateFile.isDirectory()) {
                        search(candidateFile);
                    } else {
                        //If it is a file, check whether it's extension is the same as the required...
                        String candidatesExtension = FilenameUtils.getExtension(candidateFile.getAbsolutePath());
                        System.out.println(candidateFile.getAbsoluteFile().toString());
                        if (requiredExtension.equals(candidatesExtension)) {
                            //... and add it to the list of found files
                            foundFiles.add(candidateFile.getAbsoluteFile().toString());
                        }
                    }
                }
            } else {
                System.out.println(pathToSearchAt.getAbsoluteFile() + " - no access!");
            }
        }
    }
}
