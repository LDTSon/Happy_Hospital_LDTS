package entity;

import gameAlgo.Position;
import main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.util.Random;

public class Agent extends Entity{
    private Position startPos;
    private Position endPos;
    private int id;
    public boolean isOverlap = false;


    public Agent(GamePanel gp) {
        super(gp);

        x = 4*gp.tileSize;
        y = 13*gp.tileSize;
        direction = "down";
        speed = 1;

        solidArea = new Rectangle(0, 0, 32, 32);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        getAgentImage();
    }

    public void getAgentImage(){

        entityImage = setup("agent");
    }

    public void setAction() {

        actionLockCounter++;

        if(actionLockCounter == 120) {

            Random random = new Random();
            int i = random.nextInt(100) +  1;   //pick up a number from 1 to 100

            if(i <= 25) {
                direction = "up";
            }
            if(i > 25 && i <= 50) {
                direction = "down";
            }
            if(i > 50 && i <= 75) {
                direction = "left";
            }
            if(i > 75 && i <= 100) {
                direction = "right";
            }

            actionLockCounter = 0;
        }
    }

    public static void setTimeout(Runnable runnable, int delay){
        new Thread(() -> {
            try {
                Thread.sleep(delay);
                runnable.run();
            }
            catch (Exception e){
                System.err.println(e);
            }
        }).start();
    }

    public void handleOverlap() {
        if(this.isOverlap) return;
        this.isOverlap = true;
        setTimeout(() -> this.isOverlap = false, 4000);
        justCollided = false;
    }

    public void update() {
        if(this.isOverlap) return;
        setAction();

        collisionOn = false;
        gp.cChecker.checkTile(this);
        gp.cChecker.checkPlayer(this);
        if(justCollided) handleOverlap();

        //IF COLLISION IS TRUE -> PLAYER CAN'T MOVE
        if(!collisionOn) {
            switch (direction) {
                case "up" -> y -= speed;
                case "down" -> y += speed;
                case "left" -> x -= speed;
                case "right" -> x += speed;
            }
        }
    }

    public void draw(Graphics2D g2){

        g2.drawImage(entityImage, x, y, gp.tileSize, gp.tileSize, null);

    }
}
