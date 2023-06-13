package ILG;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Panel1 extends JPanel {
    private JLabel inputNote = new JLabel("Expression:");
    private JTextField inputField = new JTextField("Input here", 20);
    private JButton generate = new JButton("Generate");
    private JTextArea Expressions = new JTextArea();
    private JTextArea GrammarTree = new JTextArea();

    public Panel1() {
        // 监听器
        setActionListener();

        // 设置组件布局
        setAppearance();
    }

    private void setActionListener() {
        // 点击按钮后开始产生逆波兰式、三元式、四元式
        generate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (inputField.getText().equals("Input here")) {
                    JOptionPane.showMessageDialog(Panel1.this, "请输入内容！");
                } else {
                    String text = inputField.getText();
                    ExpressionTrans ET = new ExpressionTrans();
                    Object res = ET.Trans(text);
                    if (res instanceof Boolean) {
                        JOptionPane.showMessageDialog(Panel1.this, "表达式不合法！");
                    } else if (res instanceof String[]) {
                        String[] rest = (String[])res;
                        Expressions.setText("逆波兰式：\n" + rest[0] + "\n\n三元式：\n" + rest[1] + "\n四元式：\n" + rest[2]);
                        AbstractGrammarTreeGenerate AGT = new AbstractGrammarTreeGenerate();
                        TreeNode AGTTree = AGT.TreeGenerate(rest[0]);
                        GrammarTree.setText("抽象语法树：\n" + AGTTree.TreeDisplay()
                                + "\n先序遍历：" + AGTTree.PreOrderSequence()
                                + "\n中序遍历：" + AGTTree.InOrderSequence()
                                + "\n后序遍历：" + AGTTree.PostOrderSequence());
                    }
                }
            }
        });

        inputField.addFocusListener(new FocusListener() {
            // 提示信息
            @Override
            public void focusGained(FocusEvent e) {
                if (inputField.getText().equals("Input here")) {
                    inputField.setText("");
                    inputField.setForeground(Color.BLACK);
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (inputField.getText().equals("")) {
                    inputField.setForeground(Color.GRAY);
                    inputField.setText("Input here");
                }
            }
        });
    }

    private void setAppearance() {
        this.setLayout(new GridBagLayout());

        // 提示信息
        GridBagConstraints textGBC = new GridBagConstraints();
        textGBC.insets = new Insets(3, 3, 3, 3);
        inputNote.setFont(new Font("Times New Roman", Font.PLAIN, 18));
        this.add(inputNote, textGBC);

        // 输入栏
        GridBagConstraints inputGBC = new GridBagConstraints();
        inputGBC.insets = new Insets(3, 3, 3, 3);
        inputField.setFont(new Font("Times New Roman", Font.PLAIN,18));
        inputField.setForeground(Color.GRAY);
        this.add(inputField, inputGBC);

        // 按钮
        GridBagConstraints buttonGBC = new GridBagConstraints();
        buttonGBC.insets = new Insets(3, 3, 3, 3);
        buttonGBC.gridwidth = GridBagConstraints.REMAINDER;
        generate.setFont(new Font("Times New Roman", Font.PLAIN, 16));
        this.add(generate, buttonGBC);

        // 表达式区域
        GridBagConstraints ExpGBC = new GridBagConstraints();
        ExpGBC.insets = new Insets(3, 3, 3, 3);
        Expressions.setEditable(false);
        Expressions.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        this.add(Expressions, ExpGBC);

        // 语法树区域
        GridBagConstraints GramGBC = new GridBagConstraints();
        GramGBC.insets = new Insets(3, 3, 3, 3);
        GramGBC.gridwidth = GridBagConstraints.REMAINDER;
        GrammarTree.setEditable(false);
        GrammarTree.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        this.add(GrammarTree, GramGBC);
    }
}
