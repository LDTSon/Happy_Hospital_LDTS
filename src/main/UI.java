package main;

import entity.Agent;
import entity.Agv;

import javax.swing.*;
import javax.swing.text.Document;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.DecimalFormat;

public class UI {
    GamePanel gp;
    Graphics2D g2;
    Font arial_40, arial_80B;
    public boolean messageOn = false;
    public String message = "";
    int messageCounter = 0;
    public boolean gameFinish = false;
    double playTime;
    DecimalFormat dFormat = new DecimalFormat("#0.00");
    public UI(GamePanel  gp){
        this.gp = gp;
        arial_80B = new Font("Arial",Font.BOLD,80);
        arial_40 = new Font("Arial",Font.PLAIN,40);
    }
    public void showMessage(String text){
        message = text;
        messageOn = true;

    }
    public void draw(Graphics2D g2){

        this.g2 = g2;

        g2.setFont(arial_80B);
        g2.setColor(Color.RED);
        //PLAYSTATE
        if(gp.gameState == gp.playState) {
            drawAGVScreen();
        }
        //PAUSESTATE
        if(gp.gameState == gp.pauseState) {
            drawPauseScreen();
        }
    }

    private void drawPauseScreen() {
        String text = "Tạm dừng";
        int x = getXForCenteredText(text);
        int y = gp.screenHeight/2;

        g2.drawString(text, x, y);
    }
    private void drawAGVScreen(){
        //FRAME
        final int frameX = gp.tileSize*52;
        final int frameY = 0;

        //INPUT_NUM_OF_AGV
        JTextField numOfAGV = new JTextField(4);
        numOfAGV.setFont(new Font("Times New Roman", Font.PLAIN, 20));
        numOfAGV.setBounds(32*52, 32*7, 32*5, 32*2);
        numOfAGV.setBackground(Color.WHITE);
        gp.add(numOfAGV);

        //OUTPUT_AGV_DEADLINES
        JTextArea AGVDeadlines = new JTextArea(10000, 1);
        AGVDeadlines.setFont(new Font("Times New Roman", Font.PLAIN, 15));
        AGVDeadlines.setBounds(32*52, 32*16, 32*8, 32*10);

        //BUTTON
        JButton jb1 = new JButton("Save Data");
        jb1.setBounds(32*54, 32, 32*4,32);
        jb1.setFont(new Font("Times New Roman", Font.BOLD, 20));
        jb1.setBackground(Color.white);
        jb1.setForeground(Color.black);
        gp.add(jb1);

        JButton jb2 = new JButton("Load Data");
        jb2.setBounds(32*54, 32*3, 32*4,32);
        jb2.setFont(new Font("Times New Roman", Font.BOLD, 20));
        jb2.setBackground(Color.white);
        jb2.setForeground(Color.black);
        gp.add(jb2);

        JButton jb3 = new JButton("Apply");
        jb3.setBounds(32*57, 32*7, 32*3, 32*2);
        jb3.setFont(new Font("Times New Roman", Font.BOLD, 20));
        jb3.setBackground(Color.white);
        jb3.setForeground(Color.black);
        jb3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Agent.agentNum = Integer.parseInt(numOfAGV.getText());
                gp.requestFocusInWindow();
            }
        });
        gp.add(jb3);

        JButton jb4 = new JButton("Hướng dẫn");
        jb4.setBounds(32*54, 32*26, 32*4, 32*2);
        jb4.setFont(new Font("Times New Roman", Font.PLAIN, 18));
        jb4.setBackground(Color.white);
        jb4.setForeground(Color.black);
        jb4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openWebpage(URI.create("https://github.com/phamtuanhien/Project20211_HappyHospital"));
            }
        });
        gp.add(jb4);


        //TEXT_ATTRIBUTE
        g2.setColor(Color.RED);
        g2.setFont(g2.getFont().deriveFont(22F));

        //TEXT
        int textX = frameX + gp.tileSize;
        int textY = frameY + gp.tileSize*6;
        g2.drawString("Number of Agents:", textX, textY);
        textX += gp.tileSize;
        textY += gp.tileSize*5;

        g2.drawString(Timer.getFormattedTime(), textX, textY);//TIMER
        textX -= gp.tileSize;
        textY += gp.tileSize*2;
        g2.drawString("H.NESS IS HERE!", textX, textY);//H.NESS
        textY += gp.tileSize*2;
        g2.drawString("AGV Deadlines", textX, textY);//AGV_DEADLINES_TABLE
        gp.add(AGVDeadlines);
    }

    private int getXForCenteredText(String text) {

        int length = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        return gp.screenWidth/2 - length/2;
    }

    public static boolean openWebpage(URI uri) {
        Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
        if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
            try {
                desktop.browse(uri);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }


}
