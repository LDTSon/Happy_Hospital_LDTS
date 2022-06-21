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

    public TileManager(GamePanel gp){
        this.gp = gp;
        tile = new Tile[20];
        getTileImage();
        mapTileNum = new int[100][100];
        loadMap("/res/map.txt");
        tile[4].tileDirection="left";
        tile[6].tileDirection="right";
        tile[8].tileDirection="up";
        tile[2].tileDirection="down";
    }
    public void loadMap(String filePath){
        try{
            InputStream is = getClass().getResourceAsStream(filePath);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            int col = 0;
            int row = 0;

            while(col < gp.maxScreenCol && row < gp.maxScreenRow){
                String line = br.readLine();
                String number[] = line.split(" ");

                while(col < gp.maxScreenCol){
                    int num = Integer.parseInt(number[col]);
                    mapTileNum[row][col] = num;
                    col++;
                }
                if(col == gp.maxScreenCol){
                    col = 0;
                    row++;
                }
            }
            br.close();
        }catch (Exception e){

        }
    }
    public void getTileImage(){

        setup(0, "0", true);
        setup(1, "1", false);
        setup(2, "2", false);
        setup(3, "3", false);
        setup(4, "4", false);
        setup(5, "5", true);
        setup(6, "6", false);
        setup(7, "7", false);
        setup(8, "8", false);
        setup(9, "9", false);
        setup(10, "10", true);
        setup(11, "11", false);
        setup(12, "12", false);
        setup(13, "13", false);
        setup(14, "14", false);
        setup(15, "15", false);
        setup(16, "16", false);
        setup(17, "17", false);
        setup(18, "18", false);

    }

    public void setup(int index, String imageName, boolean collision) {

        UtilityTool uTool = new UtilityTool();

        try{
            tile[index] = new Tile();
            tile[index].image = ImageIO.read(getClass().getResourceAsStream("/tiles/" + imageName + ".png"));
            tile[index].image = uTool.scaleImage(tile[index].image, gp.tileSize, gp.tileSize);
            tile[index].collison = collision;

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void draw(Graphics2D g2){
        int worldCol = 0;
        int worldRow = 0;

        while(worldCol < gp.maxScreenCol && worldRow < gp.maxScreenRow){

            int tileNum = mapTileNum[worldRow][worldCol];

            int screenX = worldCol*gp.tileSize;
            int screenY = worldRow*gp.tileSize;


            g2.drawImage(tile[tileNum].image, screenX, screenY, gp.tileSize, gp.tileSize,null);

            worldCol++;

            if(worldCol == gp.maxScreenCol){
                worldCol = 0;
                worldRow ++;
            }
        }
    }
}