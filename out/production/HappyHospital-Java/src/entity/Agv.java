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
            solidArea=new Rectangle(8,8,16,16);
            solidAreaDefaultX=solidArea.x;
            solidAreaDefaultY=solidArea.y;
            setDefaultValues();
            getPlayerImage();
            screenX=gp.screenWidth/2 -gp.tileSize/2;
            screenY=gp.screenHeight/2-gp.tileSize/2;
            arial_17=new Font("Arial",Font.TYPE1_FONT,10);
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
            int midY = y +6 +gp.tileSize/2 ;
            System.out.println(x +" "+ y);
            midX = midX / gp.tileSize;
            midY = midY / gp.tileSize;

            int tileNum = gp.TileM.mapTileNum[midY][midX];
            System.out.println(tileNum);
            String d = gp.TileM.tile[tileNum].tileDirection;

            if (d.equals("left")) {
                if (direction.equals("right")) isImmortal = true;
            } else if (d.equals("right")) {
                if (direction.equals("left")) isImmortal = true;
            } else if (d.equals("up")) {
                if (direction.equals("down")) isImmortal = true;
            } else if(d.equals("down")){
                if (direction.equals("up")) isImmortal = true;
            }
            else;
        }
        public void update(){
            if(keyH.leftPressed==true || keyH.downPressed==true || keyH.upPressed==true || keyH.rightPressed==true){
                if(keyH.upPressed==true){
                    direction="up";
                }
                else if(keyH.downPressed==true){
                    direction="down";
                }
                else if(keyH.rightPressed==true){
                    direction="right";
                }
                else if(keyH.leftPressed==true){
                    direction="left";
                }
                //CHECK TILE COLLISION
                collisionOn=false;
                gp.cChecker.CheckTile(this);
                //CHECK DIRECTION
                isImmortal=false;
                checkDirection();
                //CHECK OBJECT COLLISION
                //IF COLLISION IS TRUE -> PLAYER CAN'T MOVE
                if(collisionOn==false && isImmortal==false) {
                    switch (direction) {
                        case "up":
                            y -= speed;
                            break;
                        case "down":
                            y += speed;
                            break;
                        case "left":
                            x -= speed;
                            break;
                        case "right":
                            x += speed;
                            break;
                    }
                }

            }
        }
        public void draw(Graphics2D g2){
/*            g2.setColor(Color.white);
            g2.fillRect(x,y,gp.tileSize,gp.tileSize);*/

            g2.setFont(arial_17);
            g2.setColor(Color.green);
            String text="AGV";
            int textLength=(int)g2.getFontMetrics().getStringBounds(text,g2).getWidth();
            int x= this.x +12-textLength/2;
            int y= this.y -6;
            g2.drawString(text,x,y);
            if(isImmortal==true) ToastInvalidMove(g2);
            g2.drawImage(entityImage, this.x, this.y,gp.tileSize,gp.tileSize,null);

        }
    }
