/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package komunikator.serv;

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
    public static final List<Room> roomList = Collections.synchronizedList(new ArrayList<>());
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        roomList.add(new Room());
        ServerSocket serverSocket = new ServerSocket(8080);
        try {
            System.out.println("Server started!");
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
