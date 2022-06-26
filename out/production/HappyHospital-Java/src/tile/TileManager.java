package tile;

import main.GamePanel;
import main.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
public class TileManager {
    GamePanel gp;
    public Tile [] tile;
    public int[][] mapTileNum;

    boolean drawPath = false;

    public TileManager(GamePanel gp){
        this.gp = gp;
        tile = new Tile[20];
        getTileImage();
        mapTileNum = new int[100][100];
        tile[4].tileDirection="left";
        tile[6].tileDirection="right";
        tile[8].tileDirection="up";
        tile[2].tileDirection="down";
        tile[5].agentCollision = true;
        tile[10].agentCollision = true;

        loadMap("/res/map.txt");
    }
    public void loadMap(String filePath){
        try{
            InputStream is = getClass().getResourceAsStream(filePath);
            assert is != null;
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            int col = 0;
            int row = 0;

            while(col < gp.maxScreenCol && row < gp.maxScreenRow){
                String line = br.readLine();
                String[] number = line.split(" ");

                while(col < gp.maxScreenCol){
                    int num = Integer.parseInt(number[col]);
                    mapTileNum[col][row] = num;
                    col++;
                }
                if(col == gp.maxScreenCol){
                    col = 0;
                    row++;
                }
            }
            br.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void getTileImage(){

        setup(0, "0", true, false);
        setup(1, "1", false, false);
        setup(2, "2", false, false);
        setup(3, "3", false, false);
        setup(4, "4", false, false);
        setup(5, "5", true, true);
        setup(6, "6", false, false);
        setup(7, "7", false, false);
        setup(8, "8", false, false);
        setup(9, "9", false, false);
        setup(10, "10", true, true);
        setup(11, "11", false, false);
        setup(12, "12", false, false);
        setup(13, "13", false, false);
        setup(14, "14", false, false);
        setup(15, "15", false,false);
        setup(16, "16", false, false);
        setup(17, "17", false, false);
        setup(18, "18", false, false);

    }

    public void setup(int index, String imageName, boolean agvCollision, boolean agentCollision) {

        UtilityTool uTool = new UtilityTool();

        try{
            tile[index] = new Tile();
            tile[index].image = ImageIO.read(getClass().getResourceAsStream("/tiles/" + imageName + ".png"));
            tile[index].image = uTool.scaleImage(tile[index].image, gp.tileSize, gp.tileSize, "tile");
            tile[index].agvCollision = agvCollision;
            tile[index].agentCollision = agentCollision;

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void draw(Graphics2D g2){
        int worldCol = 0;
        int worldRow = 0;

        while(worldCol < gp.maxScreenCol && worldRow < gp.maxScreenRow){

            int tileNum = mapTileNum[worldCol][worldRow];

            int screenX = worldCol*gp.tileSize;
            int screenY = worldRow*gp.tileSize;


            g2.drawImage(tile[tileNum].image, screenX, screenY, gp.tileSize, gp.tileSize,null);

            worldCol++;

            if(worldCol == gp.maxScreenCol){
                worldCol = 0;
                worldRow ++;
            }
        }

        if(drawPath == true) {
            g2.setColor(new Color(255, 0, 0, 70));

            for(int i = 0; i < gp.pFinder.pathList.size(); i++) {

                int x = gp.pFinder.pathList.get(i).col * gp.tileSize;
                int y = gp.pFinder.pathList.get(i).row * gp.tileSize;

                g2.fillRect(x, y, gp.tileSize, gp.tileSize);
            }
        }
    }
}