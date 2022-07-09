package entity;
import main.GamePanel;
import main.Timer;
import main.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.lang.String;
import java.util.Objects;

public class Entity {

    static GamePanel gp;
    public int x, y;
    public int speed;
    public static int  _id = 0;
    public final int agvID;
    public double start;
    public BufferedImage entityImage;
    public String direction;

    public Rectangle solidArea;
    public boolean collisionOn = false;
    public boolean justCollided = false;
    public int expectedTime;

    public int solidAreaDefaultX,solidAreaDefaultY;
    public Text entityText = new Text();
    public boolean onPath = true;
    public static double harmfulness = 0;

    public Entity(GamePanel gp) {
        this.gp = gp;
        if(this instanceof AutoAgv || this instanceof Agv) {
            Entity._id++;
            this.agvID = Entity._id;
        } else this.agvID = -1; //Ám chỉ đây là agent
    }

    public void setDefaultValues(){}

    public BufferedImage setup(String imageName) {

        UtilityTool uTool = new UtilityTool(gp);
        BufferedImage image = null;

        try{
            image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/res/" + imageName + ".png")));
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
        if(gp.pFinder.search(this)) {
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
                if(collisionOn) direction = "left";
            }
            else if(enTopY > nextY && enLeftX < nextX) {
                //up or right
                direction = "up";
                gp.cChecker.checkTile(this);
                if(collisionOn) direction = "right";
            }
            else if(enTopY < nextY && enLeftX > nextX) {
                //down or left
                direction = "down";
                gp.cChecker.checkTile(this);
                if(collisionOn) direction = "left";
            }
            else if(enTopY < nextY && enLeftX < nextX) {
                //down or right
                direction = "down";
                gp.cChecker.checkTile(this);
                if(collisionOn) direction = "right";
            }
            int nextCol = gp.pFinder.pathList.get(0).col;
            int nextRow = gp.pFinder.pathList.get(0).row;

            if(nextCol == goalCol && nextRow == goalRow) onPath = false;
            return true;
        } else {
            System.out.println("stuck " + startCol + " " + startRow + " " + goalCol + " " + goalRow);
            return false;
        }
    }

    public void estimateArrivalTime(int startX, int startY, int endX, int endY) {
         this.expectedTime = (int) Math.floor(Math.sqrt((endX - startX)*(endX - startX) + (endY - startY)*(endY - startY))*0.085);
    }

    public void calHarmfulness() {
        double realTimeTaken = gp.timer.secondsSinceStart - start;
        if(realTimeTaken < expectedTime - Timer.DURATION || realTimeTaken > expectedTime + Timer.DURATION) {
            harmfulness += 5*Math.max(expectedTime - Timer.DURATION - realTimeTaken,
                                    realTimeTaken -(expectedTime + Timer.DURATION));
        }
    }

    public void appendAgvDeadline() {
        String outPut="";
        outPut += "DES_" + this.agvID + ": ";
        outPut += Timer.getFormattedTime(this.expectedTime);
        gp.sc.ta.append(outPut + "\n");
    }

    public void update() {}
}

class Text {
    int x;
    int y;
    int textLength;
    String text;
    String fontSize;
    String color;

    public int getTextLength() {
        return this.text.length()*5 + 20;
    }
}

