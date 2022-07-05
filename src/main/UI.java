package main;

import entity.Agent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URI;

public class UI {
    GamePanel gp;
    Graphics2D g2;
    Font arial_28B,arial_80B;
    public int commandNum = 0;
//    double playTime;
//    DecimalFormat dFormat = new DecimalFormat("#0.00");
    public UI(GamePanel  gp){
        this.gp = gp;

        arial_80B = new Font("Arial",Font.BOLD,80);
        arial_28B = new Font("Arial",Font.BOLD,28);
    }

    public void draw(Graphics2D g2){

        this.g2 = g2;

        g2.setFont(arial_80B);
        g2.setColor(Color.RED);

        if(gp.gameState == gp.playState) {

        }
        if(gp.gameState == gp.pauseState) {
            drawPauseScreen();
        }

        if(gp.gameState == gp.endState) {
            System.out.println("what???");
            drawEndScreen();
        }
    }

    private void drawEndScreen() {

        System.out.println("draw end screen");

        g2.setColor(Color.RED);
        g2.setFont(arial_80B);
        g2.drawString("GAME ENDED", getXForCenteredText("GAME ENDED"), gp.tileSize*12);

        if(gp.player.goalReached == true) {
            g2.drawString("FINISHED :)", getXForCenteredText("FINISHED :)"), gp.tileSize*15);
        } else {
            g2.drawString("UNFINISHED :(", getXForCenteredText("UNFINISHED :("), gp.tileSize*15);
        }
    }

    private void drawPauseScreen() {

        //CREATE A FRAME
        final int frameX = gp.tileSize*18;
        final int frameY = gp.tileSize*5;
        final int frameWidth = gp.tileSize*16;
        final int frameHeight = gp.tileSize*18;
        drawSubWindow(frameX, frameY, frameWidth, frameHeight);

        //TEXT

        g2.setColor(Color.RED);
        g2.setFont(arial_80B);
        g2.drawString("PAUSED", getXForCenteredText("PAUSED"), gp.tileSize*8);

        g2.setColor(Color.white);
        g2.setFont(arial_28B);
        int x = frameX + 50;
        int y = frameY + gp.tileSize*5;
        final int lineHeight = 40;

        String agentNum = String.valueOf(Agent.agentNum);

        //NAMES
        g2.drawString("Number of agents: " + agentNum, x, y);
        y += lineHeight;



        g2.drawString("Increase number of agents by 1", x, y);
        if(commandNum == 0) g2.drawString(">", x - gp.tileSize, y);
        y += lineHeight;

        g2.drawString("Decrease number of agents by 1", x, y);
        if(commandNum == 1) g2.drawString(">", x - gp.tileSize, y);
        y += lineHeight;

        g2.drawString("Game instruction", x, y);
        if(commandNum == 2) g2.drawString(">", x - gp.tileSize, y);
        y += lineHeight;

        g2.drawString("Quit game ", x, y);
        if(commandNum == 3) g2.drawString(">", x - gp.tileSize, y);
        y += lineHeight;

        //VALUES
        int tailX = (frameX + frameWidth) -30;
        //Reset textY
        y = frameY + gp.tileSize*3;
    }

    public void drawSubWindow(int x, int y, int width, int height) {

        Color c = new Color(0, 0, 0, 210);
        g2.setColor(c);
        g2.fillRoundRect(x, y, width, height, 35, 35);

        c = new Color(255, 255, 255);
        g2.setColor(c);
        g2.setStroke(new BasicStroke(5));
        g2.drawRoundRect(x+5, y+5, width-10, height-10, 25, 25);
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
