package entity;

import gameAlgo.Position;
import main.GamePanel;
import main.Main;

import javax.swing.text.BadLocationException;
import java.awt.*;
import java.util.Random;

public class AutoAgv extends Entity{

        private Position startPos;
        private Position endPos;
        private int isOnGate=0;
        private boolean isGettingDes=false;
        private boolean haveChangeX=false;
        public int countTimeDeadLock=0;
        public boolean isFree=false;
        public static int autoAgvNum = 10;
        public static boolean lock=false;

        Font arial_17 = new Font("Arial",Font.TYPE1_FONT,17);
        private Text endText = new Text();


    public AutoAgv(GamePanel gp, Position startPos, Position endPos) {
            super(gp);
            this.startPos = startPos;
            this.endPos = endPos;
            setDefaultValues();;
            getAutoAgvImage();
            deadLine=calculateDes();
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

        public static void bornRandomAutoAgv(GamePanel gp) {
            Random random = new Random();
            Position startPoint=new Position(2*gp.tileSize,14*gp.tileSize);
            int randomEnd = random.nextInt(gp.DesPos.size());
            gp.autoAgvs.add(new AutoAgv(gp, startPoint, gp.DesPos.get(randomEnd)));
    }

        public void setDefaultValues() {

            x = startPos.x;
            y = startPos.y;
            direction = "right";
            speed = 1;

            this.solidArea = new Rectangle(4, 4, 24, 24);
            solidAreaDefaultX = solidArea.x;
            solidAreaDefaultY = solidArea.y;

            endText.text = "DES";
            endText.textLength = endText.getTextLength();
            endText.x = endPos.x+6;
            endText.y = endPos.y + 16;

            onPath = true;
            //handlerStuck=0;
        }

        public void getAutoAgvImage(){

            entityImage = setup("agv");
        }

        public void setAction() {

            if(onPath == true && (isOnGate==0||isOnGate==1)) {
                int goalCol = endPos.x/gp.tileSize;
                int goalRow = endPos.y/gp.tileSize;

                if(searchPath(goalCol, goalRow)==false) {
                    try{
                        eliminate(this);
                    }catch (BadLocationException e){

                    }
                }

                return;
            }
            else if(onPath==false && isOnGate==0){
                int midX = x + 16;
                int midY = y + 16;
                int midEndX = endPos.x + 16;
                int midEndY = endPos.y + 16;

                boolean flag=false;

                if(midX <= midEndX - 2) {
                    direction = "right";flag=true;
                }
                else if(midX >= midEndX + 2) {
                    direction = "left";flag=true;
                }

                collisionOn = false;
                gp.cChecker.checkTile(this);
                haveChangeX=false;
                if(!collisionOn) {
                    switch (direction) {
                        case "up" -> y -= speed;
                        case "down" -> y += speed;
                        case "left" -> {
                            x -= speed;haveChangeX=true;
                        }
                        case "right" -> {
                            x += speed;
                            haveChangeX=true;
                        }
                    }
                }

                if(midY <= midEndY - 2) {
                    direction = "down";flag=true;
                }
                else if(midY >= midEndY + 2) {
                    direction = "up";flag=true;
                }

                if(flag==false) {
                    isGettingDes=true;
                    haveChangeX=false;
                    setTimeout(()-> {
                        isGettingDes=false;
                        isOnGate++;
                        onPath=true;
                        endPos.x=49*gp.tileSize;
                        int tmp=0;
                        if(this.agvID%2==1) tmp=13;
                        else tmp=14;
                        endPos.y=tmp*gp.tileSize;}, 2000);
                }
            }
            else {
                try{
                    eliminate(this);
                }catch (BadLocationException e){

                }
            }
        }

        public void eliminate(AutoAgv autoAgv) throws BadLocationException {
            GamePanel.CountAutoAgvInit--;
            gp.autoAgvs.remove(autoAgv);
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

        public void update() {

            if(isGettingDes==true) return;
            // HANDLER AUTOAGV COLLISION
            gp.cChecker.checkAutoAgv(this);
            if(checkAutoAgvMove==true ){
                setTimeout(()-> {
                    checkAutoAgvMove=false;
                }, 1);
            }
            if(countTimeDeadLock==600 && lock==false){
                isFree=true;
                lock=true;
                setTimeout(()-> {
                    isFree=false;
                    lock=false;
                }, 2000);
            }
            if(checkAutoAgvMove==true && isFree==false) {
                countTimeDeadLock++;
                return;
            }
            setAction();
            countTimeDeadLock=0;
            collisionOn = false;
            gp.cChecker.checkTile(this);

            //IF COLLISION IS TRUE -> PLAYER CAN'T MOVE

            if(haveChangeX==true) return;
            if(!collisionOn) {
                switch (direction) {
                    case "up" -> y -= speed;
                    case "down" -> y += speed;
                    case "left" -> x -= speed;
                    case "right" -> x += speed;
                }
            }
        }

        public void draw(Graphics2D g2){
            g2.setFont(arial_17);
            g2.setColor(Color.red);
            //DRAW DES TEXT
            if(isOnGate==0)
                g2.drawString(endText.text, endText.x, endText.y);
            //DRAW AUTOAGV
            g2.drawImage(entityImage, x, y, gp.tileSize, gp.tileSize, null);
        }
        public double calculateDes(){
            return Math.floor(Math.abs(endPos.x- startPos.x)+Math.abs(endPos.y- startPos.y))*0.085;
        }
}
