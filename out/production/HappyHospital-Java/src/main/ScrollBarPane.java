package main;

import javax.swing.*;
import java.awt.*;

public class ScrollBarPane extends JFrame{

    public JTextArea ta;
    //JFrame frame;

    public ScrollBarPane(){

        JFrame frame = new JFrame("AGV DEADLINE");
        frame.setLayout(new FlowLayout());

        ta = new JTextArea(30, 36);
        ta.setLineWrap(true);
        ta.setWrapStyleWord(true);
        ta.setFont(new Font("Monospaced", Font.ITALIC, 16));
        ta.setForeground(Color.BLACK);
        ta.setBackground(Color.gray);

        JScrollPane sp = new JScrollPane(ta, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

        frame.add(sp);

        frame.setSize(400, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}