package entity;


import main.GamePanel;
import main.KeyHandler;
import main.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

    public class Agv extends Entity {
        public boolean isImmortal=false; // biến cần cho xử lý overlap =))
        private boolean isDisable = false; // biến cần cho xử lý overlap =))
        private int desX;
        private int desY;
        Font arial_17;
        GamePanel gp;
        KeyHandler keyH;
        public int hasKey = 0;

        public Agv(GamePanel gp, KeyHandler keyH){
            this.gp = gp;
            this.keyH = keyH;
            solidArea = new Rectangle(8,8,16,16);
            solidAreaDefaultX = solidArea.x;
            solidAreaDefaultY = solidArea.y;
            setDefaultValues();
            getPlayerImage();
            arial_17 = new Font("Arial",Font.PLAIN,17);

        }
        public void setDefaultValues(){
            worldX = 1*gp.tileSize;
            worldY = 14*gp.tileSize;
            speed = 4;
            //direction="down";
        }
        public void getPlayerImage(){

            entityImage = setup("agv");

        }

        public BufferedImage setup(String imageName) {

            UtilityTool uTool = new UtilityTool();
            BufferedImage image = null;

            try{
                image = ImageIO.read(getClass().getResourceAsStream("/res/" + imageName + ".png"));
                image = uTool.scaleImage(image, gp.tileSize, gp.tileSize);
                //tile[index].collision = collision;

            } catch (IOException e) {
                e.printStackTrace();
            }

            return image;
        }
        public void ToastInvalidMove(Graphics2D g2){
            String message="Di chuyển không hợp lệ!";
            g2.setFont(arial_17);
            g2.drawString(message,gp.tileSize*25,gp.tileSize*20);
        }
        public void ToastOverLay(Graphics2D g2){
            String message="AGV va chạm với Agent!";
            g2.setFont(arial_17);
            g2.drawString(message,gp.tileSize*25,gp.tileSize*20);
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
                //CHECK OBJECT COLLISION
                //IF COLLISION IS TRUE -> PLAYER CAN'T MOVE
                if(collisionOn == false){
                    switch (direction){
                        case "up":
                            worldY -= speed;
                            break;
                        case "down":
                            worldY += speed;
                            break;
                        case "left":
                            worldX -= speed;
                            break;
                        case "right":
                            worldX += speed;
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
            int textLength = (int)g2.getFontMetrics().getStringBounds(text,g2).getWidth();
            int x = worldX+16-textLength/2;
            int y = worldY-6;
            g2.drawString(text,x,y);
            g2.drawImage(entityImage,worldX,worldY,32,32,null);

        }
    }
