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
            public void actionPerformed(ActionEvent e) {
                if (input.getText().equals("Input here")) {
                    JOptionPane.showMessageDialog(Window.this, "请输入内容！");
                } else {
                    String text = input.getText();
                    text = ClearSpaces(text);

                    ExpressionTrans ET = new ExpressionTrans();
                    String IP = ET.InversePolish(text);                // 产生逆波兰式
                    String ans[] = ET.ThreeAddressCode_Quadruple(IP);  // 利用逆波兰式产生三元式和四元式
                    String TAC = ans[0];
                    String QDP = ans[1];
                    outputArea.setText("逆波兰式：\n" + IP + "\n三元式：\n" + TAC + "\n四元式：\n" + QDP);
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

    private String ClearSpaces(String text) {
        String ans = new String("");
        int len = text.length();
        for (int i = 0; i < len; ++i) {
            if (text.charAt(i) == ' ') continue;
            ans += text.charAt(i);
        }
        return ans;
    }
}
