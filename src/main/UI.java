package main;

import entity.Agent;

import java.awt.*;
import java.net.URI;
import java.text.DecimalFormat;

public class UI {
    GamePanel gp;
    Graphics2D g2;
    Font arial_28B, arial_80B;
    public int commandNum = 0;
    public int titleScreenState = 0;
    public boolean showPlayerStatus = false;
    public DecimalFormat decimalFormat = new DecimalFormat("###,###,###.###");
    public UI(GamePanel  gp) {
        this.gp = gp;
        arial_28B = new Font("Arial", Font.BOLD, 28);
        arial_80B = new Font("Arial", Font.BOLD, 80);
    }

    public void draw(Graphics2D g2){

        this.g2 = g2;

        g2.setFont(arial_80B);
        g2.setColor(Color.RED);

        if(gp.gameState == gp.titleState) {
            drawTitleScreen();
        }

        if(gp.gameState == gp.playState) {
            drawPlayState();
        }
        if(gp.gameState == gp.pauseState) {
            drawPauseScreen();
        }

        if(gp.gameState == gp.endState) {
            drawEndScreen();
        }
    }

    private void drawPlayState() {
        if(showPlayerStatus) {
            g2.setFont(arial_28B);
            g2.drawString("Time: " + Timer.getFormattedTime(gp.timer.secondsSinceStart), gp.tileSize*6, gp.tileSize*4);
            g2.drawString("Harmfulness: " + decimalFormat.format(gp.player.harmfulness), gp.tileSize*6, gp.tileSize*5);
        }
    }

    private void drawTitleScreen() {

        if(titleScreenState == 0) {
            //TITLE NAME
            g2.setFont(arial_80B);
            String text = "Happy Hospital";
            int x = getXForCenteredText(text);
            int y = gp.tileSize * 12;

            //SHADOW
            g2.setColor(Color.gray);
            g2.drawString(text, x+5, y+5);
            //MAIN COLOR
            g2.setColor(Color.white);
            g2.drawString(text, x, y);

            //MENU
            g2.setFont(arial_28B);
            y += gp.tileSize*4;

            text = "NEW GAME";
            x = getXForCenteredText(text);
            y += gp.tileSize*2;
            g2.drawString(text, x, y);
            if(commandNum == 0) g2.drawString(">", x - gp.tileSize, y);

            text = "LOAD GAME";
            x = getXForCenteredText(text);
            y += gp.tileSize*2;
            g2.drawString(text, x, y);
            if(commandNum == 1) g2.drawString(">", x - gp.tileSize, y);

            text = "QUIT";
            x = getXForCenteredText(text);
            y += gp.tileSize*2;
            g2.drawString(text, x, y);
            if(commandNum == 2) g2.drawString(">", x - gp.tileSize, y);
        } else if(titleScreenState == 1) {

            //CHARACTER SELECTION SCREEN
            g2.setColor(Color.white);
            g2.setFont(arial_80B);

            String text = "Select your character!";
            int x = getXForCenteredText(text);
            int y = gp.tileSize * 8;
            g2.drawString(text, x, y);

            g2.setFont(arial_28B);
            y += gp.tileSize*12;

            text = "Red";
            x = gp.tileSize*11;
            g2.drawString(text, x, y);
            g2.drawImage(gp.player.agvImage[0], x-gp.tileSize*2, y-gp.tileSize*8, gp.tileSize*6, gp.tileSize*6, null);
            if(commandNum == 0) g2.drawString(">", x - gp.tileSize, y);

            text = "Green";
            x += gp.tileSize*10;
            g2.drawString(text, x, y);
            g2.drawImage(gp.player.agvImage[1], x-gp.tileSize*2, y-gp.tileSize*8, gp.tileSize*6, gp.tileSize*6, null);
            if(commandNum == 1) g2.drawString(">", x - gp.tileSize, y);

            text = "Yellow";
            x += gp.tileSize*10;
            g2.drawString(text, x, y);
            g2.drawImage(gp.player.agvImage[2], x-gp.tileSize*2, y-gp.tileSize*8, gp.tileSize*6, gp.tileSize*6, null);
            if(commandNum == 2) g2.drawString(">", x - gp.tileSize, y);

            text = "Blue";
            x += gp.tileSize*10;
            g2.drawString(text, x, y);
            g2.drawImage(gp.player.agvImage[3], x-gp.tileSize*2, y-gp.tileSize*8, gp.tileSize*6, gp.tileSize*6, null);
            if(commandNum == 3) g2.drawString(">", x - gp.tileSize, y);

            text = "Back";
            x = getXForCenteredText(text);
            y += gp.tileSize*4;
            g2.drawString(text, x, y);
            if(commandNum == 4) g2.drawString(">", x - gp.tileSize, y);
        }

    }

    private void drawEndScreen() {

        g2.setColor(Color.RED);
        g2.setFont(arial_80B);

        if(gp.player.goalReached > 0) {
            g2.drawString("GAME ENDED", getXForCenteredText("GAME ENDED"), gp.tileSize*10);
            g2.drawString("FINISHED :)", getXForCenteredText("FINISHED :)"), gp.tileSize*12);
            String text = "Time: " + Timer.getFormattedTime(gp.timer.secondsSinceStart);
            g2.drawString(text, getXForCenteredText(text), gp.tileSize*14);
            text = "Harmfulness: " + decimalFormat.format(gp.player.harmfulness);
            g2.drawString(text, getXForCenteredText(text), gp.tileSize*16);
            text = "Number of goal reached: " + gp.player.goalReached;
            g2.drawString(text, getXForCenteredText(text), gp.tileSize*18);
        } else {
            g2.drawString("GAME ENDED", getXForCenteredText("GAME ENDED"), gp.tileSize*12);
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
