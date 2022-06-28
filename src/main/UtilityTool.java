package main;
import java.awt.*;
import java.awt.image.BufferedImage;

import static java.awt.image.BufferedImage.TYPE_INT_ARGB;
public class UtilityTool {
    public BufferedImage scaleImage(BufferedImage original, int width, int height, String type) {
        Color bgColor = null;
        if(type == "entity") bgColor = null;
        else if(type == "door") bgColor = null;
        else if(type == "tile") bgColor = new Color(233, 233, 233);
        BufferedImage scaledImage = new BufferedImage(width, height, TYPE_INT_ARGB);
        Graphics2D g2 = scaledImage.createGraphics();
        g2.drawImage(original, 0, 0, width, height, bgColor, null);
        g2.dispose();

        return scaledImage;
    }
}
