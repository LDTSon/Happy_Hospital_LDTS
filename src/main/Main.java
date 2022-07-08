package main;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, FontFormatException {
        JFrame window = new JFrame();
        window.getDefaultCloseOperation();
        window.setResizable(false);
        window.setTitle("Sơn gaming và fanclub");
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        GamePanel gamePanel = new GamePanel();

        window.add(gamePanel);

        window.pack();


        window.setLocationRelativeTo(null);
        window.setVisible(true);
        gamePanel.startGameThread();

    }

}
