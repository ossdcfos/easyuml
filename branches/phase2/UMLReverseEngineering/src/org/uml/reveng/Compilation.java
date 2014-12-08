package org.uml.reveng;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

import java.util.LinkedList;
import javax.annotation.processing.AbstractProcessor;
import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;

import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import org.apache.commons.io.FileUtils;
import org.uml.model.ClassDiagram;
import static org.uml.reveng.CompilationProcessor.generatedDiagram;

/**
 * Compilation process that processes all required files starts here.
 *
 * @author Milan Djoric
 */
public class Compilation {

    /**
     * Starts the compilation process and cleans compiled files afterwards.
     *
     * @param projectSource where the selected project is located
     * @param separator default file system separator character
     */
    public static void initiateCompilation(String projectSource, String separator) {
        //Creating an instance of Java compiler
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        //Creating an instance of Standard Java file manager
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);
        
        //Setting the root folder location and extension of files to search for
        String projectBasePath = projectSource + separator;
        String source = projectBasePath + "src";
        String extension = "java";
        //Generating an array of filepaths corresponding to found files
        Collection<File> files = FileUtils.listFiles(new File(source), new String[]{extension}, true);
        String[] listaStr = new String[files.size()];
        int i = 0;
        for(File f : files){
            listaStr[i++] = f.getAbsoluteFile().toString();
        }
        
        //Creating a list of files to be compiled
        Iterable<? extends JavaFileObject> compilationUnits = fileManager.getJavaFileObjects(listaStr);
        
        //Creating tempClasses folder and emptying one, if it already exists
        String tempClassFolderName = "UMLTemp";
        String tempClassFolderPath = projectBasePath + tempClassFolderName;
        File tempClassFolder = new File(tempClassFolderPath);
        tempClassFolder.mkdirs();
        try {
            FileUtils.cleanDirectory(tempClassFolder);
        } catch (IOException ex) {
            System.out.println("Could not erase folder's contents!");
        }
        
        // **** FIRST PASS ****
        //Setting the compilation options
        String encoding = "UTF-8";
        Iterable<String> compOptions = Arrays.asList("-g", "-encoding", encoding, "-d", tempClassFolderPath);
        //Creating a compilation task that will compile found files
        CompilationTask task = compiler.getTask(null, fileManager, null, compOptions, null, compilationUnits);
        //Creating a list that will hold annotation processors
        LinkedList<AbstractProcessor> processors = new LinkedList<>();
        //Adding a annotation processor(s) to the the above mentioned list
        processors.add(new CompilationProcessor());
        CompilationProcessor.generatedDiagram = new ClassDiagram();
        //Setting annotation prosessor(s) to the compilation task
        task.setProcessors(processors);
        //Staritng the compilation
        task.call();
        
        //Searching for .class files that have been made
//        Collection<File> classFiles = FileUtils.listFiles(new File(tempClassFolderPath), new String[]{"class"}, true);
//        String[] classFilesStrings = new String[classFiles.size()];
//        i = 0;
//        for(File cf : classFiles){
//            classFilesStrings[i++] = cf.getAbsoluteFile().toString();
//        }
        
//        // **** SECOND PASS ****
//        //Fill in data about Implements and Extends relations that the compiler couldn't get
//        ClassProcessing.ClassProcessor(Arrays.asList(classFilesStrings), separator, tempClassFolderName);
//        //Raises the flag if there are no .class files generated during the compilation process -
//        //if it has failed (usually if the project selected has errors)
//        if (tempClassFolder.listFiles().length == 0) {
//            GeneratedDiagramManager.getInstance().setZeroClassesGenerated(true);
//        } else {
//            GeneratedDiagramManager.getInstance().setZeroClassesGenerated(false);
//        }
        //Delete the folder containing compiled .class files
        try {
            FileUtils.deleteDirectory(tempClassFolder);
        } catch (IOException ex) {
            System.out.println("Directory for deletion not found!");
        }
    }
}
