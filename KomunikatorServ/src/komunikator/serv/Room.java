/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package komunikator.serv;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import komunikator.messages.Message;

/**
 *
 * @author Krzyś
 */
public class Room {
    private static long  id=0;
    private final static AtomicBoolean ab=new AtomicBoolean(false);
    public final List<ClientThread> clientsList = Collections.synchronizedList(new ArrayList<>());
    private int capacity;
    private String roomName;
    private volatile long roomId;
    private final static int defaultCapacity=3;
    
    public Room(){
        this.roomName="defaultRoomName";
        this.capacity=defaultCapacity;
        this.roomId=Room.id;
        Room.incrementId();
        
    }
    private static void incrementId(){
        while(ab.getAndSet(true)){
            
        }
        Room.id++;
        ab.set(false);
    }
    
    public Room(String roomName){
        this.roomName=roomName;
        this.capacity=defaultCapacity;
        this.roomId=Room.id;
        Room.incrementId();
    }
    
    public Room(String roomName,int capacity){
        this.roomName=roomName;
        this.capacity=capacity;
        this.roomId=Room.id;
        Room.incrementId();
    }
    
    public Room(int capacity){
        this.capacity=capacity;
        this.roomId=Room.id;
        Room.incrementId();
    }
    
    public void sendToAllUsers(Message msg) {
        synchronized (clientsList) {
            for (ClientThread ct : clientsList) {
                ct.send(msg);
            }
        }
    }
    
    public long getRoomId(){
        return this.roomId;
    }
    public synchronized boolean isFull(){
        return clientsList.size()==capacity;
    }
    
    public synchronized String getRoomName(){
        return this.roomName;
    }
    
}
