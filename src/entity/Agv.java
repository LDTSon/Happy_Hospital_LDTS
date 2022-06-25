package entity;


import main.GamePanel;
import main.KeyHandler;

import java.awt.*;

public class Agv extends Entity {
        public boolean isValidDirection = true;
        private boolean isImmortal = false; // biến cần cho xử lý overlap =))
        private boolean isDisable = false; // biến cần cho xử lý overlap =))
        private int desX;
        private int desY;
        Font arial_17;
        Font arial_30;
        KeyHandler keyH;
        public final int screenX;
        public final int screenY;


        public Agv(GamePanel gp, KeyHandler keyH){

            super(gp);
            this.keyH = keyH;
            solidArea = new Rectangle(8,8,16,16);
            solidAreaDefaultX = solidArea.x;
            solidAreaDefaultY = solidArea.y;
            setDefaultValues();
            getPlayerImage();
            screenX = gp.screenWidth/2 -gp.tileSize/2;
            screenY = gp.screenHeight/2-gp.tileSize/2;
            arial_17 = new Font("Arial",Font.TYPE1_FONT,17);
            arial_30 = new Font("Arial",Font.TYPE1_FONT,30);
        }
        public void setDefaultValues(){
            x = 2*gp.tileSize;
            y = 13*gp.tileSize;
            speed = 1;
            direction = "right";
        }
        public void getPlayerImage(){

            entityImage = setup("agv");
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

            int tileNum = gp.TileM.mapTileNum[midY][midX];
            //System.out.println(tileNum);
            String d = gp.TileM.tile[tileNum].tileDirection;

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

//                //CHECK TILE COLLISION
                collisionOn = false;
                gp.cChecker.checkTile(this);

                //CHECK DIRECTION
                isValidDirection = true;
                checkDirection();

                //CHECK OBJECT COLLISION


                //IF COLLISION IS TRUE -> PLAYER CAN'T MOVE
                if(!collisionOn && isValidDirection) {
                    switch (direction) {
                        case "up" -> y -= speed;
                        case "down" -> y += speed;
                        case "left" -> x -= speed;
                        case "right" -> x += speed;
                    }
                }

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
            System.out.println("va cham agent");
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
/*          g2.setColor(Color.white);
            g2.fillRect(x,y,gp.tileSize,gp.tileSize);*/
            g2.setFont(arial_17);
            g2.setColor(Color.green);
            String text="AGV";
            int textLength = (int)g2.getFontMetrics().getStringBounds(text,g2).getWidth();
            int x = this.x + 12 - textLength/2;
            int y = this.y - 6;
            g2.drawString(text, x, y);
            //System.out.println(this.isDisable);
            if(this.isDisable) toastOverLay(g2);
            if(!isValidDirection) toastInvalidMove(g2);
            g2.drawImage(entityImage, this.x, this.y,gp.tileSize,gp.tileSize,null);
        }
    }
