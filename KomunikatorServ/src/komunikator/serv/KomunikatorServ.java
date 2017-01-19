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
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import komunikator.messages.Message;
import komunikator.messages.MsgType;

/**
 *
 * @author Krzyś
 */
public class KomunikatorServ {
    public static final List<ClientThread> list = Collections.synchronizedList(new ArrayList<>());
    public static final List<Room> roomList = Collections.synchronizedList(new ArrayList<>());
    public static volatile long numberOfUsers;
    private static Semaphore counterSemaphore;
    private static long clientID;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        counterSemaphore = new Semaphore(1);
        numberOfUsers = 0;
        roomList.add(new Room("First Room"));
        clientID=1;
        scheduleUserNumberUpdates();
        ServerSocket serverSocket = new ServerSocket(8080);
        try {
            System.out.println("Server started!");
            while(true){
                Socket clientSocket = serverSocket.accept();
                ClientThread clientThread = new ClientThread(clientID,clientSocket, counterSemaphore);
                Thread watek = new Thread(clientThread);
                incrementNumberOfUsers();
                clientID++;
                watek.start();
            }
        }
        catch(IOException | InterruptedException e){
           System.out.println(e);
        }
    }
    
    private static void incrementNumberOfUsers() throws InterruptedException {
        counterSemaphore.acquire();
        numberOfUsers++;
        counterSemaphore.release();        
    }
    
    private static void scheduleUserNumberUpdates() {
        ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
        exec.scheduleAtFixedRate(new Runnable() {
            public void run() {
                Message numberOfUsers = new Message("Server", MsgType.NUMBER_OF_USERS, 
                        KomunikatorServ.numberOfUsers + "");
                synchronized (roomList) {
                    for (Room r : roomList) {
                        r.sendToAllUsers(numberOfUsers);
                    }
                } 
            }
        }, 5, 5, TimeUnit.SECONDS);
    }
}
