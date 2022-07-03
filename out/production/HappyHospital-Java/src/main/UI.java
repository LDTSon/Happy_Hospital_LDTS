package main;

import entity.Agent;

import java.awt.*;
import java.text.DecimalFormat;

public class UI {
    GamePanel gp;
    Graphics2D g2;
    Font arial_28,arial_80B;
    public boolean messageOn = false;
    public String message = "";
    int messageCounter = 0;
    public boolean gameFinish = false;
    public int commandNum = 0;
    double playTime;
    DecimalFormat dFormat = new DecimalFormat("#0.00");
    public UI(GamePanel  gp){
        this.gp = gp;
        arial_80B = new Font("Arial",Font.BOLD,80);
        arial_28 = new Font("Arial",Font.PLAIN,28);
    }
    public void showMessage(String text){
        message = text;
        messageOn = true;

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
    }

    private void drawPauseScreen() {
//        String text = "Tạm dừng";
//        int x = getXForCenteredText(text);
//        int y = gp.screenHeight/2;
//
//        g2.drawString(text, x, y);

        //CREATE A FRAME
        final int frameX = gp.tileSize*18;
        final int frameY = gp.tileSize*5;
        final int frameWidth = gp.tileSize*16;
        final int frameHeight = gp.tileSize*18;
        drawSubWindow(frameX, frameY, frameWidth, frameHeight);

        //TEXT

        g2.setColor(Color.RED);
        g2.setFont(arial_80B);
        g2.setStroke(new BasicStroke(5));
        g2.drawString("PAUSED", getXForCenteredText("PAUSED"), gp.tileSize*8);

        g2.setColor(Color.white);
        g2.setFont(arial_28);
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

        g2.drawString("Quit game ", x, y);
        if(commandNum == 2) g2.drawString(">", x - gp.tileSize, y);
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
}
