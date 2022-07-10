package entity;

import gameAlgo.Position;
import main.GamePanel;
import main.Timer;

import javax.swing.text.BadLocationException;
import java.awt.*;
import java.util.Random;

import static java.lang.Math.abs;

public class AutoAgv extends Entity{

    private Position startPos;
    private Position endPos;
    private int isOnGate = 0;
    private boolean isGettingDes = false;
    private boolean haveChangeX = false;
    public int countTimeDeadLock = 0;
    public boolean isFree = false;
    public boolean checkMoveEntity;
    public static int autoAgvNum = 10;
    public static boolean lock = false;
    public boolean checkUpdateEndPos=false;

    Font arial_17 = new Font("Arial", Font.BOLD,17);
    private Text endText = new Text();


    public AutoAgv(GamePanel gp, Position startPos, Position endPos) {
        super(gp);
        this.startPos = startPos;
        this.endPos = endPos;
        setDefaultValues();
        getAutoAgvImage();
    }



    public static void bornRandomAutoAgv(GamePanel gp) {
        Random random = new Random();
        Position startPoint = new Position(2*gp.tileSize,14*gp.tileSize);
        int randomEnd = random.nextInt(gp.desPos.size());
        Position tmp = new Position(0,0);
        tmp.x=gp.desPos.get(randomEnd).x;
        tmp.y=gp.desPos.get(randomEnd).y;

        gp.autoAgvs.add(new AutoAgv(gp, startPoint, tmp));
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

        estimateArrivalTime(startPos.x, startPos.y, endPos.x, endPos.y);

        //AUTOAGV DEADLINE
        appendAgvDeadline();
    }

    public void getAutoAgvImage(){

        entityImage = setup("agvRed");
    }

    public void setAction() {

        if(onPath && (isOnGate == 0 || isOnGate==1)) {
            int goalCol = endPos.x/gp.tileSize;
            int goalRow = endPos.y/gp.tileSize;

            if(isOnGate == 1 && !checkUpdateEndPos){
                if(x>=46*gp.tileSize){
                    int tmp1 = abs(y-13*gp.tileSize);
                    int tmp2 = abs(y-14*gp.tileSize);
                    if(tmp1>tmp2){
                        endPos.y=14*gp.tileSize;
                    }
                    else endPos.y=13*gp.tileSize;
                    checkUpdateEndPos=true;
                }
            }
            if(!searchPath(goalCol, goalRow)) {
                try{
                    eliminate(this);
                }catch (BadLocationException e){
                    e.printStackTrace();
                }
            }
        }
        else if(!onPath && isOnGate == 0){
            int midX = x + 16;
            int midY = y + 16;
            int midEndX = endPos.x + 16;
            int midEndY = endPos.y + 16;

            boolean flag = false;

            if(midX <= midEndX - 2) {
                direction = "right"; flag = true;
            }
            else if(midX >= midEndX + 2) {
                direction = "left"; flag = true;
            }

            collisionOn = false;
            gp.cChecker.checkTile(this);
            haveChangeX=false;
            if(!collisionOn) {
                switch (direction) {
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
                direction = "down"; flag = true;
            }
            else if(midY >= midEndY + 2) {
                direction = "up"; flag = true;
            }

            if(!flag) {
                calHarmfulness();
                isGettingDes = true;
                haveChangeX = false;
                setTimeout(()-> {
                    isGettingDes = false;
                    isOnGate++;
                    onPath = true;
                    endPos.x = 49*gp.tileSize;
                    int tmp;
                    if(this.agvID%2 == 1) tmp = 13;
                    else tmp = 14;
                    endPos.y = tmp*gp.tileSize;}, 2000);
            }
        }
        else {
            try{
                eliminate(this);
            }catch (BadLocationException e){
                e.printStackTrace();
            }
        }
    }

    public void eliminate(AutoAgv autoAgv) throws BadLocationException {
        GamePanel.CountAutoAgvInit--;
        gp.autoAgvs.remove(autoAgv);
        int lines = gp.sc.ta.getLineCount();
        try{
            for(int i = 0; i < lines-1; i++){
                int start = gp.sc.ta.getLineStartOffset(i);
                int end = gp.sc.ta.getLineEndOffset(i);
                String tmp1 = gp.sc.ta.getText(start,end-start);
                int x = tmp1.indexOf('_')+1;
                int y = tmp1.indexOf(':');
                String tmp2 = tmp1.substring(x,y);
                int idValue;
                if(tmp2.length() == 1) idValue = tmp2.charAt(0)-'0';
                else{
                    idValue=(tmp2.charAt(1)-'0') + (tmp2.charAt(0)-'0')*10;
                }
                if(idValue == this.agvID){
                    gp.sc.ta.replaceRange("",start,end);
                    break;
                }
            }
        }catch (BadLocationException e){
            e.printStackTrace();
        }
    }

    public void update() {

        if(isGettingDes) return;
        // HANDLE AUTOAGV COLLISION
        gp.cChecker.checkMove(this,20);
        if(checkMoveEntity){
            setTimeout(()-> checkMoveEntity = false, 1);
        }
        if(countTimeDeadLock >= 400 && !lock){
            int maxCountTime = 0;
            for(int i = 0; i < gp.autoAgvs.size(); i++){
                if(gp.autoAgvs.get(i) != null){
                    if(gp.autoAgvs.get(i).countTimeDeadLock>maxCountTime) maxCountTime=gp.autoAgvs.get(i).countTimeDeadLock;
                }
            }
            if(countTimeDeadLock == maxCountTime){
                isFree = true;
                lock = true;
                setTimeout(()-> {
                    isFree = false;
                    lock = false;
                }, 2000);
            }
        }
        if(checkMoveEntity && !isFree) {
            countTimeDeadLock++;
            return;
        }
        setAction();
        countTimeDeadLock=0;
        collisionOn = false;
        gp.cChecker.checkTile(this);

        //IF COLLISION IS TRUE -> PLAYER CAN'T MOVE

        if(haveChangeX) return;
        if(!collisionOn) {
            switch (direction) {
                case "up" -> y -= speed;
                case "down" -> y += speed;
                case "left" -> x -= speed;
                case "right" -> x += speed;
            }
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

    public void draw(Graphics2D g2){
        g2.setFont(arial_17);
        g2.setColor(Color.red);
        //DRAW DES TEXT
        if(isOnGate == 0)
            g2.drawString(endText.text, endText.x, endText.y);
        //DRAW AUTOAGV
        g2.drawImage(entityImage, x, y, gp.tileSize, gp.tileSize, null);
    }

}