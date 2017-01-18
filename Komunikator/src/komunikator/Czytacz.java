/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package komunikator;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import komunikator.messages.Message;

/**
 *
 * @author Krzyś
 */
public class Czytacz implements Runnable{
    ObjectInputStream in;
    JTextArea j;
    JLabel userCounter;
    RoomListComponent rlc;
    
    public Czytacz(ObjectInputStream in, JTextArea j, JLabel userCounter, RoomListComponent rlc){
        this.j=j;
        this.in=in;
        this.rlc=rlc;
        this.userCounter = userCounter;
    }
    
    @Override
    public void run() {
        while(true){
            Message fromServer;
            try {
                if((fromServer=(Message) in.readObject())!=null){
                    reactToMessage(fromServer);
                }
            } catch (IOException | ClassNotFoundException | ClassCastException ex) {
                Logger.getLogger(Czytacz.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private void reactToMessage(Message msg){        
        switch (msg.getType()) {
            case MESSAGE:
                String displayedMsg = String.format("%s: %s%n", 
                        msg.getSenderName(), msg.getContent());
                j.append(displayedMsg);
                break;
            case USER_LIST:   
                String userList = String.format("Users: %s%n", msg.getContent());
                j.append(userList);
                break;
            case NEW_USER:
                String newUser = String.format("%s has connected%n", msg.getSenderName());
                j.append(newUser);
                break;
            case USER_DEAD:
                String dearDeparted = String.format("%s is no longer with us%n", msg.getSenderName());
                j.append(dearDeparted);
                break;
            case USER_NAME_CHANGE:
                String nameChange = String.format("%s changed nickname to %s%n", msg.getSenderName(), msg.getContent());
                j.append(nameChange);
                break;
            case ROOM_LIST:
                roomUpdater(msg.getContent());
                break;
            case NUMBER_OF_USERS:
                userCounter.setText(msg.getContent());
                break;
                
        }
        
        j.setCaretPosition(j.getDocument().getLength());
    }
    
    private void roomUpdater(String s){
        String [] roomsData = s.split(", ");
        String [] roomsNames = new String[roomsData.length];
        String [] roomsIds = new String[roomsData.length];
        for(int i=0;i<roomsData.length;i++){
            String [] singleRoomData = roomsData[i].split(":");
            if(singleRoomData.length>=2){
                roomsNames[i]= singleRoomData[1];
                roomsIds[i]= singleRoomData[0];
            }
        }
        rlc.setData(roomsNames, roomsIds);
       
    }
}
