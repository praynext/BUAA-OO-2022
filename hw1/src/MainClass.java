import expr.Expr;

import com.oocourse.spec1.ExprInput;
import com.oocourse.spec1.ExprInputMode;

import java.math.BigInteger;
import java.util.HashMap;

public class MainClass {
    public static void main(String[] args) {
        ExprInput scanner = new ExprInput(ExprInputMode.NormalMode);
        String input = scanner.readLine();
        Lexer lexer = new Lexer(input);
        Parser parser = new Parser(lexer);
        Expr expr = parser.parseExpr();
        expr = expr.addsimplify();
        HashMap<Integer, BigInteger> answer = expr.genmap();
        expr.print(answer);
    }
}
