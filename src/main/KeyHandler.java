package main;

import entity.Agent;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {

    GamePanel gp;
    public boolean upPressed,downPressed,leftPressed,rightPressed;

    public KeyHandler(GamePanel gp) {
        this.gp = gp;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

        int code = e.getKeyCode();

        if(gp.gameState == gp.playState) playState(code);
        else if(gp.gameState == gp.pauseState) pauseState(code);
    }

    public void playState(int code) {

        if(code == KeyEvent.VK_W){
            upPressed = true;
        }
        if(code == KeyEvent.VK_S){
            downPressed = true;
        }
        if(code == KeyEvent.VK_A){
            leftPressed = true;
        }
        if(code == KeyEvent.VK_D){
            rightPressed = true;
        }
        if(code == KeyEvent.VK_P) {
            gp.gameState = gp.pauseState;
        }
    }

    public void pauseState(int code) {
        if(code == KeyEvent.VK_W){
            gp.ui.commandNum--;
            if(gp.ui.commandNum < 0) gp.ui.commandNum = 2;
        }
        if(code == KeyEvent.VK_S){
            gp.ui.commandNum++;
            if(gp.ui.commandNum > 2) gp.ui.commandNum = 0;
        }
        if(code == KeyEvent.VK_ENTER){
            switch (gp.ui.commandNum) {
                case 0 -> Agent.agentNum++;
                case 1 -> {
                    if(Agent.agentNum > 0) Agent.agentNum--;
                }
                case 2 -> System.exit(0);
            }
        }
        if(code == KeyEvent.VK_P) {
            gp.gameState = gp.playState;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();
        if(code == KeyEvent.VK_W){
            upPressed = false;
        }
        if(code == KeyEvent.VK_S){
            downPressed = false;
        }
        if(code == KeyEvent.VK_A){
            leftPressed = false;
        }
        if(code == KeyEvent.VK_D){
            rightPressed = false;
        }
    }
}