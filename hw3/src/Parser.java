import expr.Diyfunction;
import expr.Expr;
import expr.Pow;
import expr.Trifunction;
import expr.Factor;
import expr.Term;
import expr.Sumfunction;
import expr.Exprpow;
import expr.Number;

import java.math.BigInteger;

public class Parser {
    private final Lexer lexer;

    public Parser(Lexer lexer) {
        this.lexer = lexer;
    }

    public Expr parseExpr() {
        boolean negative = false;
        if (lexer.peek().equals("+")) {
            lexer.next();
        } else if (lexer.peek().equals("-")) {
            negative = true;
            lexer.next();
        }
        Term term = parseTerm();
        if (negative) {
            term.addFactor(new Number(BigInteger.valueOf(-1)));
        }
        Expr expr = new Expr();
        expr.addTerm(term);
        while (true) {
            boolean sub = false;
            if (lexer.peek().equals("+")) {
                lexer.next();
            } else if (lexer.peek().equals("-")) {
                sub = true;
                lexer.next();
            } else {
                break;
            }
            term = parseTerm();
            if (sub) {
                term.addFactor(new Number(BigInteger.valueOf(-1)));
            }
            expr.addTerm(term);
        }
        return expr.addsimplify();
    }

    public Term parseTerm() {
        Term term = new Term();
        if (lexer.peek().equals("+")) {
            lexer.next();
        } else if (lexer.peek().equals("-")) {
            term.addFactor(new Number(BigInteger.valueOf(-1)));
            lexer.next();
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
        while (lexer.peek().equals("*")) {
            lexer.next();
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
        } else if (lexer.peek().equals("x") ||
                lexer.peek().equals("y") ||
                lexer.peek().equals("z") ||
                lexer.peek().equals("i")) {
            return powfactor(lexer.peek());
        } else if (lexer.peek().equals("sin") || lexer.peek().equals("cos")) {
            return trifactor(lexer.peek());
        } else if (lexer.peek().equals("f") ||
                lexer.peek().equals("g") ||
                lexer.peek().equals("h")) {
            Factor diyfactor = diyfactor(lexer.peek());
            return ((Diyfunction) diyfactor).simplify(lexer.getTotaldiyfunctions());
        } else if (lexer.peek().equals("sum")) {
            return sumfactor();
        } else {
            return numfactor();
        }
    }

    public Factor exprfactor() {
        lexer.next();
        Factor expr = parseExpr();
        //if (!lexer.peek().equals(")")){Wrong Format!}
        lexer.next();
        if (lexer.peek().equals("**")) {
            lexer.next();
            if (lexer.peek().equals("+")) {
                lexer.next();
            }
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
            return expr;
        }
    }

    public Factor powfactor(String name) {
        lexer.next();
        if (lexer.peek().equals("**")) {
            lexer.next();
            if (lexer.peek().equals("+")) {
                lexer.next();
            }
            int i = Integer.parseInt(lexer.peek());
            lexer.next();
            if (i == 0) {
                return new Number(BigInteger.valueOf(1));
            } else {
                return new Pow(name, i);
            }
        } else {
            return new Pow(name, 1);
        }
    }

    public Factor trifactor(String name) {
        lexer.next();
        lexer.next();//if (lexer.peek().equals("("))
        Factor content = parseFactor();
        if (content instanceof Exprpow) {
            content = ((Exprpow) content).expand();
        }
        if (content instanceof Expr) {
            content = ((Expr) content).addsimplify();
            if (((Expr) content).getTerms().size() == 0) {
                content = new Number(BigInteger.ZERO);
            } else if (((Expr) content).getTerms().size() == 1 &&
                    ((Expr) content).getTerms().get(0).getFactors().size() == 1) {
                content = ((Expr) content).getTerms().get(0).getFactors().get(0);
            }
        }
        lexer.next();//if (lexer.peek().equals(")"))
        if (lexer.peek().equals("**")) {
            lexer.next();
            if (lexer.peek().equals("+")) {
                lexer.next();
            }
            int i = Integer.parseInt(lexer.peek());
            lexer.next();
            if (i == 0) {
                return new Number(BigInteger.ONE);
            }
            if (content instanceof Number) {
                if (((Number) content).getNum().equals(BigInteger.ZERO)) {
                    if (name.equals("sin")) {
                        return new Number(BigInteger.ZERO);
                    } else {
                        return new Number(BigInteger.ONE);
                    }
                } else if (((Number) content).getNum().compareTo(BigInteger.ZERO) < 0) {
                    return removed(name, (Number) content, i);
                }
            } else if (content instanceof Expr) {
                return removed(name, (Expr) content, i);
            }
            return new Trifunction(name, i, content);
        } else {
            if (content instanceof Number) {
                if (((Number) content).getNum().equals(BigInteger.ZERO)) {
                    if (name.equals("sin")) {
                        return new Number(BigInteger.ZERO);
                    } else {
                        return new Number(BigInteger.ONE);
                    }
                } else if (((Number) content).getNum().compareTo(BigInteger.ZERO) < 0) {
                    return removed(name, (Number) content, 1);
                }
            } else if (content instanceof Expr) {
                return removed(name, (Expr) content, 1);
            }
            return new Trifunction(name, 1, content);
        }
    }

    public Factor removed(String name, Number content, int i) {
        Expr expr = new Expr();
        Term term = new Term();
        expr.addTerm(term);
        if (name.equals("sin")) {
            BigInteger sign = BigInteger.ONE.negate().pow(i);
            term.addFactor(new Number(sign));
            term.addFactor(
                    new Trifunction(name, i,
                            new Number(content.getNum().abs())));
            return expr;
        } else {
            return new Trifunction(name, i,
                    new Number(content.getNum().abs()));
        }
    }

    public Factor removed(String name, Expr content, int i) {
        if (content.toString().startsWith("-")) {
            Factor newcontent = content.copy();
            for (Term subterm : ((Expr) newcontent).getTerms()) {
                subterm.addFactor(new Number(BigInteger.ONE.negate()));
            }
            newcontent = ((Expr) newcontent).addsimplify();
            if (((Expr) newcontent).getTerms().size() == 1 &&
                    ((Expr) newcontent).getTerms().get(0).getFactors().size() == 1) {
                newcontent = ((Expr) newcontent).getTerms().get(0).getFactors().get(0);
            }
            if (name.equals("sin")) {
                Expr expr = new Expr();
                Term term = new Term();
                expr.addTerm(term);
                BigInteger sign = BigInteger.ONE.negate().pow(i);
                term.addFactor(new Number(sign));
                term.addFactor(new Trifunction(name, i, newcontent));
                return expr;
            } else {
                return new Trifunction(name, i, newcontent);
            }
        } else {
            return new Trifunction(name, i, content);
        }
    }

    public Factor diyfactor(String name) {
        lexer.next();
        if (lexer.peek().equals("(")) {
            lexer.next();
        }
        Factor argument = parseFactor();
        Diyfunction diyfunction = new Diyfunction(name);
        diyfunction.addArgument(argument);
        while (true) {
            if (lexer.peek().equals(",")) {
                lexer.next();
                argument = parseFactor();
                diyfunction.addArgument(argument);
            } else if (lexer.peek().equals(")")) {
                lexer.next();
                break;
            }
        }
        return diyfunction;
    }

    public Factor sumfactor() {
        lexer.next();
        lexer.next();//if (lexer.peek().equals("("))
        lexer.next();//if (lexer.peek().equals("i"))
        lexer.next();//if (lexer.peek().equals(","))
        BigInteger inf;
        if (lexer.peek().equals("+")) {
            lexer.next();
            inf = new BigInteger(lexer.peek());
        } else if (lexer.peek().equals("-")) {
            lexer.next();
            inf = new BigInteger(lexer.peek()).negate();
        } else {
            inf = new BigInteger(lexer.peek());
        }
        lexer.next();
        lexer.next();//if (lexer.peek().equals(","))
        BigInteger sup;
        if (lexer.peek().equals("+")) {
            lexer.next();
            sup = new BigInteger(lexer.peek());
        } else if (lexer.peek().equals("-")) {
            lexer.next();
            sup = new BigInteger(lexer.peek()).negate();
        } else {
            sup = new BigInteger(lexer.peek());
        }
        lexer.next();
        lexer.next();//if (lexer.peek().equals(","))
        Factor content = parseFactor();
        lexer.next();//if (lexer.peek().equals(")"))
        Sumfunction sumfunction = new Sumfunction(inf, sup, content);
        return sumfunction.simplify();
    }

    public Factor numfactor() {
        if (lexer.peek().equals("+")) {
            lexer.next();
            BigInteger num = new BigInteger(lexer.peek());
            lexer.next();
            return new Number(num);
        } else if (lexer.peek().equals("-")) {
            lexer.next();
            BigInteger num = new BigInteger(lexer.peek());
            lexer.next();
            return new Number(num.negate());
        } else {
            BigInteger num = new BigInteger(lexer.peek());
            lexer.next();
            return new Number(num);
        }
    }
}