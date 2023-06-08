package ILG;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Window extends JFrame {
    private JButton confirm = new JButton("确定");
    private JTextField input = new JTextField("Input here", 20);
    private JTextArea outputArea = new JTextArea();

    public Window() {
        // 设置组件布局
        this.setLayout(new GridBagLayout());
        input.setPreferredSize(new Dimension(200, 30));
        input.setFont(new Font("Arial", Font.PLAIN,16));
        input.setForeground(Color.GRAY);
        confirm.setSize(200, 60);
        outputArea.setEditable(false);

        // 添加组件
        this.add(input);
        this.add(confirm);
        this.add(outputArea);

        // 监听器
        confirm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {           // 点击按钮后开始产生逆波兰式、三元式、四元式
                if (input.getText().equals("Input here")) {
                    JOptionPane.showMessageDialog(Window.this, "请输入内容！");
                } else {
                    String text = input.getText();
                    ExpressionTrans ET = new ExpressionTrans();
                    Object res = ET.Trans(text);
                    if (res instanceof Boolean) {
                        JOptionPane.showMessageDialog(Window.this, "表达式不合法！");
                    } else if (res instanceof String[]) {
                        String[] rest = (String[])res;
                        outputArea.setText("逆波兰式：\n" + rest[0] + "\n\n三元式：\n" + rest[1] + "\n四元式：\n" + rest[2]);
                    }
                }
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
