package entity;

import main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;

public class Agent extends Entity{



    public Agent(GamePanel gp) {
        super(gp);
        solidArea = new Rectangle(8,8,16,16);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        getAgentImage();
    }

    public void getAgentImage(){

        entityImage = setup("agent");
    }

    public void draw(Graphics2D g2){

        g2.drawImage(entityImage, 50, 50, gp.tileSize, gp.tileSize, null);

    }
}
