/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package komunikator;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import komunikator.messages.Message;
import komunikator.messages.MsgType;

/**
 *
 * @author Krzyś
 */
public class RoomListComponent extends JComponent implements MouseListener,MouseMotionListener {
    public static final int WIDTH = 100;
    public static final int HEIGHT = 100;
    private BufferedImage img;
    
    private int mouseX;
    private int mouseY;
    private int roomButtonHeight;
    private int currentHighlighted;
    
    private ObjectOutputStream oos;
    private JScrollPane sp;
    
    
    private String[] roomNames;
    private String[] roomIds;
    
    public RoomListComponent(){
        super();
        setPreferredSize(new Dimension(WIDTH,HEIGHT));
        setFocusable(true);
        addMouseMotionListener(this);
        addMouseListener(this);
        img=new BufferedImage(200,200,BufferedImage.TYPE_INT_RGB);
        Graphics g=img.getGraphics();
        g.setColor(new Color(240,240,240));
        g.fillRect(0, 0, img.getWidth(), img.getHeight());
        currentHighlighted=0;
        roomButtonHeight=30;
        
    }
    
    public void setObjectOutputStream(ObjectOutputStream oos){
        this.oos = oos;
    }
    
    public void setScrollignPane(JScrollPane sp){
        this.sp = sp;
    }
    
    public void paint (Graphics g){
         g.drawImage(img.getScaledInstance(getWidth(), getHeight(),0), 0, 0, null);
        
        currentHighlighted = mouseY/roomButtonHeight;
        
        if(roomNames!=null){
             for(int i=0;i<roomNames.length;i++){
                 if(i%2==0)
                     if(i==currentHighlighted)
                        g.setColor(new Color(140,140,150));
                     else
                        g.setColor(new Color(130,130,150)); 
                 else
                     if(i==currentHighlighted)
                        g.setColor(new Color(120,120,140));
                     else
                        g.setColor(new Color(110,110,130)); 
                 g.fillRect(0, i*roomButtonHeight, this.getWidth(), roomButtonHeight);
                 g.setColor(new Color(255,255,255));
                 g.drawString(roomNames[i],0, ((i+1)*roomButtonHeight)-roomButtonHeight/2);
             }
        }
         
    }
    
    public void setData(String[] roomNames,String[] roomIds){
        this.roomIds=roomIds;
        this.roomNames=roomNames;
        repaint();
        this.setPreferredSize(new Dimension(this.getWidth(),roomButtonHeight*roomIds.length));
        if(sp!=null){
            System.out.println("asd");
            sp.revalidate();
            sp.repaint();
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        
    }

    @Override
    public void mouseMoved(MouseEvent e) {
       mouseX= e.getX();
       mouseY= e.getY();
       repaint();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if(roomIds!=null && currentHighlighted<roomIds.length){
            //System.out.println(roomIds[currentHighlighted]);
            if(oos!=null){
                try {
                    oos.writeObject(new Message(null,MsgType.CHANGE_ROOM,roomIds[currentHighlighted]));
                } catch (IOException ex) {
                    Logger.getLogger(RoomListComponent.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
        }
        
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
    
    
}
