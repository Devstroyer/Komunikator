/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package komunikatorserv;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 *
 * @author Krzyś
 */
public class KomunikatorServ {
    public static ArrayList<ClientThread> list = new ArrayList<>();
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
