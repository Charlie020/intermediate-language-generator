package ILG;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Panel1 extends JPanel {
    private JButton confirm = new JButton("确定");
    private JTextField input = new JTextField("Input here", 20);
    private JTextArea outputArea = new JTextArea();

    public Panel1() {
        // 设置组件布局
        this.setLayout(new GridBagLayout());

        // 监听器
        setActionListener();

        // 外观
        setAppearance();
    }

    private void setActionListener() {
        // 点击按钮后开始产生逆波兰式、三元式、四元式
        confirm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (input.getText().equals("Input here")) {
                    JOptionPane.showMessageDialog(Panel1.this, "请输入内容！");
                } else {
                    String text = input.getText();
                    ExpressionTrans ET = new ExpressionTrans();
                    Object res = ET.Trans(text);
                    if (res instanceof Boolean) {
                        JOptionPane.showMessageDialog(Panel1.this, "表达式不合法！");
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
    }

    private void setAppearance() {
        input.setPreferredSize(new Dimension(200, 30));
        input.setFont(new Font("微软雅黑", Font.PLAIN,16));
        input.setForeground(Color.GRAY);
        confirm.setSize(200, 60);
        outputArea.setEditable(false);
        outputArea.setFont(new Font("微软雅黑", Font.PLAIN, 14));

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        this.add(input, constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = 2;
        this.add(outputArea, constraints);

        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.gridwidth = 1;
        this.add(confirm, constraints);
    }
}
