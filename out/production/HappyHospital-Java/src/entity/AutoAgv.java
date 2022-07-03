package entity;

import gameAlgo.Position;
import main.GamePanel;

import java.awt.*;
import java.util.Random;

public class AutoAgv extends Entity{

        private Position startPos;
        private Position endPos;
        private int id;
        private int isOnGate=0;
        private boolean isGettingDes=false;
        public static int HandlerDeadLock=120;

     //   private int handlerStuck;

        public static int autoAgvNum = 10;

        Font arial_17 = new Font("Arial",Font.TYPE1_FONT,17);
        private Text endText = new Text();
       // public boolean isOverlap = false;


    public AutoAgv(GamePanel gp, Position startPos, Position endPos, int id) {
            super(gp);
            this.startPos = startPos;
            this.endPos = endPos;
            this.id = id;
            setDefaultValues();;
            getAutoAgvImage();
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

        public static void bornRandomAutoAgv(GamePanel gp) {
            Random random = new Random();
            int index = random.nextInt(autoAgvNum);
            Position startPoint=new Position(2*gp.tileSize,14*gp.tileSize);
            int randomEnd = random.nextInt(gp.DesPos.size());

            gp.autoAgvs.add(new AutoAgv(gp, startPoint, gp.DesPos.get(randomEnd), index));

    }

        public void setDefaultValues() {

            x = startPos.x;
            y = startPos.y;
            direction = "right";
            speed = 1;

            this.solidArea = new Rectangle(4, 4, 24, 24);
            solidAreaDefaultX = solidArea.x;
            solidAreaDefaultY = solidArea.y;

            endText.text = "DES";
            endText.textLength = endText.getTextLength();
            endText.x = endPos.x + 11;
            endText.y = endPos.y + 16;

            onPath = true;
            //handlerStuck=0;
        }

        public void getAutoAgvImage(){

            entityImage = setup("agv");
        }

        public void setAction() {

            if(onPath == true && (isOnGate==0||isOnGate==1)) {
                int goalCol = endPos.x/gp.tileSize;
                int goalRow = endPos.y/gp.tileSize;

                searchPath(goalCol, goalRow);

                return;
            }
            else if(onPath==false && isOnGate==0){
                isGettingDes=true;
                setTimeout(()-> {
                    isGettingDes=false;
                    isOnGate++;
                    onPath=true;
                    endPos.x=50*gp.tileSize;
                    endPos.y=14*gp.tileSize;}, 2000);
            }
            else {
                eliminate(this);
            }
//        else {
//            int midX = x + 16;
//            int midY = y + 16;
//            int midEndX = endPos.x + 16;
//            int midEndY = endPos.y + 16;
//
//            if(midX <= midEndX - 16) direction = "right";
//            else if(midX >= midEndX + 16) direction = "left";
//            else if(midY <= midEndY - 16) direction = "down";
//            else if(midY >= midEndY + 16) direction = "up";
//            else eliminate(this);
//        }
        }

        public void eliminate(AutoAgv autoAgv) {
            gp.autoAgvs.remove(autoAgv);
            if(gp.autoAgvs.size() < autoAgvNum) {
                Random random = new Random();
                int index = random.nextInt(autoAgvNum);
                bornRandomAutoAgv(gp);
            }
        }

        /*public static void setTimeout(Runnable runnable, int delay){
            new Thread(() -> {
                try {
                    Thread.sleep(delay);
                    runnable.run();
                }
                catch (Exception e){
                    System.err.println(e);
                }
            }).start();
        }*/

        /*public void handleOverlap() {
            if(this.isOverlap) return;
            this.isOverlap = true;
            setTimeout(() -> this.isOverlap = false, 4000);
            justCollided = false;
        }*/

        public void update() {

            if(isGettingDes==true) return;
            //if(this.isOverlap) return;
            // HANDLER AUTOAGV COLLISION
            gp.cChecker.checkAutoAgv(this);
            if(checkAutoAgvMove==true ){
                setTimeout(()-> {
                    checkAutoAgvMove=false;
                }, 1);
            }
            if(checkAutoAgvMove==true) return;

            setAction();

            collisionOn = false;
           // gp.cChecker.checkTile(this);
           // gp.cChecker.checkPlayer(this);
           // if(justCollided) handleOverlap();

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
            g2.setFont(arial_17);
            g2.setColor(Color.red);
            //DRAW DES TEXT
            if(isOnGate==0)
                g2.drawString(endText.text, endText.x, endText.y);
            //DRAW AUTOAGV
            g2.drawImage(entityImage, x, y, gp.tileSize, gp.tileSize, null);
        }
}
