package ILG;

import java.util.*;

public class TreeNode {
    String val;
    TreeNode left, right;
    String OrderSequence, Display;

    // 构造函数
    public TreeNode(String val, TreeNode left, TreeNode right) {
        this.val = val;
        this.left = left;
        this.right = right;
    }
    public TreeNode(String val) {
        this.val = val;
        this.left = null;
        this.right = null;
    }

    // 打印二叉树的调用接口
    public String TreeDisplay() {
        int maxLevel = GetTreeLevel(this);
        Display = "";
        TreeFormDisplay(Collections.singletonList(this), 1, maxLevel);
        return Display;
    }


    // 递归，打印抽象语法树
    private void TreeFormDisplay(List<TreeNode> Node, int level, int maxLevel) {
        if (Node == null || Node.isEmpty() || isAllElementsNull(Node)) return;

        int floor = maxLevel - level;
        int edgeLines = (int) Math.pow(2, (Math.max(floor - 1, 0)));
        int firstSpaces = (int) Math.pow(2, (floor)) - 1;
        int betweenSpaces = (int) Math.pow(2, (floor + 1)) - 1;
        PrintSpaces(firstSpaces);
        // a = 1 + (1 + 2) * 3 + (1 << 3) / 2
        List<TreeNode> newNode = new ArrayList<>();
        for (TreeNode n : Node) {
            if (n != null) {
                Display += n.val;
                newNode.add(n.left);
                newNode.add(n.right);
            } else {
                newNode.add(null);
                newNode.add(null);
                Display += " ";
            }
            PrintSpaces(betweenSpaces);
        }
        Display += "\n";

        for (int i = 1; i <= edgeLines; i++) {
            for (TreeNode treeNode : Node) {
                PrintSpaces(firstSpaces - i);
                if (treeNode == null) {
                    PrintSpaces(edgeLines + edgeLines + i + 1);
                    continue;
                }
                if (treeNode.left != null) {
                    Display += "/";
                } else {
                    PrintSpaces(1);
                }

                PrintSpaces(i + i - 1);
                if (treeNode.right != null) {
                    Display += "\\";
                } else {
                    PrintSpaces(1);
                }
                PrintSpaces(edgeLines + edgeLines - i);
            }
            Display += "\n";
        }

        TreeFormDisplay(newNode, level + 1, maxLevel);
    }

    // 求树高
    public int GetTreeLevel(TreeNode root) {
        if (root == null) return 0;
        int leftLevel = GetTreeLevel(root.left);
        int rightLevel = GetTreeLevel(root.right);
        return Math.max(leftLevel, rightLevel) + 1;
    }

    // 打印空格，维护格式
    private void PrintSpaces(int count) {
        for (int i = 0; i < count; i++) Display += " ";
    }

    // 判断是否所有元素为空
    private <T> boolean isAllElementsNull(List<T> list) {
        for (Object object : list)
            if (object != null) return false;
        return true;
    }


    // 先序遍历
    public String PreOrderSequence() {      // 先序遍历接口
        OrderSequence = "";
        PreOrder(this);
        return OrderSequence;
    }
    private void PreOrder(TreeNode root) {
        if (root == null) return ;
        OrderSequence += root.val + " ";
        PreOrder(root.left);
        PreOrder(root.right);
    }

    // 中序遍历
    public String InOrderSequence() {       // 中序遍历接口
        OrderSequence = "";
        InOrder(this);
        return OrderSequence;
    }
    private void InOrder(TreeNode root) {
        if (root == null) return ;
        InOrder(root.left);
        OrderSequence += root.val + " ";
        InOrder(root.right);
    }

    // 后序遍历
    public String PostOrderSequence() {      // 后序遍历接口
        OrderSequence = "";
        PostOrder(this);
        return OrderSequence;
    }
    private void PostOrder(TreeNode root) {
        if (root == null) return ;
        PostOrder(root.left);
        PostOrder(root.right);
        OrderSequence += root.val + " ";
    }
}
