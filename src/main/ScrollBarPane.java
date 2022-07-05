package main;

import javax.swing.*;
import java.awt.*;

public class ScrollBarPane extends JFrame{

        public JTextArea ta;
        JFrame frame;

        public ScrollBarPane(){

        JFrame frame = new JFrame("AutoAgv Deadline");
        frame.setLayout(new FlowLayout());

        ta = new JTextArea(30, 20);
        ta.setLineWrap(true);
        ta.setWrapStyleWord(true);

        JScrollPane sp = new JScrollPane(ta, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

        frame.add(sp);

        frame.setSize(300, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        }
}

