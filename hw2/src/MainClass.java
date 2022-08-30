import expr.Diyfunction;
import expr.Expr;
import expr.Factor;
import expr.Totaldiyfunctions;
import com.oocourse.spec2.ExprInput;
import com.oocourse.spec2.ExprInputMode;

public class MainClass {
    public static void main(String[] args) {
        ExprInput scanner = new ExprInput(ExprInputMode.NormalMode);
        int n = scanner.getCount();
        Totaldiyfunctions totaldiyfunctions = new Totaldiyfunctions();
        for (int i = 0; i < n; i++) {
            String diyinput = scanner.readLine();
            Lexer functionlexer = new Lexer(diyinput);
            Parser functionparser = new Parser(functionlexer);
            Factor diyfunction;
            diyfunction = functionparser.diyfactor(functionlexer.peek());
            if (functionlexer.peek().equals(" ")) {
                functionlexer.next();
            }
            functionlexer.next();
            if (functionlexer.peek().equals(" ")) {
                functionlexer.next();
            }
            Expr content = functionparser.parseExpr();
            if (diyfunction instanceof Diyfunction) {
                ((Diyfunction) diyfunction).setContent(content);
                totaldiyfunctions.addFunction((Diyfunction) diyfunction);
            }
        }
        String input = scanner.readLine();
        Lexer lexer = new Lexer(input);
        lexer.setTotaldiyfunctions(totaldiyfunctions);
        Parser parser = new Parser(lexer);
        Expr expr = parser.parseExpr();
        expr = expr.addsimplify();
        System.out.println(expr);
    }
}