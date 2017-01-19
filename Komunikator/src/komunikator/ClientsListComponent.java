/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package komunikator;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.concurrent.locks.ReentrantLock;
import javax.swing.JComponent;
import javax.swing.JScrollPane;

/**
 *
 * @author Krzyś
 */
public class ClientsListComponent extends JComponent implements MouseListener,MouseMotionListener {
    public static final int WIDTH = 100;
    public static final int HEIGHT = 100;
    public final ReentrantLock clientsLock;
    private BufferedImage img;
    
    private int mouseX;
    private int mouseY;
    private int roomButtonHeight;
    private int currentHighlighted;
    private int currentChoosed;
    
    private JScrollPane sp;
    
    
    private String[] clientsNames;
    private String[] clientsIds;
    
    public ClientsListComponent(){
        super();
        clientsLock = new ReentrantLock();
        setPreferredSize(new Dimension(WIDTH,HEIGHT));
        setFocusable(true);
        addMouseMotionListener(this);
        addMouseListener(this);
        img=new BufferedImage(200,200,BufferedImage.TYPE_INT_RGB);
        Graphics g=img.getGraphics();
        g.setColor(new Color(240,240,240));
        g.fillRect(0, 0, img.getWidth(), img.getHeight());
        currentHighlighted=0;
        currentChoosed=-1;
        roomButtonHeight=30;
        
    }
    
    
    public void setScrollignPane(JScrollPane sp){
        this.sp = sp;
    }
    
    public void paint (Graphics g){
         g.drawImage(img.getScaledInstance(getWidth(), getHeight(),0), 0, 0, null);
        
        currentHighlighted = mouseY/roomButtonHeight;
        
        clientsLock.lock();
        try {
            if(clientsNames!=null){
                for(int i=0;i<clientsNames.length;i++){
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
                    if(i==currentChoosed)
                        g.setColor(new Color(50,160,50));
                    g.fillRect(0, i*roomButtonHeight, this.getWidth(), roomButtonHeight);
                    g.setColor(new Color(255,255,255));
                    g.drawString(clientsNames[i],0, ((i+1)*roomButtonHeight)-roomButtonHeight/2);
                }
            }
        } finally {
            clientsLock.unlock();
        }
    }
    
    public void setData(String[] clientsNames,String[] clientsIds){
        this.clientsIds=clientsIds;
        this.clientsNames=clientsNames;
        repaint();
        this.setPreferredSize(new Dimension(this.getWidth(),roomButtonHeight*clientsIds.length));
        if(sp!=null){
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
        if(clientsIds!=null && currentHighlighted<clientsIds.length){
            //System.out.println(roomIds[currentHighlighted]);
            if(currentChoosed==currentHighlighted){
                currentChoosed=-1;
            }
            else{
                currentChoosed=currentHighlighted;
            }
            repaint();
        }
    }
    
    public boolean isSomethingChoosed(){
        return (currentChoosed!=-1 && clientsIds.length>currentChoosed);
    }
    
    public String getCurrentId(){
        if(clientsIds.length>currentChoosed)
            return (clientsIds[currentChoosed]);
        return "0";
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
