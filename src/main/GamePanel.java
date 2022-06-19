package main;

import entity.Agv;
import tile.TileManager;
import javax.swing.JPanel;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable {
    public final int originalTileSize = 12; //16 * 16
    public final int scale =2;
    public final int tileSize = originalTileSize*scale;//32 *32
    public final int maxScreenCol=52;
    public final int maxScreenRow=28;
    public final int screenWidth=tileSize*maxScreenCol;//52*32
    public final int screenHeight=tileSize*maxScreenRow;//28*32


    //FPS
    int FPS=60;

    public TileManager TileM=new TileManager(this);
    KeyHandler keyH=new KeyHandler();
    // public CollisionChecker cChecker=new CollisionChecker(this);
    //public UI ui =new UI(this);
    Thread gameThread;

    public Agv player=new Agv(this,keyH);

    //Set player's default position

    public GamePanel(){
        this.setPreferredSize(new Dimension(1920,1080));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);
    }

    public void startGameThread(){
        gameThread=new Thread(this);
        gameThread.start();
    }
    /*    @Override
        public void run() {
            double drawInterval=1000000000/FPS;
            double nextDrawTime=System.nanoTime()+drawInterval;
            while(gameThread!=null){
                //1---UPDATE: information such as charactor position
                update();
                //2---DRAW: the screen with the update information
                repaint();
                try {
                    double remainingTime=nextDrawTime-System.nanoTime();
                    remainingTime/=1000000;
                    if(remainingTime<0){
                        remainingTime=0;
                    }
                    Thread.sleep((long) remainingTime);
                    nextDrawTime+=drawInterval;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }*/
    public void run(){
        double drawInterval=1000000000/FPS;
        double delta=0;
        long lastTime=System.nanoTime();
        long currentTime;
        while (gameThread!=null){
            currentTime=System.nanoTime();
            delta+=(currentTime-lastTime)/drawInterval;
            lastTime=currentTime;
            if(delta>=1){
                update();
                repaint();
                delta--;
            }

        }
    }
    public void update(){
        player.update();
    }
    public void paintComponent(Graphics g){
        super.paintComponent(g);

        Graphics2D g2=(Graphics2D)g;
        //TILE
        TileM.draw(g2);
        //PLAYER
        player.draw(g2);
        //UI
       // ui.draw(g2);
        g2.dispose();
    }
}