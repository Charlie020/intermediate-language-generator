package ILG;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class ExpressionTrans {
    Map<String, Integer> OperatorPriority = new HashMap<>();
    public ExpressionTrans() {
        OperatorPriority.put("!", 6);
        OperatorPriority.put("++", 6);
        OperatorPriority.put("--", 6);
        OperatorPriority.put("~", 6);

        OperatorPriority.put("*", 5);
        OperatorPriority.put("/", 5);
        OperatorPriority.put("%", 5);

        OperatorPriority.put("+", 4);
        OperatorPriority.put("-", 4);

        OperatorPriority.put("<<", 3);
        OperatorPriority.put(">>", 3);

        OperatorPriority.put("==", 2);
        OperatorPriority.put("!=", 2);

        OperatorPriority.put("&", 1);
        OperatorPriority.put("|", 1);
        OperatorPriority.put("^", 1);
        OperatorPriority.put("&&", 1);
        OperatorPriority.put("||", 1);

        OperatorPriority.put("=", 0);
        OperatorPriority.put("+=", 0);
        OperatorPriority.put("-=", 0);
        OperatorPriority.put("*=", 0);
        OperatorPriority.put("/=", 0);
        OperatorPriority.put("%=", 0);
    }

    public String ClearSpaces(String text) {
        String ans = new String("");
        int len = text.length();
        for (int i = 0; i < len; ++i) {
            if (text.charAt(i) == ' ') continue;
            ans += text.charAt(i);
        }
        return ans;
    }

    // 求逆波兰式
    public String InversePolish(String text) {
        text = ClearSpaces(text);
        String ans = new String("");
        Stack<String> Operator = new Stack<>();

        int len = text.length();
        String op = new String();
        for (int i = 0; i < len; ++i) {
            char ch = text.charAt(i);
            if (isDigit(ch) || isLetter(ch)) {
                // 数字直接加入结果
                ans += ch;
            } else if (ch == '(') {
                // 左括号直接入栈
                Operator.push(Character.toString(ch));
            } else if (ch == ')') {
                // 右括号则退栈至左括号为止
                if (ans.charAt(ans.length() - 1) == ' ') ans = ans.substring(0, ans.length() - 1);
                while (!Operator.isEmpty() && !Operator.peek().equals("(")) ans += " " + Operator.pop();
                if (!Operator.isEmpty() && Operator.peek().equals("(")) Operator.pop();
            } else if (isOperator(ch)) {
                // 运算符
                ans += " ";
                while (i < len && isOperator(text.charAt(i))) {
                    op += text.charAt(i);
                    ++i;
                }
                --i;
                // 当前运算符优先级大于栈顶运算符则入栈，反之退栈，直至当前运算符优先级不再大于栈顶运算符
                if (Operator.isEmpty() || Operator.peek().equals("(") ||
                        OperatorPriority.get(op) > OperatorPriority.get(Operator.peek())) Operator.push(op);
                else {
                    while (!Operator.isEmpty() && !Operator.peek().equals("(")
                            && OperatorPriority.get(op) < OperatorPriority.get(Operator.peek())) ans += Operator.pop() + " ";
                    Operator.push(op);
                }
                op = "";
            }
        }
        // 清空运算符栈
        while (Operator.size() != 0) ans += " " + Operator.pop();
        return ans;
    }

    private boolean isDigit(char ch) {
        if (ch >= '0' && ch <= '9') return true;
        return false;
    }
    private boolean isLetter(char ch) {
        if (ch >= 'a' && ch <= 'z') return true;
        if (ch >= 'A' && ch <= 'Z') return true;
        return false;
    }
    private boolean isOperator(char ch) {
        if (ch == '+' || ch == '-' || ch == '*' || ch == '/'
        || ch == '=' || ch == '!') return true;
        return false;
    }
}
