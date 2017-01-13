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
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import static komunikatorserv.KomunikatorServ.list;

/**
 *
 * @author Krzyś
 */
public class ClientThread implements Runnable{
    private String clientName;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    
    public ClientThread(Socket socket) throws IOException{
        this.clientName="notSet";
        this.socket=socket;
        this.out =new PrintWriter(socket.getOutputStream(), true);
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        
        //Dodanie nowego uzytkownika i powiadomienie reszty
        KomunikatorServ.list.add(this);
        for(int i=0;i<KomunikatorServ.list.size();i++){
                    KomunikatorServ.list.get(i).send("message:"+clientName+ " has conected");
        }
    }

    @Override
    public void run() {
            
        while(true){
            if(in==null){
                break;              
            }
            String fromClient;
            try {
                if((fromClient=in.readLine())!=null){
                    messageCheck(fromClient);                          
                }
            } catch (IOException ex) {    
                //Usuniecie nowego uzytkownika i powiadomienie reszty oraz zakonczenie watku
                KomunikatorServ.list.remove(this);
                for(int i=0;i<KomunikatorServ.list.size();i++){
                    KomunikatorServ.list.get(i).send("message:"+clientName+ " has disconnected");
                }
                System.out.println(ex);
                break;
            }
        }
    }
    
    //Sprawdzacz co tam takiego nam przysłali ciekawego
    private void messageCheck(String message){
        if(message.length()>8 && message.substring(0, 8).equals("message:")){
            for(int i=0;i<KomunikatorServ.list.size();i++){
                        KomunikatorServ.list.get(i).send("message:"+this.clientName+" : "+ message.substring(8));
            }
        }
        else if( message.length()>5 && message.substring(0, 5).equals("name:")){
            if(message.substring(5).length()>1){
                for(int i=0;i<KomunikatorServ.list.size();i++){
                        KomunikatorServ.list.get(i).send("message:"+this.clientName+" changed nickname to "+ message.substring(5));
                }
                this.clientName=message.substring(5);
            }
        }
        else if(message.length()>5 && message.substring(0, 6).equals("users:")){
            String users="";
            for(int i=0;i<KomunikatorServ.list.size();i++){
                users+=KomunikatorServ.list.get(i).getName()+";;;";
            }
            send("users:"+users);
        }
    }
    
    public void send(String message){
        out.println(message);
    }
    
    public String getName(){
        return this.clientName;
    }
    
    
}
