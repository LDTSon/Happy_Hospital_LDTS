package main;

import entity.Agent;
import entity.Agv;
import entity.AutoAgv;
import gameAlgo.Position;
import gameAlgo.algorithm.PathFinder;
import tilesMap.TileManager;
import javax.swing.JPanel;
import java.awt.*;
import java.util.ArrayList;

public class GamePanel extends JPanel implements Runnable {
    public final int originalTileSize = 16; //16 * 16
    public final int scale = 2;
    public final int tileSize = originalTileSize*scale;//32 *32
    public final int maxScreenCol = 52;
    public final int maxScreenRow = 28;
    public final int screenWidth = tileSize*maxScreenCol;//52*32
    public final int screenHeight = tileSize*maxScreenRow;//28*32


    //FPS
    int FPS = 120;

    public TileManager tileM = new TileManager(this);
    KeyHandler keyH = new KeyHandler(this);
    public CollisionChecker cChecker = new CollisionChecker(this);
    public UI ui = new UI(this);
    public PathFinder pFinder = new PathFinder(this);
    Thread gameThread;

    public Agv player = new Agv(this, keyH);
    public ArrayList<Agent> agent = new ArrayList<Agent>();
    public ArrayList<Position> doorPos = new ArrayList<>();
    public ArrayList<AutoAgv> autoAgvs = new ArrayList<AutoAgv>();

    public int gameState;
    public final int playState = 1;
    public final int pauseState = 2;

    //Set player's default position

    public GamePanel(){
        this.setPreferredSize(new Dimension(52*tileSize,28*tileSize));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);
        this.gameState = playState;
        setupGame();
    }

    public void setupGame() {
        Position.getDoorPosition(this);
        for(int i = 0; i < Agent.agentNum; i++) {
            Agent.bornRandomAgent(this);
        }
        for(int i = 0; i < AutoAgv.autoAgvNum; i++) {
            AutoAgv.bornRandomAutoAgv(this);
        }
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    public void run(){
        double drawInterval = 1000000000/FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        while (gameThread != null){
            currentTime = System.nanoTime();
            delta += (currentTime-lastTime)/drawInterval;
            lastTime = currentTime;
            if(delta >= 1){
                update();
                repaint();
                delta--;
            }

        }
    }
    public void update(){

        if(gameState == playState) {
            for(int i = 0; i < autoAgvs.size(); i++)
                if(autoAgvs.get(i) != null) autoAgvs.get(i).update();

            for(int i = 0; i < agent.size(); i++)
                if(agent.get(i) != null) agent.get(i).update();

            player.update();
        }
        if(gameState == pauseState) {

        }
    }
    public void paintComponent(Graphics g){
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D)g;

        //TILE
        tileM.draw(g2);
        //AUTOAGV
        for(int i = 0; i < autoAgvs.size(); i++)
            if(autoAgvs.get(i) != null) autoAgvs.get(i).draw(g2);
        //AGENT
        for(int i = 0; i < agent.size(); i++)
            if(agent.get(i) != null) agent.get(i).draw(g2);
        //PLAYER AGV
        player.draw(g2);
        //UI
        ui.draw(g2);

        g2.dispose();
    }
}
