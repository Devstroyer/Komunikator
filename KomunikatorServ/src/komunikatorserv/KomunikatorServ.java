/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package komunikatorserv;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Krzyś
 */
public class KomunikatorServ {
    public static final List<ClientThread> list = Collections.synchronizedList(new ArrayList<>());
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8080);
        try {
            while(true){
                Socket clientSocket = serverSocket.accept();
                ClientThread clientThread =new ClientThread(clientSocket);
                Thread watek = new Thread(clientThread);
                watek.start();
            }

        }
        catch(Exception e){
           System.out.println(e);
        }
    }
    
}
