package ILG;

import java.util.*;

public class ExpressionTrans {
    private final Set<String> SingleOperator = new HashSet<>();
    private final Set<String> BinaryOperator = new HashSet<>();
    private final Set<String> TernaryOperator = new HashSet<>();
    private final Map<String, Integer> OperatorPriority = new HashMap<>();

    public ExpressionTrans() {
        initOperator();
    }

    public Object Trans(String text) {
        ClearSpaces(text);
        String IP = InversePolish(text);                // 产生逆波兰式
        if (!Check(IP)) return false;           // 利用逆波兰式判断表达式是否合法
        String[] ans = ThreeAddressCode_Quadruple(IP);  // 利用逆波兰式产生三元式和四元式
        String TAC = ans[0];
        String QDP = ans[1];
        return new String[]{IP, TAC, QDP};
    }

    // 求逆波兰式
    private String InversePolish(String text) {
        text = ClearSpaces(text); // 清除空格

        Stack<String> Operator = new Stack<>();
        StringBuilder ans = new StringBuilder();
        String op = "";
        int len = text.length();

        for (int i = 0; i < len; ++i) {
            char ch = text.charAt(i);
            if (isDigit(ch) || isLetter(ch)) {
                // 数字直接加入结果
                ans.append(ch);
            } else if (ch == '(') {
                // 左括号直接入栈
                Operator.push(Character.toString(ch));
            } else if (ch == ')') {
                // 右括号则退栈至左括号为止
                if (ans.charAt(ans.length() - 1) == ' ') ans = new StringBuilder(ans.substring(0, ans.length() - 1)); // 处理格式，两个参数间至多一个空格，方便处理
                while (!Operator.isEmpty() && !Operator.peek().equals("(")) ans.append(" ").append(Operator.pop());
                if (!Operator.isEmpty() && Operator.peek().equals("(")) Operator.pop();
            } else if (isOperator(Character.toString(ch))) {
                ans.append(" ");
                op += ch;
                // 处理某些不只一个字符的运算符
                if (i + 1 < len && isOperator(Character.toString(text.charAt(i + 1)))) {
                    char cht = text.charAt(i + 1);
                    if (isOperator(op + cht)) { // 处理两个字符的运算符
                        op += cht;
                        ++i;
                        if (i + 1 < len && isOperator(Character.toString(text.charAt(i + 1)))) { // 处理三个字符的运算符
                            char chtTmp = text.charAt(i + 1);
                            if (isOperator(op + chtTmp)) {
                                op += chtTmp;
                                ++i;
                            }
                        }
                    }
                }
                // 当前运算符优先级大于栈顶运算符则入栈
                if (Operator.isEmpty() || Operator.peek().equals("(") ||
                        OperatorPriority.get(op) > OperatorPriority.get(Operator.peek())) Operator.push(op);
                else {  // 反之退栈，直至当前运算符优先级不再大于栈顶运算符
                    while (!Operator.isEmpty() && !Operator.peek().equals("(")
                            && OperatorPriority.get(op) < OperatorPriority.get(Operator.peek())) ans.append(Operator.pop()).append(" ");
                    Operator.push(op);
                }
                op = "";
            }
        }

        // 处理格式
        int idx = ans.length() - 1;
        while (idx >= 0 && ans.charAt(idx) == ' ') idx--;
        ans = new StringBuilder(ans.substring(0, idx + 1));
        // 清空运算符栈
        while (Operator.size() != 0) ans.append(" ").append(Operator.pop());
        return ans.toString();
    }

    // 求三元式和四元式
    private String[] ThreeAddressCode_Quadruple(String IP) {
        Stack<String> TACArgs = new Stack<>();  // 三元式参数栈
        Stack<String> QDPArgs = new Stack<>();  // 四元式参数栈
        StringBuilder TAC = new StringBuilder();
        StringBuilder QDP = new StringBuilder();

        String args = "";
        StringBuilder op = new StringBuilder();
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
                    op.append(ch);
                    ++i;
                    ch = IP.charAt(i);
                }
                // 产生三元式
                ++count;
                if (SingleOperator.contains(op.toString())) {   // 单目运算符
                    // 三元组
                    String TACFirst = TACArgs.pop();
                    TAC.append("(").append(count).append(") ").append("(").append(op).append(", ").append(TACFirst).append(", _ ").append(")\n");

                    // 四元组
                    String QDPFirst = QDPArgs.pop();
                    QDP.append("(").append(count).append(") ").append("(").append(op).append(", ").append(QDPFirst).append(", _ ").append(", T").append(count).append(")\n");
                } else {        // 双目运算符
                    // 三元组
                    String TACSecond = TACArgs.pop();      // 先出栈为第二个运算对象
                    String TACFirst = TACArgs.pop();       // 后出栈为第一个运算对象
                    TAC.append("(").append(count).append(") ").append("(").append(op).append(", ").append(TACFirst).append(", ").append(TACSecond).append(")\n");

                    // 四元组
                    String QDPSecond = QDPArgs.pop();
                    String QDPFirst = QDPArgs.pop();
                    QDP.append("(").append(count).append(") ").append("(").append(op).append(", ").append(QDPFirst).append(", ").append(QDPSecond).append(", T").append(count).append(")\n");
                }

                TACArgs.push("(" + count + ")");
                QDPArgs.push("T" + count);
                op = new StringBuilder();
            }
            ++i;
        }

        String[] ans = new String[2];
        ans[0] = TAC.toString();
        ans[1] = QDP.toString();
        return ans;
    }

    // 清除空格
    private String ClearSpaces(String text) {
        StringBuilder ans = new StringBuilder();
        int len = text.length();
        for (int i = 0; i < len; ++i) {
            if (text.charAt(i) == ' ') continue;
            ans.append(text.charAt(i));
        }
        return ans.toString();
    }

    // 检查逆波兰式是否合法
    private boolean Check(String IP) {
        IP += " ";
        int i = 0, len = IP.length();
        String tmp = "";
        Stack<String> args = new Stack<>();

        while (i < len) {
            if (IP.charAt(i) == ' ') {
                if (!isOperator(tmp)) {
                    args.push(tmp);
                } else {
                    if (SingleOperator.contains(tmp) && args.size() < 1) return false;
                    else if (BinaryOperator.contains(tmp)) {
                        if (args.size() < 2) return false;
                        args.pop();
                    } else if (TernaryOperator.contains(tmp)) {
                        if (args.size() < 3) return false;
                        args.pop();
                        args.pop();
                    }
                }
                ++i;
                tmp = "";
            } else {
                tmp += IP.charAt(i++);
            }
        }
        return args.size() == 1;
    }

    private boolean isDigit(char ch) {
        return ch >= '0' && ch <= '9';
    }
    private boolean isLetter(char ch) {
        if (ch >= 'a' && ch <= 'z') return true;
        return ch >= 'A' && ch <= 'Z';
    }
    private boolean isOperator(String st) {
        return SingleOperator.contains(st) || BinaryOperator.contains(st)
                || TernaryOperator.contains(st);
    }

    private void initOperator() {
        // 初始化运算符
        SingleOperator.add("++");
        SingleOperator.add("--");
        SingleOperator.add("~");
        SingleOperator.add("!");

        BinaryOperator.add("*");
        BinaryOperator.add("/");
        BinaryOperator.add("%");
        BinaryOperator.add("+");
        BinaryOperator.add("-");
        BinaryOperator.add("<<");
        BinaryOperator.add(">>");
        BinaryOperator.add("<");
        BinaryOperator.add("<=");
        BinaryOperator.add(">");
        BinaryOperator.add(">=");
        BinaryOperator.add("==");
        BinaryOperator.add("!=");
        BinaryOperator.add("&");
        BinaryOperator.add("^");
        BinaryOperator.add("|");
        BinaryOperator.add("&&");
        BinaryOperator.add("||");
        BinaryOperator.add("=");
        BinaryOperator.add("+=");
        BinaryOperator.add("-=");
        BinaryOperator.add("*=");
        BinaryOperator.add("/=");
        BinaryOperator.add("%=");
        BinaryOperator.add("<<=");
        BinaryOperator.add(">>=");
        BinaryOperator.add("&=");
        BinaryOperator.add("|=");
        BinaryOperator.add("^=");
        BinaryOperator.add(",");

        TernaryOperator.add("?:");

        // 优先级值越大越优先
        OperatorPriority.put("++", 15);
        OperatorPriority.put("--", 15);
        OperatorPriority.put("~", 15);
        OperatorPriority.put("!", 15);

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
}
