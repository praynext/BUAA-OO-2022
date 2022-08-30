package expr;

import java.math.BigInteger;

public class Sumfunction implements Function {
    private final BigInteger inf;
    private final BigInteger sup;
    private final Factor content;

    public Sumfunction(BigInteger inf, BigInteger sup, Factor content) {
        this.inf = inf;
        this.sup = sup;
        this.content = content;
    }

    public Expr simplify() {
        Expr ans = new Expr();
        for (BigInteger i = inf; i.compareTo(sup) <= 0; i = i.add(BigInteger.ONE)) {
            Factor newcontent = content.copy();
            Term term = new Term();
            if (newcontent instanceof Pow) {
                if ("i".equals(((Pow) newcontent).getName())) {
                    newcontent = new Number(i.pow(((Pow) newcontent).getExponent()));
                }
                term.addFactor(newcontent);
            } else if (newcontent instanceof Trifunction) {
                ((Trifunction) newcontent).substitute(i);
                Factor contents = ((Trifunction) newcontent).getContent();
                if (contents instanceof Number) {
                    if (((Number) contents).getNum().equals(BigInteger.ZERO)) {
                        if (((Trifunction) newcontent).getName().equals("sin")) {
                            newcontent = new Number(BigInteger.ZERO);
                        } else if (((Trifunction) newcontent).getName().equals("cos")) {
                            newcontent = new Number(BigInteger.ONE);
                        }
                    } else if (((Number) contents).getNum().compareTo(BigInteger.ZERO) < 0) {
                        if (((Trifunction) newcontent).getName().equals("sin")) {
                            ((Trifunction) newcontent).
                                    setContent(new Number(((Number) contents).getNum().abs()));
                            BigInteger sign = BigInteger.ONE.negate().
                                    pow(((Trifunction) newcontent).getExponent());
                            term.addFactor(new Number(sign));
                        } else if (((Trifunction) newcontent).getName().equals("cos")) {
                            ((Trifunction) newcontent).
                                    setContent(new Number(((Number) contents).getNum().abs()));
                        }
                    }
                }
                term.addFactor(newcontent);
            } else if (newcontent instanceof Expr) {
                ((Expr) newcontent).substitute(i);
                term.addFactor(newcontent);
                term.setSimplified(false);
            } else {
                term.addFactor(newcontent);
            }
            ans.addTerm(term);
        }
        return ans.addsimplify();
    }

    @Override
    public Sumfunction copy() {
        return new Sumfunction(this.inf, this.sup, this.content);
    }

    @Override
    public Factor getRadix() {
        return null;
    }

    @Override
    public int getExp() {
        return 0;
    }
}
