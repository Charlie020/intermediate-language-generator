package ILG;

import java.util.*;

public class TreeNode {
    String val;
    TreeNode left;
    TreeNode right;
    String OrderSequence;
    String Display;

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

    // 打印二叉树
    public String TreeDisplay() {
        int maxLevel = GetTreeLevel(this);
        Display = "";
        TreeFormDisplay(Collections.singletonList(this), 1, maxLevel);
        return Display;
    }


    private void TreeFormDisplay(List<TreeNode> Node, int level, int maxLevel) {
        if (Node == null || Node.isEmpty() || isAllElementsNull(Node)) return;

        int floor = maxLevel - level;
        int edgeLines = (int) Math.pow(2, (Math.max(floor - 1, 0)));
        int firstSpaces = (int) Math.pow(2, (floor)) - 1;
        int betweenSpaces = (int) Math.pow(2, (floor + 1)) - 1;
        PrintSpaces(firstSpaces);

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

    public int GetTreeLevel(TreeNode root) {
        if (root == null) return 0;
        int leftLevel = GetTreeLevel(root.left);
        int rightLevel = GetTreeLevel(root.right);
        return Math.max(leftLevel, rightLevel) + 1;
    }

    private void PrintSpaces(int count) {
        for (int i = 0; i < count; i++) Display += " ";
    }

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
        OrderSequence += root.val + " ";
        PostOrder(root.left);
        PostOrder(root.right);
    }
}
