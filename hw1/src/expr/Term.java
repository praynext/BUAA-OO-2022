package expr;

import java.math.BigInteger;
import java.util.ArrayList;

public class Term {
    private final ArrayList<Factor> factors;
    private boolean simplified;

    public ArrayList<Factor> getFactors() {
        return factors;
    }

    public Term() {
        this.factors = new ArrayList<>();
        this.simplified = true;
    }

    public boolean isSimplified() {
        return simplified;
    }

    public void setSimplified(boolean simplified) {
        this.simplified = simplified;
    }

    public void addFactor(Factor factor) {
        this.factors.add(factor);
    }

    public void addAllFactor(ArrayList<Factor> allfactors) {
        this.factors.addAll(allfactors);
    }

    public Expr multsimplify() {
        Expr unitexpr = new Expr();
        Term unitterm = new Term();
        unitterm.addFactor(new Number(BigInteger.ONE));
        unitexpr.addTerm(unitterm);
        for (Factor item : factors) {
            if (item instanceof Expr) {
                unitexpr = unitexpr.mult((Expr) item);
                unitexpr = unitexpr.addsimplify();
            } else {
                for (Term subterm : unitexpr.getTerms()) {
                    subterm.addFactor(item);
                }
            }
        }
        return unitexpr;
    }
}
