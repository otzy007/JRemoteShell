/*
 * Directory related stuff
 */
package jremoteshell.filebrowse;

import java.io.File;


/**
 *
 * @author Andrei G
 */

/**
 * Directory related class
 */
public class Directory {    
    File dir;
    
    /**
     * Create a new Directory object
     * @param dir   directory
     */
    public Directory(String dir) {
        this.dir = new File(dir);
    }
    
    /**
     * Create a new Directory object for current directory
     */
    public Directory() {
        this.dir = new File(".");
    }
    
    /**
     * List directory content
     */
    public String[] list() {
        return dir.list();
    }
    
    /**
     * Change the current directory
     * @param   dir directory to change to
     * @return  the directory changed to or null
     */
    public String changedir(String dir) {
        String newDir = new String();
        String oldDir = System.getProperty("user.dir");
        if (dir.substring(0,1).contains("/") ||
                dir.contains(":"))
            newDir = dir;
        else 
            newDir = oldDir + "/" + dir;
        
        System.setProperty("user.dir", newDir);
        try {
            this.dir = new File(newDir);
            if (!this.dir.isDirectory()) {
                System.setProperty("user.dir", oldDir);
                this.dir = new File(oldDir);
                throw new Exception();
            }
           return newDir;
        } catch(Exception e) {
            return null;
       }
    }
    
    /**
     * Get the absolute path of working dir
     * @return path of working dir
     */
    public String pwd() {
        String pwd = dir.getAbsolutePath();
        
        pwd = pwd.substring(0, pwd.length() - 1);
        
        if (pwd == null)
            return("Cannot get current working directory");
        else
            return pwd;
    }
    
    /**
     * Get the absolute path of file/directory
     * @param file
     * @return the absolute path of file
     * @throws StringIndexOutOfBoundsException 
     */
    public String getAbsolutePath(String file) 
            throws StringIndexOutOfBoundsException {
        if (file.substring(0, 1).contains("/") ||
                file.contains(":"))
            return file;
        return (System.getProperty("user.dir") + "/" + file);
    }
}