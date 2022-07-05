package entity;


import gameAlgo.Position;
import main.GamePanel;
import main.KeyHandler;

import javax.swing.text.BadLocationException;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class Agv extends Entity {
        public boolean isValidDirection = true;
        private boolean isImmortal = false; // biến cần cho xử lý overlap
        private boolean isDisable = false; // biến cần cho xử lý overlap
        private Position goalPos;
        private Text goalText = new Text();
        Font arial_17;
        Font arial_30;
        KeyHandler keyH;
        private static ArrayList<Position> DesPos=new ArrayList<Position>();


        public Agv(GamePanel gp, KeyHandler keyH){
            super(gp);
            this.keyH = keyH;
            setDefaultValues();
            getPlayerImage();
        }

    public static void getDesPosition(GamePanel gp){
        int col = 0;
        int row = 0;

        while(col < gp.maxScreenCol && row < gp.maxScreenRow){

            int tileNum = gp.tileM.mapTileNum[col][row];
            boolean check=gp.tileM.tile[tileNum].agvCollision;

            int x = col*gp.tileSize;
            int y = row*gp.tileSize;

            if(check==false && col>=5 && col<=45) DesPos.add(new Position(x, y));

            col++;

            if(col == gp.maxScreenCol){
                col = 0;
                row ++;
            }
        }
    }
        public void setDefaultValues(){
            x = 2*gp.tileSize;
            y = 13*gp.tileSize;
            speed = 1;
            direction = "right";

            entityText.text = "AGV";
            entityText.textLength = entityText.getTextLength();
            entityText.x = this.x + 12 - entityText.textLength/2;
            entityText.y = this.y - 6;
            arial_17 = new Font("Arial", Font.BOLD,17);
            arial_30 = new Font("Arial", Font.BOLD,30);

            solidArea = new Rectangle(8,8,16,16);
            solidAreaDefaultX = solidArea.x;
            solidAreaDefaultY = solidArea.y;

            getDesPosition(gp);
            Random random = new Random();
            int randomGoal = random.nextInt(DesPos.size());
            this.goalPos = DesPos.get(randomGoal);
            goalText.text = "DES";
            goalText.x = goalPos.x + 11;
            goalText.y = goalPos.y + 16;
            deadLine=calculateDes();
            String output=this.secondToHMS();
            gp.sc.ta.append(output+"\n");
        }
        public void getPlayerImage(){

            entityImage = setup("agv");
        }

        public void toastInvalidMove(Graphics2D g2){
            g2.setColor(Color.RED);
            String message = "Di chuyển không hợp lệ!";
            g2.setFont(arial_30);
            g2.drawString(message,gp.tileSize*20,gp.tileSize*10);
        }

        public void toastOverLay(Graphics2D g2){
            g2.setColor(Color.RED);
            String message = "AGV va chạm với Agent!";
            g2.setFont(arial_30);
            g2.drawString(message,gp.tileSize*20,gp.tileSize*14);
        }

        public void checkDirection() {
            int midX = x + gp.tileSize/2 ;
            int midY = y + gp.tileSize/2 ;
            //System.out.println(x +" "+ y);
            midX = midX / gp.tileSize;
            midY = midY / gp.tileSize;

            int tileNum = gp.tileM.mapTileNum[midX][midY];
            //System.out.println(tileNum);
            String d = gp.tileM.tile[tileNum].tileDirection;

            switch (d) {
                case "left":
                    if (direction.equals("right")) isValidDirection = false;
                    break;
                case "right":
                    if (direction.equals("left")) isValidDirection = false;
                    break;
                case "up":
                    if (direction.equals("down")) isValidDirection = false;
                    break;
                case "down":
                    if (direction.equals("up")) isValidDirection = false;
                    break;
            }
        }
        public void update(){

            if(this.isDisable) return;

            //CHECK AGENT COLLISION
            if(justCollided) handleOverlap();

            if(keyH.leftPressed || keyH.downPressed || keyH.upPressed || keyH.rightPressed){
                if(keyH.upPressed){
                    direction="up";
                }
                else if(keyH.downPressed){
                    direction="down";
                }
                else if(keyH.rightPressed){
                    direction="right";
                }
                else if(keyH.leftPressed){
                    direction="left";
                }

                //CHECK TILE COLLISION
                collisionOn = false;
                gp.cChecker.checkTile(this);
                //CHECK DIRECTION
                isValidDirection = true;
                checkDirection();

                //IF COLLISION IS TRUE -> PLAYER CAN'T MOVE
                if(!collisionOn && isValidDirection) {
                    switch (direction) {
                        case "up" -> y -= speed;
                        case "down" -> y += speed;
                        case "left" -> x -= speed;
                        case "right" -> x += speed;
                    }
                }

                //CHECK IF AGV TOUCH GOAL
                if(goalText.x -11 <= x && x <= goalText.x + 21 &&
                   goalText.y -16 <= y && y <= goalText.y + 16) {

                    reachToDestination();
                    Random random = new Random();
                    int randomGoal = random.nextInt(DesPos.size());
                    this.goalPos = DesPos.get(randomGoal);
                    goalText.x = goalPos.x + 11;
                    goalText.y = goalPos.y + 16;
                    deadLine=calculateDes();
                    String output=this.secondToHMS();
                    gp.sc.ta.append(output+"\n");
                }

                //UPDATE TEXT
                entityText.x = this.x + 12 - entityText.textLength/2;
                entityText.y = this.y - 6;
            }
        }

        public static void setTimeout(Runnable runnable, int delay){
            new Thread(() -> {
                try {
                    Thread.sleep(delay);
                    runnable.run();
                }
                catch (Exception e){
                    System.err.println(e);
                }
            }).start();
        }
        public void handleOverlap(){

            if(!this.isImmortal){
                this.isDisable = true;
                setTimeout(()->{
                    this.isImmortal = true;
                    this.isDisable = false;
                    setTimeout(()-> {this.isImmortal = false;}, 2000);}, 1000);
            }
            justCollided = false;
        }

        public void draw(Graphics2D g2){

            //AGV TEXT
            g2.setFont(arial_17);
            g2.setColor(Color.green);
            g2.drawString(entityText.text, entityText.x, entityText.y);
            if(goalText != null) {
                g2.setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 17));
                g2.drawString(goalText.text, goalText.x, goalText.y);
            }

            //AGV
            if(this.isDisable) toastOverLay(g2);
            if(!isValidDirection) toastInvalidMove(g2);
            g2.drawImage(entityImage, this.x, this.y,gp.tileSize,gp.tileSize,null);
        }
    public double calculateDes() {
        return Math.floor(Math.abs(goalPos.x - x) + Math.abs(goalPos.y - y)) * 0.085;
    }
    public void reachToDestination(){

        int lines=gp.sc.ta.getLineCount();
        try{
            for(int i=0;i<lines-1;i++){
                int start=gp.sc.ta.getLineStartOffset(i);
                int end=gp.sc.ta.getLineEndOffset(i);
                String tmp1=gp.sc.ta.getText(start,end-start);
                int x=tmp1.indexOf('_')+1;
                int y=tmp1.indexOf(':');
                String tmp2=tmp1.substring(x,y);
                int idValue=0;
                if(tmp2.length()==1) idValue=tmp2.charAt(0)-'0';
                else{
                    idValue=(tmp2.charAt(1)-'0') + (tmp2.charAt(0)-'0')*10;
                }
                if(idValue==this.agvID){
                    gp.sc.ta.replaceRange("",start,end);
                    break;
                }
            }
        }catch (BadLocationException e){

        }
    }
}
