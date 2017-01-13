/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package komunikator;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JList;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 *
 * @author Krzyś
 */
public class Czytacz implements Runnable{
    BufferedReader in;
    JTextArea j;
    public Czytacz(BufferedReader in, JTextArea j){
        this.j=j;
        this.in=in;
    }
    @Override
    public void run() {
        while(true){
            String fromServer;
            try {
                if((fromServer=in.readLine())!=null){
                    messageCheck(fromServer);
                }
            } catch (IOException ex) {
                Logger.getLogger(Czytacz.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private void messageCheck(String message){
        if(message.substring(0, 8).equals("message:")){
            j.append(message.substring(8));
            j.append("\n");
        }
        else if(message.substring(0, 6).equals("users:")){
            String []users = message.substring(6).split(";;;");
            for (String token : users)
            {   
                j.append("users: ");
                j.append(token+";");
            }
            j.append("\n");
        }
        j.setCaretPosition(j.getDocument().getLength());
    }
}
