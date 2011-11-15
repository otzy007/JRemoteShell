/*
 * File transfer implementation
 * Methods:
 * get
 * push
 */
package jremoteshell;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author Andrei G
 */

class FileTransfer {
    public static final int PORT = 8101;

    FileTransfer(String cmd, String name) {
        ServerSocket serverSocket = null;
        
        try {
            if (cmd.contentEquals("put")) {
                /**
                 * put the file
                 */
                serverSocket = new ServerSocket(PORT);
                Socket socket = serverSocket.accept();
                
                DataInputStream src = new DataInputStream(socket.getInputStream());
                DataOutputStream file = new DataOutputStream(new FileOutputStream(name));
                int c;
                
                while ((c = src.read()) !=-1)
                    file.write(c);
                
                file.close();
                src.close();
                socket.close();
                serverSocket.close();
            }
            
            if (cmd.contentEquals("get")) {
                /**
                 * get the file
                 */
                serverSocket = new ServerSocket(PORT);
                Socket socket = serverSocket.accept();
                
                DataOutputStream dest = new DataOutputStream(socket.getOutputStream());
                DataInputStream file = new DataInputStream(new FileInputStream(name));           
                int c;
                
                while ((c = file.read()) != -1) {
                    dest.write(c);
                }
                            
                dest.flush();
                dest.close();
                file.close();
                socket.close();
                serverSocket.close();
                serverSocket = null;
     
            }
           
        }   catch(Exception e) {
            
        }
    }    
}