package tile;

import main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
public class TileManager {
    GamePanel gp;
    public Tile [] tile;
    public int[][] mapTileNum;

    public TileManager(GamePanel gp){
        this.gp=gp;
        tile=new Tile[10];
        getTileImage();
        mapTileNum=new int[100][100];
        loadMap("/res/map.txt");
    }
    public void loadMap(String filePath){
        try{
            InputStream is =getClass().getResourceAsStream(filePath);
            BufferedReader br=new BufferedReader(new InputStreamReader(is));
            int col=0;
            int row=0;

            while(col<gp.maxScreenCol && row<gp.maxScreenRow){
                String line =br.readLine();

                while(col< gp.maxScreenCol){
                    String number[]=line.split(" ");
                    int num=Integer.parseInt(number[col]);
                    mapTileNum[row][col]=num;
                    col++;
                }
                if(col==gp.maxScreenCol){
                    col=0;
                    row++;
                }
            }
            br.close();
        }catch (Exception e){

        }
    }
    public void getTileImage(){
        try{
            tile[0]=new Tile();
            tile[0].image= ImageIO.read(getClass().getResourceAsStream("/res/white.png"));

            tile[1]=new Tile();
            tile[1].image= ImageIO.read(getClass().getResourceAsStream("/res/T.png"));



        }catch (IOException e){
            e.printStackTrace();
        }
    }
    public void draw(Graphics2D g2){
        int worldCol=0;
        int worldRow=0;

        while(worldCol<gp.maxScreenCol && worldRow<gp.maxScreenRow){

            int tileNum=mapTileNum[worldRow][worldCol];

            int screenX=worldCol*gp.tileSize;
            int screenY=worldRow*gp.tileSize;


            g2.drawImage(tile[tileNum].image,screenX,screenY,gp.tileSize,gp.tileSize,null);

            worldCol++;

            if(worldCol==gp.maxScreenCol){
                worldCol=0;
                worldRow++;
            }
        }
    }
}
