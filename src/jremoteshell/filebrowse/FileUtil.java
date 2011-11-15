/**
 * File related tools:
 * getInfo
 * isDir
 * remove
 * move 
 * copy
 */
package jremoteshell.filebrowse;

/**
 *
 * @author oz
 */

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.sql.Time;
import java.sql.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileUtil {
    public static String getInfo(File f) {
        /**
         * ia informatii despre fisier
         */
        return (isDir(f) + trueFalse(f.canRead()) + trueFalse(f.canWrite()) 
                + trueFalse(f.canExecute()) + " " + f.getAbsolutePath() + " "
                + f.length() + " " + new Date(f.lastModified()).toString() 
                + " " + new Time(f.lastModified()).toString());
    }
    
    public static String isDir(File f) {
        if (f.isDirectory())
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
    
    public static boolean remove (File file) {
        return file.delete();
    }
    
    public static boolean move (File f, File dest) {
        return f.renameTo(dest);
    }
    
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