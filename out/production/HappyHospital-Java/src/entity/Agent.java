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

    public static int agentNum = 100;

    Font arial_17 = new Font("Arial",Font.TYPE1_FONT,17);
    private Text endText = new Text();
    public boolean isOverlap = false;


    public Agent(GamePanel gp, Position startPos, Position endPos, int id) {
        super(gp);
        this.startPos = startPos;
        this.endPos = endPos;
        this.id = id;
        setDefaultValues();;
        getAgentImage();
    }

    public static void bornRandomAgent(GamePanel gp) {
        Random random = new Random();
        int index = random.nextInt(agentNum);
        int randomStart = random.nextInt(gp.doorPos.size());
        int randomEnd;
        do {
            randomEnd = random.nextInt(gp.doorPos.size());
        } while(randomStart == randomEnd);

        gp.agent.add(new Agent(gp, gp.doorPos.get(randomStart), gp.doorPos.get(randomEnd), index));
    }

    public void setDefaultValues() {

        x = startPos.x;
        y = startPos.y;
        direction = "up";
        speed = 1;

        this.solidArea = new Rectangle(4, 4, 24, 24);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        entityText.text = String.valueOf(this.id);
        entityText.textLength = entityText.getTextLength();
        entityText.x = this.x + 11;
        entityText.y = this.y - 6;

        endText.text = String.valueOf(this.id);
        endText.textLength = endText.getTextLength();
        endText.x = endPos.x + 11;
        endText.y = endPos.y + 16;

        onPath = true;
    }

    public void getAgentImage(){

        entityImage = setup("agent");
    }

    public void setAction() {

        if(onPath == true) {
            int goalCol = endPos.x/gp.tileSize;
            int goalRow = endPos.y/gp.tileSize;

            if(!searchPath(goalCol, goalRow)) eliminate(this);
            return;
        }
        else {
            int midX = x + 16;
            int midY = y + 16;
            int midEndX = endPos.x + 16;
            int midEndY = endPos.y + 16;

            if(midX <= midEndX - 1) direction = "right";
            else if(midX >= midEndX + 1) direction = "left";
            else if(midY <= midEndY - 1) direction = "down";
            else if(midY >= midEndY + 1) direction = "up";
            else eliminate(this);
        }
    }

    public void eliminate(Agent agent) {
        gp.agent.remove(agent);
        if(gp.agent.size() < agentNum) {
            Random random = new Random();
            int index = random.nextInt(agentNum);
            bornRandomAgent(gp);
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

            //UPDATE TEXT
            entityText.x = this.x + 11;
            entityText.y = this.y - 6;
        }
    }

    public void draw(Graphics2D g2){
        g2.setFont(arial_17);
        g2.setColor(Color.red);
        //DRAW AGENT TEXT
        g2.drawString(entityText.text, entityText.x, entityText.y);
        //DRAW DES TEXT
        //System.out.println(endText.text);
        g2.drawString(endText.text, endText.x, endText.y);
        //DRAW AGENT
        g2.drawImage(entityImage, x, y, gp.tileSize, gp.tileSize, null);
    }
}
