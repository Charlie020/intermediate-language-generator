package ILG;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        Panel1 panel1 = new Panel1();

        frame.add(panel1, BorderLayout.CENTER);

        // 窗口设置
        frame.setTitle("Intermediate Language Generator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1100, 900);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
