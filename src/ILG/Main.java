package ILG;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Intermediate Language Generator");
        Panel1 panel1 = new Panel1();

        frame.add(panel1);

        // 其他设置
        frame.setBounds(500, 200, 800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
