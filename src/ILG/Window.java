package ILG;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Window extends JFrame {
    public Window() {

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                // 在窗口关闭时执行程序终止操作
                System.exit(0);
            }
        });

        this.setBounds(400, 200, 800, 600);
        this.setTitle("IntermediateLanguageGenerator");
        this.setVisible(true);
    }
}
