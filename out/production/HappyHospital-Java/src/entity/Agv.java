package entity;


import gameAlgo.Position;
import main.GamePanel;
import main.KeyHandler;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

public class Agv extends Entity {

        public BufferedImage agvImage[] = new BufferedImage[4];
        public boolean isValidDirection = true;
        private boolean isImmortal = false; // biến cần cho xử lý overlap =))
        private boolean isDisable = false; // biến cần cho xử lý overlap =))
        private Position goalPos;
        public boolean goalReached;
        private Text goalText = new Text();
        Font arial_17;
        Font arial_30;
        KeyHandler keyH;


        public Agv(GamePanel gp, KeyHandler keyH){
            super(gp);
            this.keyH = keyH;
            setDefaultValues();
            getPlayerImage();
        }
        public void setDefaultValues(){
            x = 2*gp.tileSize;
            y = 13*gp.tileSize;
            speed = 1;
            direction = "right";

            entityText.text = "AGV";
            entityText.textLength = entityText.getTextLength();
            entityText.x = this.x + 12 - entityText.textLength/2;
            entityText.y = this.y - 6;
            arial_17 = new Font("Arial", Font.BOLD,17);
            arial_30 = new Font("Arial", Font.BOLD,30);

            solidArea = new Rectangle(8,8,16,16);
            solidAreaDefaultX = solidArea.x;
            solidAreaDefaultY = solidArea.y;

            Random random = new Random();
            int randomGoal = random.nextInt(gp.desPos.size());
            this.goalPos = gp.desPos.get(randomGoal);
            goalText.text = "DES";
            goalText.x = goalPos.x + 11;
            goalText.y = goalPos.y + 16;
        }
        public void getPlayerImage(){

            agvImage[0] = setup("agvRed");
            agvImage[1] = setup("agvGreen");
            agvImage[2] = setup("agvYellow");
            agvImage[3] = setup("agvBlue");
        }

        public void toastInvalidMove(Graphics2D g2){
            g2.setColor(Color.RED);
            String message = "Di chuyển không hợp lệ!";
            g2.setFont(arial_30);
            g2.drawString(message,gp.tileSize*20,gp.tileSize*10);
        }

        public void toastOverLay(Graphics2D g2){
            g2.setColor(Color.RED);
            String message = "AGV va chạm với Agent!";
            g2.setFont(arial_30);
            g2.drawString(message,gp.tileSize*20,gp.tileSize*14);
        }

        public void checkDirection() {
            int midX = x + gp.tileSize/2 ;
            int midY = y + gp.tileSize/2 ;
            //System.out.println(x +" "+ y);
            midX = midX / gp.tileSize;
            midY = midY / gp.tileSize;

            int tileNum = gp.tileM.mapTileNum[midX][midY];
            //System.out.println(tileNum);
            String d = gp.tileM.tile[tileNum].tileDirection;

            switch (d) {
                case "left":
                    if (direction.equals("right")) isValidDirection = false;
                    break;
                case "right":
                    if (direction.equals("left")) isValidDirection = false;
                    break;
                case "up":
                    if (direction.equals("down")) isValidDirection = false;
                    break;
                case "down":
                    if (direction.equals("up")) isValidDirection = false;
                    break;
            }
        }
        public void update(){

            if(this.isDisable) return;

            //CHECK AGENT COLLISION
            if(justCollided) handleOverlap();

            if(keyH.leftPressed || keyH.downPressed || keyH.upPressed || keyH.rightPressed){
                if(keyH.upPressed){
                    direction="up";
                }
                else if(keyH.downPressed){
                    direction="down";
                }
                else if(keyH.rightPressed){
                    direction="right";
                }
                else if(keyH.leftPressed){
                    direction="left";
                }

                //CHECK TILE COLLISION
                collisionOn = false;
                gp.cChecker.checkTile(this);
                //CHECK DIRECTION
                isValidDirection = true;
                checkDirection();

                //IF COLLISION IS TRUE -> PLAYER CAN'T MOVE
                if(!collisionOn && isValidDirection) {
                    switch (direction) {
                        case "up" -> y -= speed;
                        case "down" -> y += speed;
                        case "left" -> x -= speed;
                        case "right" -> x += speed;
                    }
                }

                //CHECK IF AGV TOUCH GOAL
                if(goalPos.x - 4 <= x && x <= goalPos.x + 4 &&
                   goalPos.y - 4 <= y && y <= goalPos.y + 4) {
                    goalReached = true;
                    this.speed = 0;
                    setTimeout(()->{
                        this.speed = 1;
                        this.goalText = null;}, 1000);
                }

                //CHECK IF AGV REACHED END POS
                if(this.x >= 50*gp.tileSize) gp.gameState = gp.endState;

                //UPDATE TEXT
                entityText.x = this.x + 12 - entityText.textLength/2;
                entityText.y = this.y - 6;
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
        public void handleOverlap(){

            if(!this.isImmortal){
                this.isDisable = true;
                setTimeout(()->{
                    this.isImmortal = true;
                    this.isDisable = false;
                    setTimeout(()-> {this.isImmortal = false;}, 2000);}, 1000);
            }
            justCollided = false;
        }

        public void draw(Graphics2D g2){

            //AGV TEXT
            g2.setFont(arial_17);
            g2.setColor(Color.green);
            g2.drawString(entityText.text, entityText.x, entityText.y);
            if(goalText != null) {
                g2.setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 17));
                g2.drawString(goalText.text, goalText.x, goalText.y);
            }

            //AGV
            if(this.isDisable) toastOverLay(g2);
            if(!isValidDirection) toastInvalidMove(g2);
            g2.drawImage(entityImage, this.x, this.y,gp.tileSize,gp.tileSize,null);
        }
    }
