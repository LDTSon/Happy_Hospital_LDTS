package main;
import entity.Agent;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Objects;
import com.google.gson.Gson;
import entity.Entity;
import gameAlgo.Position;


import javax.swing.*;

import static java.awt.image.BufferedImage.TYPE_INT_ARGB;
public class UtilityTool {
    private static GamePanel gp;

    public UtilityTool(GamePanel gp) {
        UtilityTool.gp = gp;
    }

    public BufferedImage scaleImage(BufferedImage original, int width, int height, String type) {
        Color bgColor = null;
        if(Objects.equals(type, "entity")) bgColor = null;
        else if(Objects.equals(type, "door")) bgColor = null;
        else if(Objects.equals(type, "tilesMap")) bgColor = new Color(233, 233, 233);
        BufferedImage scaledImage = new BufferedImage(width, height, TYPE_INT_ARGB);
        Graphics2D g2 = scaledImage.createGraphics();
        g2.drawImage(original, 0, 0, width, height, bgColor, null);
        g2.dispose();

        return scaledImage;
    }

    public static void exportJSON () {
        int[] agvPos = {gp.player.x, gp.player.y};
        int agvImage = 0;
        if (gp.player.agvImage[0].equals(gp.player.entityImage)) {
            agvImage = 0;
        } else if (gp.player.agvImage[1].equals(gp.player.entityImage)) {
            agvImage = 1;
        } else if (gp.player.agvImage[2].equals(gp.player.entityImage)) {
            agvImage = 2;
        } else if (gp.player.agvImage[3].equals(gp.player.entityImage)) {
            agvImage = 3;
        }
        int [] agvDesPos = {gp.player.goalPos.x, gp.player.goalPos.y};
        SaveAgv agv = new SaveAgv(agvPos, agvImage, agvDesPos, gp.player.start, gp.player.expectedTime, gp.player.goalReached);
        int n = gp.agents.size();
        int[][] startPos = new int[n][2];
        int[][] endPos = new int[n][2];
        int[] AgentId = new int[n];
        int a = 0;
        for(Agent i : gp.agents) {
            startPos[a] = new int[]{i.startPos.x, i.startPos.y};
            endPos[a] = new int[]{i.endPos.x, i.endPos.y};
            AgentId[a] = i.id;
            a++;
        }
        SaveAgent saveAgents = new SaveAgent(startPos, endPos, AgentId);

        SaveMap saveMap = new SaveMap(gp.timer.secondsSinceStart, Entity.harmfulness, agv, saveAgents);
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

        JOptionPane.showMessageDialog(null, "Game saved!", "Message dialog", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void importJSON() {
        Gson gson = new Gson();
        try {
            BufferedReader reader = new BufferedReader(new FileReader("C:\\Users\\Admin\\Downloads\\output.json"));
            SaveMap map = gson.fromJson(reader, SaveMap.class);

            gp.timer.secondsSinceStart = map.time;
            Entity.harmfulness = map.harmfulness;

            gp.player.x = map.agv.agvPos[0];
            gp.player.y = map.agv.agvPos[1];
            gp.player.entityImage = gp.player.agvImage[map.agv.agvImage];
            gp.player.goalPos.x = map.agv.agvDes[0];
            gp.player.goalPos.y = map.agv.agvDes[1];
            gp.player.start = map.agv.agvStart;
            gp.player.expectedTime = map.agv.expectedTime;
            gp.player.goalReached = map.agv.goalReached;

            SaveAgent mapAgents = map.agents;

            gp.agents.removeAll(gp.agents);
            int n = mapAgents.id.length;
            Agent.agentNum = n;
            for(int i = 0; i < n; i++) {
                if(mapAgents.startPos[i] != null) {
                    gp.agents.add(new Agent(gp, new Position(mapAgents.startPos[i][0], mapAgents.startPos[i][1]),
                            new Position(mapAgents.endPos[i][0], mapAgents.endPos[i][1]),  mapAgents.id[i]));
                }
            }
            reader.close();
            System.out.println("loead");
            gp.gameState = gp.playState;
        }
        catch (Exception e){
            JOptionPane.showMessageDialog(Main.window, "Can not open the file!","Message", JOptionPane.ERROR_MESSAGE);
            JButton checker = new JButton("OK");
            checker.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    gp.gameState = gp.playState;
                    //draw(gp.ui.g2);
                }
            });
            gp.add(checker);
        }
    }
}

class SaveAgent {
    public int[][] startPos;
    public int[][] endPos;
    public int[] id;

    public SaveAgent (int[][] startPos, int[][] endPos, int[] id) {
        this.startPos = startPos;
        this.endPos = endPos;
        this.id = id;
    }
}

class SaveAgv {
    public int[] agvPos;
    public int agvImage;
    public int[] agvDes;
    public double agvStart;
    int expectedTime;
    public int goalReached;

    public SaveAgv(int[] agvPos, int agvImage, int[] agvDes, double agvStart, int expectedTime, int goalReached) {
        this.expectedTime = expectedTime;
        this.agvStart = agvStart;
        this.agvPos = agvPos;
        this.agvImage = agvImage;
        this.agvDes = agvDes;
        this.goalReached = goalReached;
    }
}

class SaveMap {

    public double time;
    public double harmfulness;
    public SaveAgv agv;
    public SaveAgent agents;

    public SaveMap(double time, double harmfulness, SaveAgv agv, SaveAgent agents){
        this.harmfulness = harmfulness;
        this.time = time;
        this.agv = agv;
        this.agents = agents;
    }
}
