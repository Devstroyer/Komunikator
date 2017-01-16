/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package komunikator;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTextArea;
import komunikator.messages.Message;

/**
 *
 * @author Krzyś
 */
public class Czytacz implements Runnable{
    ObjectInputStream in;
    JTextArea j;
    
    public Czytacz(ObjectInputStream in, JTextArea j){
        this.j=j;
        this.in=in;
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
        // GUI nie jest bardzo ThreadSafe (TM), do poprawy, jakoś
        // Wystarczyłby pewnie synchrtonized (j), ale to już mamy ;-)
        
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
        }
        
        j.setCaretPosition(j.getDocument().getLength());
    }
}
