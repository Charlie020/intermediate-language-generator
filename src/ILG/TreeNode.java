package ILG;

import java.util.*;

public class TreeNode {
    String val = "";
    TreeNode left;
    TreeNode right;
    String OrderSequence = "";

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
    public void TreeDisplay() {
        // 将二叉树转化为字符串数组如：1+2=3的二叉树为 ArrayTree = ["=", "+", "3", "1", "2", null, null, null, null, null, null];
//        String[] ArrayTree = TreeToArray(this);
        //
//        TreeNode newTree = constructTree(ArrayTree);
        printTree(this);
    }

//    private String[] TreeToArray(TreeNode root) {
//        if (root == null) return new String[0];
//        List<String> res = new ArrayList<>();
//        Queue<TreeNode> que = new LinkedList<>();
//        que.offer(root);
//        while(!que.isEmpty()) {
//            int size = que.size();
//            for (int i = 0; i < size; ++i) {
//                TreeNode node = que.poll();
//                if (node == null) {
//                    res.add(null);
//                } else {
//                    res.add(node.val);
//                    que.offer(node.left);
//                    que.offer(node.right);
//                }
//            }
//        }
//        return res.toArray(new String[0]);
//    }
//
//    public TreeNode constructTree(String[] array) {
//        if (array == null || array.length == 0 || array[0] == null) {
//            return null;
//        }
//
//        int index = 0;
//        int length = array.length;
//
//        TreeNode root = new TreeNode(array[0]);
//        Deque<TreeNode> nodeQueue = new LinkedList<>();
//        nodeQueue.offer(root);
//        TreeNode currNode;
//        while (index < length) {
//            index++;
//            if (index >= length) {
//                return root;
//            }
//            currNode = nodeQueue.poll();
//            String leftChild = array[index];
//            if (leftChild != null) {
//                currNode.left = new TreeNode(leftChild);
//                nodeQueue.offer(currNode.left);
//            }
//            index++;
//            if (index >= length) {
//                return root;
//            }
//            String rightChild = array[index];
//            if (rightChild != null) {
//                currNode.right = new TreeNode(rightChild);
//                nodeQueue.offer(currNode.right);
//            }
//        }
//
//        return root;
//    }

    public void printTree(TreeNode root) {
        int maxLevel = getTreeDepth(root);
        printNodeInternal(Collections.singletonList(root), 1, maxLevel);
    }

    private void printNodeInternal(List<TreeNode> nodes, int level, int maxLevel) {
        if (nodes == null || nodes.isEmpty() || isAllElementsNull(nodes)) {
            return;
        }

        int floor = maxLevel - level;
        int endgeLines = (int) Math.pow(2, (Math.max(floor - 1, 0)));
        int firstSpaces = (int) Math.pow(2, (floor)) - 1;
        int betweenSpaces = (int) Math.pow(2, (floor + 1)) - 1;

        printWhitespaces(firstSpaces);

        List<TreeNode> newNodes = new ArrayList<TreeNode>();
        for (TreeNode node : nodes) {
            if (node != null) {
                System.out.print(node.val);
                newNodes.add(node.left);
                newNodes.add(node.right);
            } else {
                newNodes.add(null);
                newNodes.add(null);
                System.out.print(" ");
            }

            printWhitespaces(betweenSpaces);
        }
        System.out.println("");

        for (int i = 1; i <= endgeLines; i++) {
            for (int j = 0; j < nodes.size(); j++) {
                printWhitespaces(firstSpaces - i);
                if (nodes.get(j) == null) {
                    printWhitespaces(endgeLines + endgeLines + i + 1);
                    continue;
                }

                if (nodes.get(j).left != null) {
                    System.out.print("/");
                } else {
                    printWhitespaces(1);
                }

                printWhitespaces(i + i - 1);
                if (nodes.get(j).right != null) {
                    System.out.print("\\");
                } else {
                    printWhitespaces(1);
                }
                printWhitespaces(endgeLines + endgeLines - i);
            }

            System.out.println("");
        }

        printNodeInternal(newNodes, level + 1, maxLevel);
    }

    public static int getTreeDepth(TreeNode root) {
        if (root == null) return 0;
        return 1 + Math.max(getTreeDepth(root.left), getTreeDepth(root.right));
    }

    private static void printWhitespaces(int count) {
        for (int i = 0; i < count; i++) {
            System.out.print(" ");
        }
    }

    private static <T> boolean isAllElementsNull(List<T> list) {
        for (Object object : list)
            if (object != null) return false;
        return true;
    }



    // 先序遍历
    public String PreOrderSequence() {      // 先序遍历接口
        if (this == null) return "";
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
        if (this == null) return "";
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
        if (this == null) return "";
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
