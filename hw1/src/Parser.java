import expr.Expr;
import expr.Exprpow;
import expr.Factor;
import expr.Number;
import expr.Pow;
import expr.Term;

import java.math.BigInteger;

public class Parser {
    private final Lexer lexer;

    public Parser(Lexer lexer) {
        this.lexer = lexer;
    }

    public Expr parseExpr() {
        if (lexer.peek().equals(" ")) {
            lexer.next();
        }
        boolean negative = false;
        if (lexer.peek().equals("+")) {
            lexer.next();
            if (lexer.peek().equals(" ")) {
                lexer.next();
            }
        } else if (lexer.peek().equals("-")) {
            negative = true;
            lexer.next();
            if (lexer.peek().equals(" ")) {
                lexer.next();
            }
        }
        Term term = parseTerm();
        if (negative) {
            term.addFactor(new Number(BigInteger.valueOf(-1)));
        }
        Expr expr = new Expr();
        expr.addTerm(term);
        if (lexer.peek().equals(" ")) {
            lexer.peek();
        }
        while (true) {
            boolean sub = false;
            if (lexer.peek().equals("+")) {
                lexer.next();
                if (lexer.peek().equals(" ")) {
                    lexer.next();
                }
            } else if (lexer.peek().equals("-")) {
                sub = true;
                lexer.next();
                if (lexer.peek().equals(" ")) {
                    lexer.next();
                }
            } else {
                break;
            }
            term = parseTerm();
            if (sub) {
                term.addFactor(new Number(BigInteger.valueOf(-1)));
            }
            expr.addTerm(term);
            if (lexer.peek().equals(" ")) {
                lexer.peek();
            }
        }
        return expr;
    }

    public Term parseTerm() {
        Term term = new Term();
        if (lexer.peek().equals("+")) {
            lexer.next();
            if (lexer.peek().equals(" ")) {
                lexer.next();
            }
        } else if (lexer.peek().equals("-")) {
            term.addFactor(new Number(BigInteger.valueOf(-1)));
            lexer.next();
            if (lexer.peek().equals(" ")) {
                lexer.next();
            }
        }
        Factor factor = parseFactor();
        if (factor instanceof Exprpow) {
            for (int i = ((Exprpow) factor).getExponent(); i > 0; i--) {
                term.addFactor(((Exprpow) factor).getFactor());
            }
            term.setSimplified(false);
        } else {
            if (factor instanceof Expr) {
                term.setSimplified(false);
            }
            term.addFactor(factor);
        }
        while (true) {
            if (lexer.peek().equals(" ")) {
                lexer.next();
            }
            if (!lexer.peek().equals("*")) {
                break;
            }
            lexer.next();
            if (lexer.peek().equals(" ")) {
                lexer.next();
            }
            factor = parseFactor();
            if (factor instanceof Exprpow) {
                for (int i = ((Exprpow) factor).getExponent(); i > 0; i--) {
                    term.addFactor(((Exprpow) factor).getFactor());
                }
                term.setSimplified(false);
            } else {
                if (factor instanceof Expr) {
                    term.setSimplified(false);
                }
                term.addFactor(factor);
            }
        }
        return term;
    }

    public Factor parseFactor() {
        if (lexer.peek().equals("(")) {
            return exprfactor();
        } else if (lexer.peek().equals("x")) {
            lexer.next();
            return powfactor();
        } else {
            return numfactor();
        }
    }

    public Factor exprfactor() {
        lexer.next();
        Factor expr = parseExpr();
        //if (!lexer.peek().equals(")")){Wrong Format!}
        lexer.next();
        if (lexer.peek().equals(" ")) {
            lexer.next();
        }
        if (lexer.peek().equals("**")) {
            lexer.next();
            if (lexer.peek().equals(" ")) {
                lexer.next();
            }
            if (lexer.peek().equals("+")) {
                lexer.next();
            }
            if (Character.isDigit(lexer.peek().charAt(0))) {
                int i = Integer.parseInt(lexer.peek());
                if (i == 0) {
                    lexer.next();
                    return new Number(BigInteger.valueOf(1));
                } else if (i == 1) {
                    lexer.next();
                    return expr;
                } else {
                    lexer.next();
                    return new Exprpow(expr, i);
                }
            } else {
                lexer.next();
                return new Number(BigInteger.valueOf(1));
                //to be replaced by Wrong Format!
            }
        } else {
            return expr;
        }
    }

    public Factor powfactor() {
        if (lexer.peek().equals(" ")) {
            lexer.next();
        }
        if (lexer.peek().equals("**")) {
            lexer.next();
            if (lexer.peek().equals(" ")) {
                lexer.next();
            }
            if (lexer.peek().equals("+")) {
                lexer.next();
            }
            if (Character.isDigit(lexer.peek().charAt(0))) {
                int i = Integer.parseInt(lexer.peek());
                lexer.next();
                if (i == 0) {
                    return new Number(BigInteger.valueOf(1));
                } else {
                    return new Pow(i);
                }
            } else {
                lexer.next();
                return new Number(BigInteger.valueOf(1));
                //to be replaced by Wrong Format!
            }
        } else {
            return new Pow(1);
        }
    }

    public Factor numfactor() {
        if (lexer.peek().equals("+")) {
            lexer.next();
            //to add Wrong Format!
            BigInteger num = new BigInteger(lexer.peek());
            lexer.next();
            return new Number(num);
        } else if (lexer.peek().equals("-")) {
            lexer.next();
            //to add Wrong Format!
            BigInteger num = new BigInteger(lexer.peek());
            lexer.next();
            return new Number(num.negate());
        } else if (Character.isDigit(lexer.peek().charAt(0))) {
            BigInteger num = new BigInteger(lexer.peek());
            lexer.next();
            return new Number(num);
        } else {
            lexer.next();
            return new Number(BigInteger.valueOf(1));
            //to be replaced by Wrong Format!
        }
    }
}