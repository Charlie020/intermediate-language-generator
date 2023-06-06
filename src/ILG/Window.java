package ILG;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Window extends JFrame {
    public JButton confirm = new JButton("确定");
    public JTextField input = new JTextField("Input here", 20);

    public Window() {
        // 设置组件布局
        setLayout(new FlowLayout());
        input.setPreferredSize(new Dimension(200, 30));
        input.setFont(new Font("Arial", Font.PLAIN,16));
        confirm.setSize(200, 60);

        // 添加组件
        this.add(input);
        this.add(confirm);

        // 监听器
        confirm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        input.addFocusListener(new FocusListener() {
            // 提示信息
            @Override
            public void focusGained(FocusEvent e) {
                if (input.getText().equals("Input here")) {
                    input.setText("");
                    input.setForeground(Color.BLACK);
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (input.getText().equals("")) {
                    input.setForeground(Color.GRAY);
                    input.setText("Input here");
                }
            }
        });

        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                // 在窗口关闭时执行程序终止操作
                System.exit(0);
            }
        });

        // 其他设置
        this.setBounds(400, 200, 800, 600);
        this.setTitle("Intermediate Language Generator");
        this.setVisible(true);
    }
}
