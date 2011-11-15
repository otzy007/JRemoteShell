/*
 * Directory related stuff
 */
package jremoteshell.filebrowse;

import java.io.File;


/**
 *
 * @author Andrei G
 */
public class Directory {
    
    /**
     * Directory related class
     */
    
    File dir;
    
    public Directory(String dir) {
        this.dir = new File(dir);
    }

    public Directory() {
        this.dir = new File(".");
    }
    
    public String[] list() {
       /**
         * List directory content
         */
        return dir.list();
       
    }
    
    public String changedir(String dir) {
        /**
         * Change directory
         */
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
    
    public String pwd() {
        /**
         * print the absolute path of directory
         */
        String pwd = dir.getAbsolutePath();
        
        pwd = pwd.substring(0, pwd.length() - 1);
        
        if (pwd == null)
            return("Cannot get current working directory");
        else
            return pwd;
    }
    
    public String getAbsolutePath(String file) 
            throws StringIndexOutOfBoundsException {
        if (file.substring(0, 1).contains("/") ||
                file.contains(":"))
            return file;
        return (System.getProperty("user.dir") + "/" + file);
    }
}