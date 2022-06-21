package main;

import javax.swing.JFrame;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        JFrame window=new JFrame();
        window.getDefaultCloseOperation();
        window.setResizable(false);
        window.setTitle("Happy Hospital - Em ham mo anh Son");

        GamePanel gamePanel=new GamePanel();
        window.add(gamePanel);
        window.pack();


        window.setLocationRelativeTo(null);
        window.setVisible(true);
        gamePanel.startGameThread();

    }

}
