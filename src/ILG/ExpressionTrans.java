package ILG;

import java.util.*;
import java.util.regex.Pattern;

public class ExpressionTrans {
    private Map<String, Integer> OperatorPriority = new HashMap<>();
    private Pattern Pat = Pattern.compile("\\+\\+|--|~|!|\\*|/|%|\\+|-|<<|>>|<|<=|>|>=|==|!=|&|\\^|\\||&&|\\|\\||\\?:|=|\\+=|-=|\\*=|/=|%=|<<=|>>=|&=|\\|=|\\^=|,");
    public ExpressionTrans() {
        // 值越大越优先
        OperatorPriority.put("++", 15);
        OperatorPriority.put("--", 15);
        OperatorPriority.put("~", 15);
        OperatorPriority.put("!", 15);;

        OperatorPriority.put("*", 14);
        OperatorPriority.put("/", 14);
        OperatorPriority.put("%", 14);

        OperatorPriority.put("+", 13);
        OperatorPriority.put("-", 13);

        OperatorPriority.put("<<", 12);
        OperatorPriority.put(">>", 12);

        OperatorPriority.put("<", 11);
        OperatorPriority.put("<=", 11);
        OperatorPriority.put(">", 11);
        OperatorPriority.put(">=", 11);

        OperatorPriority.put("==", 10);
        OperatorPriority.put("!=", 10);

        OperatorPriority.put("&", 9);
        OperatorPriority.put("^", 8);
        OperatorPriority.put("|", 7);
        OperatorPriority.put("&&", 6);
        OperatorPriority.put("||", 5);
        OperatorPriority.put("?:", 4);

        OperatorPriority.put("=", 3);
        OperatorPriority.put("+=", 3);
        OperatorPriority.put("-=", 3);
        OperatorPriority.put("*=", 3);
        OperatorPriority.put("/=", 3);
        OperatorPriority.put("%=", 3);
        OperatorPriority.put("<<=", 3);
        OperatorPriority.put(">>=", 3);
        OperatorPriority.put("&=", 3);
        OperatorPriority.put("|=", 3);
        OperatorPriority.put("^=", 3);

        OperatorPriority.put(",", 2);
    }

    public Object Trans(String text) {
        ClearSpaces(text);

        String IP = InversePolish(text);                // 产生逆波兰式
        if (!Check(IP)) return false;           // 利用逆波兰式判断表达式是否合法
        String ans[] = ThreeAddressCode_Quadruple(IP);  // 利用逆波兰式产生三元式和四元式
        String TAC = ans[0];
        String QDP = ans[1];
        String[] res = {IP, TAC, QDP};
        return res;
    }

    // 求逆波兰式
    private String InversePolish(String text) {
        text = ClearSpaces(text); // 清除空格

        Stack<String> Operator = new Stack<>();
        String ans = new String("");
        String op = new String();
        int len = text.length();
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
                if (ans.charAt(ans.length() - 1) == ' ') ans = ans.substring(0, ans.length() - 1); // 处理格式，两个参数间至多一个空格，方便处理
                while (!Operator.isEmpty() && !Operator.peek().equals("(")) ans += " " + Operator.pop();
                if (!Operator.isEmpty() && Operator.peek().equals("(")) Operator.pop();
            } else if (isOperator(Character.toString(ch))) {
                ans += " ";
                op += ch;
                // 处理某些不只一个字符的运算符，如++ --
                if (i + 1 < len && isOperator(Character.toString(text.charAt(i + 1)))) {
                    op += text.charAt(i + 1);
                    ++i;
                }
                // 当前运算符优先级大于栈顶运算符则入栈
                if (Operator.isEmpty() || Operator.peek().equals("(") ||
                        OperatorPriority.get(op) > OperatorPriority.get(Operator.peek())) Operator.push(op);
                else {  // 反之退栈，直至当前运算符优先级不再大于栈顶运算符
                    while (!Operator.isEmpty() && !Operator.peek().equals("(")
                            && OperatorPriority.get(op) < OperatorPriority.get(Operator.peek())) ans += Operator.pop() + " ";
                    Operator.push(op);
                }
                op = "";
            }
        }
        // 清空运算符栈
        while (Operator.size() != 0) ans += " " + Operator.pop();
        ans += "\n";
        return ans;
    }

    // 求三元式和四元式
    private String[] ThreeAddressCode_Quadruple(String IP) {
        Stack<String> TACArgs = new Stack<>();  // 三元式参数栈
        Stack<String> QDPArgs = new Stack<>();  // 四元式参数栈
        String TAC = new String("");
        String QDP = new String("");

        String args = new String("");
        String op = new String("");
        IP += " "; // 由于逆波兰式中各参数间以空格相隔，此处方便得到最后一个三元式
        int i = 0, len = IP.length(), count = 0;
        while (i < len) {
            char ch = IP.charAt(i);
            if (isDigit(ch) || isLetter(ch)) {      // 遇参数直接入栈
                while (isDigit(ch) || isLetter(ch)) {
                    args += ch;
                    ++i;
                    ch = IP.charAt(i);
                }
                TACArgs.push(args);
                QDPArgs.push(args);
                args = "";
            } else if (isOperator(Character.toString(ch))) {            // 遇运算符则弹出参数
                while (isOperator(Character.toString(ch))) {
                    op += ch;
                    ++i;
                    ch = IP.charAt(i);
                }
                // 产生三元式
                ++count;
                if (op.equals("++") || op.equals("--") || op.equals("!") || op.equals("~")) {   // 单目运算符
                    // 三元组
                    String TACFirst = TACArgs.pop();
                    TAC += "(" + count + ") " + "(" + op + ", " + TACFirst + ", _ " + ")\n";

                    // 四元组
                    String QDPFirst = QDPArgs.pop();
                    QDP += "(" + count + ") " + "(" + op + ", " + QDPFirst + ", _ " + ", T" + count + ")\n";
                } else {        // 双目运算符
                    // 三元组
                    String TACSecond = TACArgs.pop();      // 先出栈为第二个运算对象
                    String TACFirst = TACArgs.pop();       // 后出栈为第一个运算对象
                    TAC += "(" + count + ") " + "(" + op + ", " + TACFirst + ", " + TACSecond + ")\n";

                    // 四元组
                    String QDPSecond = QDPArgs.pop();
                    String QDPFirst = QDPArgs.pop();
                    QDP += "(" + count + ") " + "(" + op + ", " + QDPFirst + ", " + QDPSecond + ", T" + count + ")\n";
                }

                TACArgs.push("(" + count + ")");
                QDPArgs.push("T" + count);
                op = "";
            }
            ++i;
        }

        String[] ans = new String[2];
        ans[0] = TAC;
        ans[1] = QDP;
        return ans;
    }

    // 清除空格
    private String ClearSpaces(String text) {
        String ans = new String("");
        int len = text.length();
        for (int i = 0; i < len; ++i) {
            if (text.charAt(i) == ' ') continue;
            ans += text.charAt(i);
        }
        return ans;
    }

    // 检查逆波兰式是否合法
    private boolean Check(String text) {
//        text = ClearSpaces(text);

//        int len = text.length();
//        String op = new String();
//        for (int i = 0; i < len; ++i) {
//            if (text.charAt(i) == '(' || text.charAt(i) == ')') continue;
//            else if (isOperator(Character.toString(text.charAt(i)))){
//                while (i < len && isOperator(Character.toString(text.charAt(i)))) op += text.charAt(i++);
//                --i;
//                if (!isOperator(op)) return false;
//            }
//        }
        return true;
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
    private boolean isOperator(String st) {
        if (Pat.matcher(st).find()) return true;
        return false;
    }
}
