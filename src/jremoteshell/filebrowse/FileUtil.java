package jremoteshell.filebrowse;

/**
 * File related tools:
 * getInfo
 * isDir
 * remove
 * move 
 * copy
 *
 * @author Andrei G
 */

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.sql.Time;
import java.sql.Date;

public class FileUtil {
    /**
     * get File Informations
     */
    public static String getInfo(File f) {
        
        return (isDir(f) + trueFalse(f.canRead()) + trueFalse(f.canWrite()) 
                + trueFalse(f.canExecute()) + " " + f.getAbsolutePath() + " "
                + f.length() + " " + new Date(f.lastModified()).toString() 
                + " " + new Time(f.lastModified()).toString());
    }
    
    /**
     * @param file
     * @return "d" for dir, "-" for file
     */
    public static String isDir(File file) {
        if (file.isDirectory())
            return "d";
        else
            return "-";
    }
    
    public static String trueFalse(boolean bool) {
        if (bool)
            return "x";
        else
            return "-";
    }
    
    /**
     * Delete a file
     * @param file
     * @return 
     */
    public static boolean remove (File file) {
        return file.delete();
    }
    
    /**
     * Move file f to destination
     * @param f file to move
     * @param dest  destination
     * @return true if moved successfully
     */
    public static boolean move (File f, File dest) {
        return f.renameTo(dest);
    }
    
    /**
     * Copy file f to dest
     * @param f file to copy
     * @param dest  destination
     * @return true if successfully copied
     */
    public static boolean copy(File f, File dest) {
        try {
            DataInputStream src = new DataInputStream(new FileInputStream(f));
            DataOutputStream dst = new DataOutputStream(new FileOutputStream(dest));
            int c;
            
            while ((c = src.read()) !=-1) 
                dst.write(c);
            
            src.close();
            dst.close();
            return true;
            
        } catch (Exception ex) {
            return false;
        }
    }
}