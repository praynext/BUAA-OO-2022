package expr;

public class Exprpow implements Factor {
    private final Factor factor;
    private final int exponent;

    public Exprpow(Factor factor, int exponent) {
        this.factor = factor;
        this.exponent = exponent;
    }

    public Factor getFactor() {
        return factor;
    }

    public int getExponent() {
        return exponent;
    }

    public Factor expand() {
        Expr expr = new Expr();
        Term term = new Term();
        expr.addTerm(term);
        for (int i = 0; i < exponent; i++) {
            term.addFactor(factor);
        }
        term.setSimplified(false);
        return expr;
    }

    @Override
    public Exprpow copy() {
        return new Exprpow(this.getFactor().copy(), this.getExponent());
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
