/*
 * JRemoteShell is an application that provides a remote shell bash like.
 * 
 * This application does not use encryption or authentication so use it at your
 * own risk.
 * 
 * To connect to the server you can use putty with a raw connection or netcat:
 * nc host 8100
 */
package jremoteshell;

/**
 * @author Andrei G
 */

 

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PipedReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import jremoteshell.filebrowse.Directory;
import jremoteshell.filebrowse.FileBrowser;


class ClientThread extends Thread {
    /**
     * Client's thread
     */
    Socket socket = null;
    
    public ClientThread(Socket socket) {
        this.socket = socket;
    }
    
    @Override
    public void run() {
        String request, answer;
        
        try {
            
            String workingDir = System.getProperty("user.dir");
            Directory dir = new Directory();
            
            while(true) {
                BufferedReader in = new BufferedReader(new InputStreamReader(
                    socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream());
                FileBrowser fb = new FileBrowser(dir);
                PipedReader pipe = new PipedReader(fb.pipe);

                out.print("> ");
                request = in.readLine();
               
                if (request.equals("exit")) {
                    /*
                     * Kill current thread
                     */
                    out.println("BYE");
                    out.flush();
                    break;
                }

                if (request.equals("die die")) {
                    /*
                     * Kill the application
                     */

                    out.println("BYE BYE");
                    out.flush();
                    System.exit(0);
                    break;
                }

                if (request.equals("help")) {
                    out.println("Avaible commands:");
                    out.println("exit\nput\nget\ncp\nls\nmv\ninfo\npwd\nhead");
                }

                if (request.contains("put ")) {
                    FileTransfer ft = new FileTransfer("put",
                                    dir.getAbsolutePath(request.substring(4)));
                    break;
                }

                if (request.contains("get ")) {
                    FileTransfer ft = new FileTransfer("get",
                                     dir.getAbsolutePath(request.substring(4)));
                    break;

                } else {
                    /*
                     * Execute file related commands
                     */
                    fb.exec(request);
                    Scanner sc = new Scanner(pipe);

                    while (sc.hasNext())
                        out.print(sc.next() + " ");

                    out.println("");
                    sc.close();
                }
                out.flush();
                pipe.close();
            }
            System.setProperty("user.dir", workingDir);
        }   catch(IOException e) {
            System.out.println("IO Exception\n" + e);
            
        }   finally {
            try {
                socket.close();
            }   catch (IOException e) {
                System.out.println("Socket cannot be closed\n" + e);
            }
        }
    } 
}

public class JRemoteShell {
    /**
     * Server class
     */
    public static final int PORT = 8100;
    
    public JRemoteShell() {
        ServerSocket serverSocket = null;
        
        try {
            serverSocket = new ServerSocket(PORT);
            
            while (true) {
                Socket socket = serverSocket.accept();
                
                ClientThread t = new ClientThread(socket);
                t.start();
            }
            
        }   catch(IOException e) {
            System.err.println("IO Error\n" + e);
        }
    }
    
    public static void main(String[] args) {
        JRemoteShell server = new JRemoteShell();
    }
}
