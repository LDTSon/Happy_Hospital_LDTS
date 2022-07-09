package main;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static JFrame window;
    public static void main(String[] args) {
        window = new JFrame();
        window.setResizable(false);
        window.setTitle("Happy Hospital - Em ham mo anh Son");
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        GamePanel gamePanel=new GamePanel();
        window.add(gamePanel);
        window.pack();


        window.setLocationRelativeTo(null);
        window.setVisible(true);
        gamePanel.startGameThread();

    }

}
