/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package komunikator;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import komunikator.messages.Message;
import komunikator.messages.MsgType;

/**
 *
 * @author Krzyś
 */
public class KomFrame extends javax.swing.JFrame {    
    ObjectOutputStream out;
    ObjectInputStream in;
    Socket soc;
    Thread watek;
    /**
     * Creates new form KomFrame
     */
    public KomFrame() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTextField2 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jUserCounterObviously = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jButton3 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        roomListComponent1 = new komunikator.RoomListComponent();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        clientsListComponent = new komunikator.ClientsListComponent();
        jLabel3 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(600, 500));

        jTextField2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField2ActionPerformed(evt);
            }
        });
        jTextField2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextField2KeyPressed(evt);
            }
        });

        jButton1.setText("Send");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Polacz");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jTextArea1.setEditable(false);
        jTextArea1.setColumns(20);
        jTextArea1.setLineWrap(true);
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        jUserCounterObviously.setText("0");

        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });

        jButton3.setText("Add");
        jButton3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton3MouseClicked(evt);
            }
        });
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        roomListComponent1.setScrollignPane(null);
        roomListComponent1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                roomListComponent1KeyPressed(evt);
            }
        });
        jScrollPane2.setViewportView(roomListComponent1);

        jLabel1.setText("Server population:");

        jLabel2.setText("Room List");

        jScrollPane3.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane3.setViewportView(clientsListComponent);

        jLabel3.setText("User List");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jScrollPane3))
                        .addGroup(layout.createSequentialGroup()
                            .addGap(4, 4, 4)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createSequentialGroup()
                                    .addGap(58, 58, 58)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jLabel3)
                                        .addGroup(layout.createSequentialGroup()
                                            .addComponent(jLabel1)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(jUserCounterObviously, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addGroup(layout.createSequentialGroup()
                                    .addGap(21, 21, 21)
                                    .addComponent(jLabel2)))
                            .addGap(46, 46, 46))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jButton2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 218, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jTextField2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 366, Short.MAX_VALUE)
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButton2)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(jUserCounterObviously))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 396, Short.MAX_VALUE)
                            .addComponent(jScrollPane3)))
                    .addComponent(jScrollPane1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1)
                    .addComponent(jButton3)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        if(watek==null){
            try {
            //soc = new Socket("87.207.197.63",8080);
            soc = new Socket("127.0.0.1",8080);
            out = new ObjectOutputStream(soc.getOutputStream());
            
            roomListComponent1.setObjectOutputStream(out);
            roomListComponent1.setScrollignPane(jScrollPane2);
            in = new ObjectInputStream(soc.getInputStream());
            watek = new Thread(new Czytacz(in,jTextArea1,jUserCounterObviously,roomListComponent1,clientsListComponent));
            watek.start();
            jTextArea1.append("To change name write\n/name <user name>\n");
            } catch (IOException ex) {
                Logger.getLogger(KomFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        jTextField2.requestFocus();
        
  
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jTextField2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField2KeyPressed
        if(evt.getKeyCode()== KeyEvent.VK_ENTER && !jTextField2.getText().isEmpty()){
            sendCurrentMessage();
        }
    }//GEN-LAST:event_jTextField2KeyPressed

    private void jTextField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        sendCurrentMessage();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void roomListComponent1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_roomListComponent1KeyPressed

    }//GEN-LAST:event_roomListComponent1KeyPressed

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton3MouseClicked
         sendCreateRoomMessage();
    }//GEN-LAST:event_jButton3MouseClicked

    private void sendCreateRoomMessage(){
        if (!jTextField1.getText().isEmpty()) {
             boolean poprawne=true;
             for (int i=0;i<jTextField1.getText().length();i++){
                 if(!Character.isDigit(jTextField1.getText().charAt(i)) && !Character.isLetter(jTextField1.getText().charAt(i))){
                     poprawne=false;
                 }
             }
             if(poprawne){
                 String roomName = jTextField1.getText();
                 try {
                     out.writeObject(new Message(null,MsgType.CREATE_ROOM,roomName));
                 } catch (IOException ex) {
                     Logger.getLogger(KomFrame.class.getName()).log(Level.SEVERE, null, ex);
                 }
             }
         }
    }
    private void sendCurrentMessage() {
        if (!jTextField2.getText().isEmpty()) {
            Message toSend;
            if(jTextField2.getText().length()>6 && jTextField2.getText().substring(0,6).equals("/name ")){
                toSend = createNameChangeMsg(jTextField2.getText().substring(6));
                setTitle(jTextField2.getText().substring(6));
            }
            else if(jTextField2.getText().length()==6 && jTextField2.getText().substring(0,6).equals("/users")){
                toSend = createListUsersMsg();
            }
            else if(jTextField2.getText().length()==6 && jTextField2.getText().substring(0,6).equals("/rooms")){
                toSend = createListRoomsMsg();
            }
            else if(jTextField2.getText().length()==9 && jTextField2.getText().substring(0,9).equals("/allusers")){
                toSend = createAllUsersListRoomsMsg();
            }
            else if(clientsListComponent.isSomethingChoosed()){
                toSend = createPrivateMsg(jTextField2.getText());
            }
            else{
                toSend = createStandardMsg(jTextField2.getText());
            }
            try {
                out.writeObject(toSend);
            } catch (IOException ex) {
                Logger.getLogger(KomFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
            jTextField2.setText("");
        }
    }
    
    
    private Message createPrivateMsg(String content){
        
        long id=0;
        try{
           id=Long.parseLong(clientsListComponent.getCurrentId()); 
        }
        catch(Exception e){}
        return new Message(null,id,id, MsgType.DIRECT_MESSAGE, content);
    }
    
    private Message createNameChangeMsg(String newName) {
        return new Message(null, MsgType.USER_NAME_CHANGE, newName);
    }
    
    private Message createListUsersMsg() {
        return new Message(null, MsgType.USER_LIST, null);
    }
    
    private Message changeRoomMsg(String roomId) {
        return new Message(null, MsgType.CHANGE_ROOM, roomId);
    }
    
    private Message createListRoomsMsg() {
        return new Message(null, MsgType.ROOM_LIST, null);
    }
    
    private Message createStandardMsg(String content) {
        return new Message(null, MsgType.MESSAGE, content);
    }
    private Message createAllUsersListRoomsMsg(){
        return new Message(null, MsgType.ALL_USERS_LIST, null);
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(KomFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(KomFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(KomFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(KomFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new KomFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private komunikator.ClientsListComponent clientsListComponent;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JLabel jUserCounterObviously;
    private komunikator.RoomListComponent roomListComponent1;
    // End of variables declaration//GEN-END:variables
}
