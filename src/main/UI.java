package main;

import com.google.gson.Gson;
import entity.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.URI;

import java.text.DecimalFormat;

import gameAlgo.Position;
import javafx.stage.FileChooser;

import static entity.Agent.agentNum;
import static main.Main.window;


public class UI {
    GamePanel gp;
    Graphics2D g2;
    Font arial_40, arial_80B;
    public boolean messageOn = false;
    public String message = "";
    int messageCounter = 0;
    public boolean gameFinish = false;
    double playTime;
    DecimalFormat dFormat = new DecimalFormat("#0.00");
    boolean saveMapVisible = false;
    boolean saveAgentVisible = false;
    public final FileChooser fileChooser = new FileChooser();

    public UI(GamePanel  gp){
        this.gp = gp;
        arial_80B = new Font("Arial",Font.BOLD,80);
        arial_40 = new Font("Arial",Font.PLAIN,40);
    }
    public void showMessage(String text){
        message = text;
        messageOn = true;

    }
    public void draw(Graphics2D g2){

        this.g2 = g2;

        g2.setFont(arial_80B);
        g2.setColor(Color.RED);
        //PLAYSTATE
        if(gp.gameState == gp.playState) {
            drawAGVScreen();
        }
        //PAUSESTATE
        if(gp.gameState == gp.pauseState) {
            drawPauseScreen(1);
        }
    }

    private void drawPauseScreen(int k) {
        if(k==1) {
            String text = "Tạm dừng";
            int x = getXForCenteredText(text);
            int y = gp.screenHeight/2;
            g2.drawString(text, x, y);
        }

        if(k==2) {
            JOptionPane.showMessageDialog(window, "Map is saved!","Message", JOptionPane.INFORMATION_MESSAGE);
            JButton checker = new JButton("OK");

            checker.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    gp.gameState = gp.playState;
                    draw(g2);
                }
            });
            gp.add(checker);
        }

        if(k==3) {
            JOptionPane.showMessageDialog(window, "Number of Agents is saved!","Message", JOptionPane.INFORMATION_MESSAGE);
            JButton checker = new JButton("OK");
            checker.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    gp.gameState = gp.playState;
                    draw(g2);
                }
            });
            gp.add(checker);
        }
    }
    private void drawAGVScreen(){
        //FRAME
        final int frameX = gp.tileSize*52;
        final int frameY = 0;

        //TEXT_ATTRIBUTE
        g2.setColor(Color.RED);
        g2.setFont(g2.getFont().deriveFont(22F));

        //INPUT_NUM_OF_AGV
        JTextField numOfAGV = new JTextField(4);
        numOfAGV.setFont(new Font("Times New Roman", Font.PLAIN, 20));
        numOfAGV.setBounds(32*52, 32*7, 32*5, 32*2);
        numOfAGV.setBackground(Color.WHITE);
        gp.add(numOfAGV);

        //OUTPUT_AGV_DEADLINES
        JTextArea AGVDeadlines = new JTextArea(10000, 1);
        AGVDeadlines.setFont(new Font("Times New Roman", Font.PLAIN, 15));
        AGVDeadlines.setBounds(32*52, 32*16, 32*8, 32*10);

        //BUTTON
        JButton jb1 = new JButton("Save Data");
        jb1.setBounds(32*54, 32, 32*4,32);
        jb1.setFont(new Font("Times New Roman", Font.BOLD, 20));
        jb1.setBackground(Color.white);
        jb1.setForeground(Color.black);

        JLabel alertMap = new JLabel("Map is saved!");
        alertMap.setFont(new Font("Times New Roman", Font.PLAIN, 40));
        alertMap.setBackground(Color.WHITE);
        alertMap.setBounds(32*35, 32*28, 32*10, 32*3);
        alertMap.setVisible(saveMapVisible);
        jb1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gp.gameState = gp.pauseState;
                drawPauseScreen(2);
                exportJSON();
                gp.gameState = gp.playState;
                gp.requestFocusInWindow();
            }
        });
        gp.add(alertMap);
        gp.add(jb1);


        JButton jb2 = new JButton("Load Data");
        jb2.setBounds(32*54, 32*3, 32*4,32);
        jb2.setFont(new Font("Times New Roman", Font.BOLD, 20));
        jb2.setBackground(Color.white);
        jb2.setForeground(Color.black);
        jb2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gp.gameState = gp.pauseState;
                importJSON();
                gp.gameState = gp.playState;
                gp.requestFocusInWindow();
            }
        });
        gp.add(jb2);


        JButton jb3 = new JButton("Apply");
        jb3.setBounds(32*57, 32*7, 32*3, 32*2);
        jb3.setFont(new Font("Times New Roman", Font.BOLD, 20));
        jb3.setBackground(Color.white);
        jb3.setForeground(Color.black);

//        JLabel alertAgent = new JLabel("Number of Agents is saved!");
//        alertAgent.setFont(new Font("Times New Roman", Font.PLAIN, 40));
//        alertAgent.setBackground(Color.WHITE);
//        alertAgent.setBounds(32*5, 32*28, 32*16, 32*3);
//        alertAgent.setVisible(saveAgentVisible);
//        gp.add(alertAgent);
        jb3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gp.gameState = gp.pauseState;
                agentNum = Integer.parseInt(numOfAGV.getText());
                drawPauseScreen(3);
                gp.gameState = gp.playState;
                gp.requestFocusInWindow();
            }
        });
        gp.add(jb3);


        JButton jb4 = new JButton("Hướng dẫn");
        jb4.setBounds(32*54, 32*26, 32*4, 32*2);
        jb4.setFont(new Font("Times New Roman", Font.PLAIN, 18));
        jb4.setBackground(Color.white);
        jb4.setForeground(Color.black);
        jb4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openWebpage(URI.create("https://github.com/phamtuanhien/Project20211_HappyHospital"));
            }
        });
        gp.add(jb4);


        //TEXT
        int textX = frameX + gp.tileSize;
        int textY = frameY + gp.tileSize*6;
        g2.drawString("Number of Agents:", textX, textY);
        textX += gp.tileSize;
        textY += gp.tileSize*5;

        g2.drawString(TimeControl.getFormattedTime(), textX, textY);//TIMER
        textX -= gp.tileSize;
        textY += gp.tileSize*2;
        g2.drawString("H.NESS IS HERE!", textX, textY);//H.NESS
        textY += gp.tileSize*2;
        g2.drawString("AGV Deadlines", textX, textY);//AGV_DEADLINES_TABLE
        gp.add(AGVDeadlines);
    }

    private int getXForCenteredText(String text) {
        int length = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        return gp.screenWidth/2 - length/2;
    }

    public static boolean openWebpage(URI uri) {
        Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
        if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
            try {
                desktop.browse(uri);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public void exportJSON () {
        int[] saveAgv = {gp.player.x, gp.player.y};

        int n = agentNum;
        int[][] startPos = new int[n][2];
        int[][] endPos = new int[n][2];
        int[] AgentId = new int[n];
        int a = 0;
        for(Agent i : this.gp.agent) {
            startPos[a] = new int[]{i.startPos.x, i.startPos.y};
            endPos[a] = new int[]{i.endPos.x, i.endPos.y};
            AgentId[a] = i.id;
            a++;
        }
        SaveAgent saveAgents = new SaveAgent(startPos, endPos, AgentId);

        SaveMap saveMap = new SaveMap(saveAgv, saveAgents);
        Gson gson = new Gson();
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(System.getProperty("user.home") + "\\Downloads\\output.json"))) {
            bw.write("");
            String saveString = gson.toJson(saveMap, SaveMap.class);
            bw.write(saveString);
            bw.flush();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    public void importJSON() {
        //openFileChooser(fileChooser);
//        fileChooser.setInitialDirectory(
//                new File("C:\\Users\\Admin\\Downloads" + "\\save.json")
//        );
//        File file = fileChooser.showOpenDialog(gp.getWindow());

        Gson gson = new Gson();
        try {
            BufferedReader reader = new BufferedReader(new FileReader("C:\\Users\\Admin\\Downloads\\output.json"));
            SaveMap map = gson.fromJson(reader, SaveMap.class);

            gp.player.x = map.agvPos[0];
            gp.player.y = map.agvPos[1];

            SaveAgent mapAgents = map.agents;
//            while(!gp.agent.isEmpty()) {
//                    Agent.eliminate(gp.agent.get(0));
//                    System.out.println("check");
//            }
            gp.agent.removeAll(gp.agent);
            int n = mapAgents.id.length;
            for(int i = 0; i < n; i++) {
                if(mapAgents.startPos[i] != null) {
                    gp.agent.add(new Agent(gp, new Position(mapAgents.startPos[i][0], mapAgents.startPos[i][1]),
                            new Position(mapAgents.endPos[i][0], mapAgents.endPos[i][1]),  mapAgents.id[i]));
                }
            }
            reader.close();

        }
        catch (Exception e){
            JOptionPane.showMessageDialog(window, "Can not open the file!","Message", JOptionPane.ERROR_MESSAGE);
            JButton checker = new JButton("OK");
            checker.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    gp.gameState = gp.playState;
                    draw(g2);
                }
            });
            gp.add(checker);
        }
    }

}
