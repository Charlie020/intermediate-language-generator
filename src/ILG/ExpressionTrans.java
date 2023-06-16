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
                if (ans.length() >= 1 && ans.charAt(ans.length() - 1) == ' ') ans = new StringBuilder(ans.substring(0, ans.length() - 1));
                if (ans.length() >= 1) ans.append(" ");
                op += ch;
                // 处理某些不只一个字符的运算符
                if (i + 1 < len && isOperator(Character.toString(text.charAt(i + 1)))) {
                    char cht = text.charAt(i + 1);
                    if (isOperator(op + cht)) { // 处理两个字符的运算符
                        if ((op + cht).equals("++") || (op + cht).equals("--")) {
                            if (i - 1 >= 0) {  // a++
                                if (isLetter(text.charAt(i - 1))) {
                                    op = "_" + op + cht;
                                    ++i;
                                } else if (i + 2 < len && isLetter(text.charAt(i + 2))) {
                                    op = op + cht + "_";
                                    ++i;
                                }
                            } else {   // ++a
                                op = op + cht + "_";
                                ++i;
                            }
                        } else {
                            op += cht;
                            ++i;
                            if (i + 1 < len) {
                                char chtTmp = text.charAt(i + 1);
                                if (isOperator(op + chtTmp)) {
                                    op += chtTmp;
                                    ++i;
                                }
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
        while (ans.length() >= 1 && idx >= 0 && ans.charAt(idx) == ' ') idx--;
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

        IP += " "; // 由于逆波兰式中各参数间以空格相隔，此处方便得到最后一个三元式
        int i = 0, len = IP.length(), count = 0;
        String tmp = "";
        while (i < len) {
            if (IP.charAt(i) == ' ') {      // 遇参数直接入栈
                if (!isOperator(tmp)) {
                    TACArgs.push(tmp);
                    QDPArgs.push(tmp);
                } else if (isOperator(tmp)) {
                    // 产生三元式
                    ++count;
                    if (isSingleOperator(tmp)) {   // 单目运算符
                        // 三元组
                        String TACFirst = TACArgs.pop();
                        if (tmp.equals("_++") || tmp.equals("_--")) TAC.append("(").append(count).append(") ").append("(").append(tmp.substring(1, 3)).append(", ").append(TACFirst).append(", _ ").append(")\n");
                        else if (tmp.equals("++_") || tmp.equals("--_")) TAC.append("(").append(count).append(") ").append("(").append(tmp.substring(0, 2)).append(",").append(" _ , ").append(TACFirst).append(")\n");
                        else TAC.append("(").append(count).append(") ").append("(").append(tmp).append(", ").append(TACFirst).append(", _ ").append(")\n");

                        // 四元组
                        String QDPFirst = QDPArgs.pop();
                        if (tmp.equals("_++") || tmp.equals("_--")) QDP.append("(").append(count).append(") ").append("(").append(tmp.substring(1, 3)).append(", ").append(QDPFirst).append(", _ ").append(", T").append(count).append(")\n");
                        else if (tmp.equals("++_") || tmp.equals("--_")) QDP.append("(").append(count).append(") ").append("(").append(tmp.substring(0, 2)).append(",").append(" _ , ").append(QDPFirst).append(", T").append(count).append(")\n");
                        else QDP.append("(").append(count).append(") ").append("(").append(tmp).append(", ").append(QDPFirst).append(", _ ").append(", T").append(count).append(")\n");
                    } else if (isBinaryOperator(tmp)) {        // 双目运算符
                        // 三元组
                        String TACSecond = TACArgs.pop();      // 先出栈为第二个运算对象
                        String TACFirst = TACArgs.pop();       // 后出栈为第一个运算对象
                        TAC.append("(").append(count).append(") ").append("(").append(tmp).append(", ").append(TACFirst).append(", ").append(TACSecond).append(")\n");

                        // 四元组
                        String QDPSecond = QDPArgs.pop();
                        String QDPFirst = QDPArgs.pop();
                        QDP.append("(").append(count).append(") ").append("(").append(tmp).append(", ").append(QDPFirst).append(", ").append(QDPSecond).append(", T").append(count).append(")\n");
                    }

                    TACArgs.push("(" + count + ")");
                    QDPArgs.push("T" + count);
                }
                tmp = "";
            } else {
                tmp += IP.charAt(i);
            }
            ++i;
        }

        String[] ans = new String[2];
        ans[0] = TAC.toString();
        ans[1] = QDP.toString();
        return ans;
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
                } else if (isOperator(tmp)) {
                    if (isSingleOperator(tmp) && args.size() < 1) return false;
                    else if (isBinaryOperator(tmp)) {
                        if (args.size() < 2) return false;
                        args.pop();
                    } else if (isTernaryOperator(tmp)) {
                        if (args.size() < 3) return false;
                        args.pop();
                        args.pop();
                    }
                }
                tmp = "";
            } else {
                tmp += IP.charAt(i);
            }
            ++i;
        }
        return args.size() == 1;
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

    private boolean isDigit(char ch) {
        return ch >= '0' && ch <= '9';
    }
    private boolean isLetter(char ch) {
        if (ch >= 'a' && ch <= 'z') return true;
        return ch >= 'A' && ch <= 'Z';
    }
    public boolean isOperator(String st) {
        return isSingleOperator(st) || isBinaryOperator(st)
                || isTernaryOperator(st);
    }

    public boolean isSingleOperator(String st) {
        return SingleOperator.contains(st);
    }
    public boolean isBinaryOperator(String st) {
        return BinaryOperator.contains(st);
    }
    public boolean isTernaryOperator(String st) {
        return TernaryOperator.contains(st);
    }

    private void initOperator() {
        // 初始化运算符
        SingleOperator.add("++");
        SingleOperator.add("_++");
        SingleOperator.add("++_");
        SingleOperator.add("--");
        SingleOperator.add("_--");
        SingleOperator.add("--_");
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
        OperatorPriority.put("_++", 15);
        OperatorPriority.put("++_", 15);
        OperatorPriority.put("_--", 15);
        OperatorPriority.put("--_", 15);
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
