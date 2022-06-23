package entity;
import main.GamePanel;
import main.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.lang.String;
public class Entity {

    GamePanel gp;
    public int x, y;
    public int speed;
    public BufferedImage entityImage;
    public String direction;

    public Rectangle solidArea;
    public boolean collisionOn = false;

    public int solidAreaDefaultX,solidAreaDefaultY;

    public Entity(GamePanel gp) {
        this.gp = gp;
    }

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
}
