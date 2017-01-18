/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package komunikator.serv;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import komunikator.messages.Message;
import komunikator.messages.MsgType;

/**
 *
 * @author Krzyś
 */
public class ClientThread implements Runnable{
    private String clientName;
    private final Socket socket;
    private final ObjectOutputStream out;
    private final ObjectInputStream in;
    private Room currentRoom;
    private final Semaphore countingSemaphore;
    
    public ClientThread(Socket socket, Semaphore countingSemaphore) throws IOException{
        this.countingSemaphore = countingSemaphore;
        this.clientName="notSet";
        this.socket=socket;
        this.out = new ObjectOutputStream(socket.getOutputStream());
        this.in = new ObjectInputStream(socket.getInputStream());
    }

    @Override
    public void run() {        
        //Dodanie nowego uzytkownika i powiadomienie reszty
        joinFirstAvailableRoom();
        send(createRoomListMsg());
        while(true){
            if(in==null){
                break;              
            }
            Message fromClient;
            try {
                if((fromClient = (Message)in.readObject())!=null){
                    reactToMessage(fromClient);                          
                }
            } catch (IOException ex) {    
                try {
                    leave();
                } catch (InterruptedException ex1) {
                    Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex1);
                }
                System.out.println(ex);
                break;
            } catch (ClassNotFoundException | ClassCastException ex) {
                Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private void joinFirstAvailableRoom() {
        synchronized(KomunikatorServ.roomList) {
            for(int i=0;i<KomunikatorServ.roomList.size();i++){
                if(!KomunikatorServ.roomList.get(i).isFull()){
                    currentRoom=KomunikatorServ.roomList.get(i);
                    currentRoom.clientsList.add(this);
                    sendOut(createIJustJoinedMsg());
                    break;
                }    
            }
            
            if(currentRoom==null){
                    currentRoom= new Room();
                    KomunikatorServ.roomList.add(currentRoom);
                    currentRoom.clientsList.add(this);
                    sendOut(createIJustJoinedMsg());
            }
        }
    }
    
    private void leave() throws InterruptedException {
        countingSemaphore.acquire();
        KomunikatorServ.numberOfUsers--;
        countingSemaphore.release();
        currentRoom.clientsList.remove(this);
        sendOut(createUserDisconnectedMsg());
    }
    
    //Sprawdzacz i reagowacz na co tam takiego nam przysłali ciekawego
    private void reactToMessage(Message msg){
        msg = addUsernameTo(msg);
        switch (msg.getType()) {
            case USER_NAME_CHANGE:
                if (tryRenameClient(msg.getContent())) {
                    sendOut(msg);                    
                }
                break;
            case USER_LIST:
                sendOut(createUsernameListMsg());
                break;
            case ROOM_LIST:
                send(createRoomListMsg());
                break;
            case CHANGE_ROOM:
                parseToRoomId(msg.getContent());
                break;
            case CREATE_ROOM:
                createRoom(msg.getContent());
                break;
            default:
                sendOut(msg);
                break;
        }
    }    
    
    private void createRoom(String s){
        KomunikatorServ.roomList.add(new Room(s));
        send(createRoomListMsg());
    }
    private Message createUsernameListMsg() {
        synchronized(currentRoom.clientsList) {
            String users =currentRoom.clientsList.stream()
                    .map(client -> client.getName())
                    .collect(Collectors.joining(", "));
            return new Message(clientName, MsgType.USER_LIST, users);
        }
    }
    
    private void parseToRoomId(String textId){
        try{
            long id=Long.parseLong(textId);
            tryChangeRoom(id);
        }
        catch(Exception e){}
    }
    private void tryChangeRoom(long id){
        for(int i=0;i<KomunikatorServ.roomList.size();i++){
            if(KomunikatorServ.roomList.get(i).getRoomId()==id && !KomunikatorServ.roomList.get(i).isFull()){
                if(currentRoom!=KomunikatorServ.roomList.get(i)){
                    sendOut(createInfoMsg(this.getName()+" has left the room"));
                    currentRoom.clientsList.remove(this);
                    currentRoom =KomunikatorServ.roomList.get(i);
                    currentRoom.clientsList.add(this);
                    send(createInfoMsg("You've changed your room to "+currentRoom.getRoomName()));
                    sendOut(createIJustJoinedMsg()); 
                }
            }
        }
    }
    
    private Message createInfoMsg(String info){
        return new Message("Server",MsgType.MESSAGE,info);
    }
    
    private Message createRoomListMsg() {
        synchronized(currentRoom.clientsList) {
            String rooms =KomunikatorServ.roomList.stream()
                    .map(room -> Long.toString(room.getRoomId())+":"+room.getRoomName())
                    .collect(Collectors.joining(", "));
            
            return new Message(clientName, MsgType.ROOM_LIST, rooms);
        }
    }
    
    private Message createIJustJoinedMsg() {
        return new Message(clientName, MsgType.NEW_USER,null);
    }
    
    private void sendOut(Message msg) {
        synchronized(currentRoom.clientsList) {
            for (ClientThread client : currentRoom.clientsList) {
                client.send(msg);
            }
        }
    }
    
    private boolean tryRenameClient(String newName) {
        boolean success = false;
        // Tu się może trochę głupio zrobić, jak dwóch postanowi mieć tą samą nazwę...
        if (newName != null && newName.length() > 1) {
            clientName = newName;
            success = true;
        }
        return success;
    }
    
    private Message addUsernameTo(Message msg) {
        return new Message(clientName, msg.getType(), msg.getContent());
    }
    
    private Message createUserDisconnectedMsg() {
        return new Message(clientName, MsgType.USER_DEAD, null);
    }
    
    public void send(Message msg){
        try {
            out.writeObject(msg);
        } catch (IOException ex) {
            Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public String getName(){
        return this.clientName;
    }
}
