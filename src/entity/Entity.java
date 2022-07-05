package entity;
import main.GamePanel;
import main.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.lang.String;
public class Entity {

    static GamePanel gp;
    public int x, y;
    public int speed;
    public static int  _id = 1;
    public final int agvID;
    public BufferedImage entityImage;
    public String direction;

    public Rectangle solidArea;
    public boolean collisionOn = false;
    public boolean justCollided = false;

    public int solidAreaDefaultX,solidAreaDefaultY;
    public int actionLockCounter = 0;
    public Text entityText = new Text();
    public boolean onPath = true;
    public boolean onGate=true;
    public boolean checkAutoAgvMove=false;
    public double deadLine;
    public static boolean justEliminate=false;

    public Entity(GamePanel gp) {
        this.gp = gp;
        if(this instanceof AutoAgv) {
            Entity._id++;
            this.agvID = Entity._id;
        }
        else if(this instanceof Agv) this.agvID=1;
        else this.agvID = -1; //Ám chỉ đây là agent
    }

    public void setDefaultValues(){}

    public BufferedImage setup(String imageName) {

        UtilityTool uTool = new UtilityTool();
        BufferedImage image = null;

        try{
            image = ImageIO.read(getClass().getResourceAsStream("/res/" + imageName + ".png"));
            image = uTool.scaleImage(image, gp.tileSize, gp.tileSize, "entity");
        }catch (IOException e){
            e.printStackTrace();
        }

        return image;
    }

    public void setAction() {}

    public boolean searchPath(int goalCol, int goalRow) {
        int startCol = (x + solidArea.x)/gp.tileSize;
        int startRow = (y + solidArea.y)/gp.tileSize;
        gp.pFinder.setNodes(startCol, startRow, goalCol, goalRow, this);
        if(gp.pFinder.search(this) == true) {
            //Next x & y
            int nextX = gp.pFinder.pathList.get(0).col*gp.tileSize;
            int nextY = gp.pFinder.pathList.get(0).row*gp.tileSize;

            //Entity's solidArea position
            int enLeftX = x + solidArea.x;
            int enRightX = x + solidArea.x + solidArea.width;
            int enTopY = y + solidArea.y;
            int enBottomY = y + solidArea.y + solidArea.height;

            if(enTopY > nextY && enLeftX >= nextX && enRightX < nextX + gp.tileSize) direction = "up";
            else if(enTopY < nextY && enLeftX >= nextX && enRightX < nextX + gp.tileSize) direction = "down";
            else if(enTopY > nextY && enBottomY < nextY + gp.tileSize) {
                //left or right
                if(enLeftX > nextX) direction = "left";
                else if(enLeftX < nextX) direction = "right";
            }
            else if(enTopY > nextY && enLeftX > nextX) {
                //up or left
                direction = "up";
                gp.cChecker.checkTile(this);
                if(collisionOn == true) direction = "left";
            }
            else if(enTopY > nextY && enLeftX < nextX) {
                //up or right
                direction = "up";
                gp.cChecker.checkTile(this);
                if(collisionOn == true) direction = "right";
            }
            else if(enTopY < nextY && enLeftX > nextX) {
                //down or left
                direction = "down";
                gp.cChecker.checkTile(this);
                if(collisionOn == true) direction = "left";
            }
            else if(enTopY < nextY && enLeftX < nextX) {
                //down or right
                direction = "down";
                gp.cChecker.checkTile(this);
                if(collisionOn == true) direction = "right";
            }
            int nextCol = gp.pFinder.pathList.get(0).col;
            int nextRow = gp.pFinder.pathList.get(0).row;

            if(nextCol == goalCol && nextRow == goalRow) onPath = false;
            return true;
        } else {
            //System.out.println("stuck " + startCol + " " + startRow + " " + goalCol + " " + goalRow);
            return false;
        }
       // else System.out.println("KHONG TIM DUOC DUONG!!!!!!!"+goalCol+"  " +goalRow);
    }
    public void update() {}

    public String secondToHMS(){
        int h= (int) Math.floor(deadLine/3600);
        int m=(int)Math.floor(deadLine/60);
        int s=(int)Math.floor(deadLine%60);

        String res="DES_";
        res+=this.agvID+":  ";
        if(h<10) res+="0";
        res+= h +":";
        if(m<10) res+="0";
        res+= m +":";
        if(s<10) res+="0";
        res+=Integer.toString(s);
        res+=" ± 4";

        return res;
    }

}

class Text {
    int x;
    int y;
    int textLength;
    String text;
    String fontSize;
    String color;

    public int getTextLength() {
        int textLength = this.text.length()*5 + 20;
        return textLength;
    }
}

