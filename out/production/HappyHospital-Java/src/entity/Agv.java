package entity;


import main.GamePanel;
import main.KeyHandler;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;

    public class Agv extends Entity {
        public boolean isImmortal=false; // biến cần cho xử lý overlap =))
        private boolean isDisable = false; // biến cần cho xử lý overlap =))
        private int desX;
        private int desY;
        Font arial_17;
        Font arial_30;
        GamePanel gp;
        KeyHandler keyH;
        public final int screenX;
        public final int screenY;

        public Agv(GamePanel gp, KeyHandler keyH){
            this.gp=gp;
            this.keyH=keyH;
            solidArea = new Rectangle(8,8,16,16);
            solidAreaDefaultX=solidArea.x;
            solidAreaDefaultY=solidArea.y;
            setDefaultValues();
            getPlayerImage();
            screenX=gp.screenWidth/2 -gp.tileSize/2;
            screenY=gp.screenHeight/2-gp.tileSize/2;
            arial_17=new Font("Arial",Font.TYPE1_FONT,17);
            arial_30=new Font("Arial",Font.TYPE1_FONT,30);
        }
        public void setDefaultValues(){
            x =2*gp.tileSize;
            y =13*gp.tileSize;
            speed=1;
            direction="right";
        }
        public void getPlayerImage(){
            try{
                entityImage=ImageIO.read(getClass().getResourceAsStream("/res/agv.png"));
            }catch (IOException e){
                e.printStackTrace();
            }
        }

        public void ToastInvalidMove(Graphics2D g2){
            g2.setColor(Color.RED);
            String message="Di chuyển không hợp lệ!";
            g2.setFont(arial_30);
            g2.drawString(message,gp.tileSize*20,gp.tileSize*10);
        }
        public void ToastOverLay(Graphics2D g2){
            g2.setColor(Color.RED);
            String message="AGV va chạm với Agent!";
            g2.setFont(arial_30);
            g2.drawString(message,gp.tileSize*20,gp.tileSize*14);
        }
        public void checkDirection() {
            int midX = x + gp.tileSize/2 ;
            int midY = y + gp.tileSize/2 ;
            System.out.println(x +" "+ y);
            midX = midX / gp.tileSize;
            midY = midY / gp.tileSize;

            int tileNum = gp.TileM.mapTileNum[midY][midX];
            System.out.println(tileNum);
            String d = gp.TileM.tile[tileNum].tileDirection;

            switch (d) {
                case "left":
                    if (direction.equals("right")) isImmortal = true;
                    break;
                case "right":
                    if (direction.equals("left")) isImmortal = true;
                    break;
                case "up":
                    if (direction.equals("down")) isImmortal = true;
                    break;
                case "down":
                    if (direction.equals("up")) isImmortal = true;
                    break;
            }
        }
        public void update(){
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
                gp.cChecker.CheckTile(this);

                //CHECK DIRECTION
                isImmortal = false;
                checkDirection();

                //CHECK OBJECT COLLISION
                //IF COLLISION IS TRUE -> PLAYER CAN'T MOVE
                if(!collisionOn && !isImmortal) {
                    switch (direction) {
                        case "up" -> y -= speed;
                        case "down" -> y += speed;
                        case "left" -> x -= speed;
                        case "right" -> x += speed;
                    }
                }

            }
        }
        public void draw(Graphics2D g2){
/*          g2.setColor(Color.white);
            g2.fillRect(x,y,gp.tileSize,gp.tileSize);*/

            g2.setFont(arial_17);
            g2.setColor(Color.green);
            String text="AGV";
            int textLength = (int)g2.getFontMetrics().getStringBounds(text,g2).getWidth();
            int x= this.x +12-textLength/2;
            int y= this.y -6;
            g2.drawString(text,x,y);
            if(isImmortal) ToastInvalidMove(g2);
            g2.drawImage(entityImage, this.x, this.y,gp.tileSize,gp.tileSize,null);

        }
    }
