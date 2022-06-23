package entity;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.lang.String;
public class Entity {
    public int x, y;
    public int speed;
    public BufferedImage entityImage;
    public String direction;

    public Rectangle solidArea;
    public boolean collisionOn = false;

    public int solidAreaDefaultX,solidAreaDefaultY;
}
