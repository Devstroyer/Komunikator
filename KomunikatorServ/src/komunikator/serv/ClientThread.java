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
import java.util.function.Consumer;
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
    private final long clientId;
    
    public ClientThread(long clientId,Socket socket, Semaphore countingSemaphore) throws IOException{
        this.clientId=clientId;
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
        refreshUserListForAll();
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
                    refreshUserListForAll();
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
                    refreshRoomListForAll();
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
    
    private void refreshUserListForAll(){
        synchronized(KomunikatorServ.roomList) {
                String clients=KomunikatorServ.roomList.stream()
                    .map(room -> room.getSendableUserList())
                        .collect(Collectors.joining(", "));
                Message msg =new Message(null, MsgType.ALL_USERS_LIST, clients);
                KomunikatorServ.roomList.stream().forEach((Room room) -> room.sendToAllUsers(msg));    
        }  
    }
    
    private void refreshRoomListForAll() {
        synchronized(KomunikatorServ.roomList) {
            String rooms =KomunikatorServ.roomList.stream()
                    .map(room -> Long.toString(room.getRoomId())+":"+room.getRoomName())
                    .collect(Collectors.joining(", "));
            Message msg =new Message(null, MsgType.ROOM_LIST, rooms);
            KomunikatorServ.roomList.stream().forEach((Room room) -> room.sendToAllUsers(msg));
        }
    }
    
    
    //Sprawdzacz i reagowacz na co tam takiego nam przysłali ciekawego
    private void reactToMessage(Message msg){
        msg = addUsernameTo(msg);
        switch (msg.getType()) {
            case USER_NAME_CHANGE:
                if (tryRenameClient(msg.getContent())) {
                    sendOut(msg);
                    refreshUserListForAll();                    
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
            case ALL_USERS_LIST:
                send(createAllUsernamesListMsg());
                break;
            case DIRECT_MESSAGE:
                createPrivateMessage(msg);
                break;
            default:
                sendOut(msg);
                break;
        }
    }    
    
    private void createRoom(String s){
        KomunikatorServ.roomList.add(new Room(s));
        refreshRoomListForAll();
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
    
    private void createPrivateMessage(Message msg){
        synchronized (KomunikatorServ.roomList) {
            for(Room room : KomunikatorServ.roomList){
                synchronized (room.clientsList) {
                    for(ClientThread client : room.clientsList){
                        if(client.getClientId()==msg.getReciverId()){
                            send(new Message("Private to "+client.getName(),MsgType.MESSAGE,msg.getContent()));
                            client.send(new Message(getName(),getClientId(),client.getClientId(),MsgType.DIRECT_MESSAGE,msg.getContent()));
                            break;
                        }
                    }
                }
            }
        }
    }
    
    private Message createInfoMsg(String info){
        return new Message("Server",MsgType.MESSAGE,info);
    }
    
    private Message createRoomListMsg() {
        synchronized(KomunikatorServ.roomList) {
            String rooms =KomunikatorServ.roomList.stream()
                    .map(room -> Long.toString(room.getRoomId())+":"+room.getRoomName())
                    .collect(Collectors.joining(", "));
            
            return new Message(clientName, MsgType.ROOM_LIST, rooms);
        }
    }
    
     private Message createAllUsernamesListMsg() {
        synchronized(KomunikatorServ.roomList) {
            String clients=KomunikatorServ.roomList.stream()
                .map(room -> room.getSendableUserList())
                    .collect(Collectors.joining(", "));
            return new Message(clientName, MsgType.ALL_USERS_LIST, clients);
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
        if (newName != null && newName.length() > 1) {
            clientName = newName;
            success = true;
        }
        return success;
    }
    
    private Message addUsernameTo(Message msg) {
        return new Message(clientName,msg.getSenderId(),msg.getReciverId(), msg.getType(), msg.getContent());
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
    public long getClientId(){
        return this.clientId;
    }
}
