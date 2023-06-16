package ILG;

import java.util.Stack;

public class AbstractGrammarTreeGenerate {
    public TreeNode TreeGenerate(String IP) {
        ExpressionTrans ET = new ExpressionTrans();

        Stack<TreeNode> Tree = new Stack<>();
        IP += " ";
        int i = 0, len = IP.length();
        String tmp = "";
        while (i < len) {
            if (IP.charAt(i) == ' ') {
                if (!ET.isOperator(tmp)) {
                    TreeNode root = new TreeNode(tmp);
                    Tree.push(root);
                } else if (ET.isOperator(tmp)) { // 1 2 + 3 =
                    if (ET.isSingleOperator(tmp)) {
                        if (tmp.equals("++_") || tmp.equals("--_")) {
                            TreeNode right = Tree.pop();
                            TreeNode root = new TreeNode(tmp.substring(0, 2), new TreeNode("_"), right);
                            Tree.push(root);
                        } else {
                            TreeNode left = Tree.pop();
                            if (tmp.equals("_++") || tmp.equals("_--")) {
                                TreeNode root = new TreeNode(tmp.substring(1, 3), left, new TreeNode("_"));
                                Tree.push(root);
                            }
                            else {
                                TreeNode root = new TreeNode(tmp.substring(1, 3), left, new TreeNode("_"));
                                Tree.push(root);
                            }
                        }
                    } else if (ET.isBinaryOperator(tmp)) {
                        TreeNode right = Tree.pop();
                        TreeNode left = Tree.pop();
                        TreeNode root = new TreeNode(tmp, left, right);
                        Tree.push(root);
                    }
                }
                tmp = "";
            } else {
                tmp += IP.charAt(i);
            }
            ++i;
        }
        return Tree.peek();
    }
}
