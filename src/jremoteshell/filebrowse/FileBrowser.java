/*
 * File Browsing
 * 
 */
package jremoteshell.filebrowse;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PipedWriter;
import java.io.PrintWriter;
import java.util.Scanner;


/**
 *
 * @author oz
 */
public class FileBrowser {
    Directory d;
    public PipedWriter pipe = new PipedWriter();
    public PrintWriter pw = new PrintWriter(pipe);

    public FileBrowser(Directory dir) {
        d = dir;
    }
    
    public  boolean exec(String cmd) throws IOException {
        /**
         * Execute file related commads
         */
        
        if (cmd.contentEquals("ls") || cmd.contentEquals("dir") ||
                cmd.contains("ls ") || cmd.contains("dir ")) {
            /*
             * List directory content
             */
            if (cmd.length() > 3) {
                String[] a = cmd.split(" ");
                d = new Directory(d.getAbsolutePath(a[1]));
            }
            
            try {
               String list[] = d.list();
               for (int i = 0; i < list.length; i++) {
                    pw.write(list[i]);
                    pw.write("\n");
               }
               pw.write("\0");
               pw.flush();
               pw.close();
               return true;
               
            } catch (Exception e) {
                return false;
            }
        }
        
        if (cmd.contains("cd ")) {
            /*
             * Directory changing
             */
            cmd = cmd.substring(3);
            if (d.changedir(cmd) == null)
                 pw.write("Cannot change directory: " + cmd);

            pw.flush();
            pw.close();
            
            return true;
        }
        
        if (cmd.contains("info ")) {
            /*
             * Get file info
             * same as ls -l in Linux
             */
            cmd = cmd.substring(5);
            pw.write(FileUtil.getInfo(new File(d.getAbsolutePath(cmd))));
            pw.flush();
            pw.close();
            
            return true;
        }
        
        if (cmd.contains("rm ")) {
            /*
             * Remove file
             */
            cmd = cmd.substring(3);
            if (!FileUtil.remove(new File(d.getAbsolutePath(cmd))))
                pw.write("Cannot remove: " + cmd);
            
            pw.flush();
            pw.close();
          
            return true;
        }
        
        if (cmd.contains("mv ")) {
            cmd = cmd.substring(3);
            String file[] = cmd.split(" ", 2);
            if (!FileUtil.move(new File(d.getAbsolutePath(file[0])), 
                                         new File(d.getAbsolutePath(file[1]))))
                pw.write("Cannot move: " + file[0] + " to " + file[1]);
            pw.flush();
            pw.close();
            
            return true;
        }
        
        if (cmd.contains("pwd")) {
            pw.write(d.pwd());
            pw.flush();
            pw.close();
            
            return true;
        }
        
        if (cmd.contains("cp ")) {
            cmd = cmd.substring(3);
            String files[] = cmd.split(" ");
            
            if ( !FileUtil.copy(new File(d.getAbsolutePath(files[0])),
                                        new File(d.getAbsolutePath(files[1]))))
                pw.write("Cannot copy file");      
            pw.flush();
            pw.close();
            
            return true;
        }
        
        if (cmd.contains("head ")) {
            /**
             * return first 18 lines from file
             */
            cmd = cmd.substring(5);
            try {
                Scanner file = new Scanner(new File(d.getAbsolutePath(cmd)));
                int counter = 18;
                while (file.hasNextLine() && counter > 0) {
                    pw.write(file.nextLine());
                    counter--;
                }    
                pw.flush();
                pw.close();

                return true;
            } catch (FileNotFoundException e) {
                pw.write("File not found");
            }
            
        } else 
            pw.write("Cannot execute command");
            
        pw.flush(); 
        pw.close();
        return true;
    }
}