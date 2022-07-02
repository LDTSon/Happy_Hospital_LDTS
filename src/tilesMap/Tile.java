package tilesMap;

import java.awt.image.BufferedImage;
public class Tile {
    public BufferedImage image;
    public boolean agvCollision = false;
    public boolean agentCollision = false;
    public String tileDirection = "undirected";
}
