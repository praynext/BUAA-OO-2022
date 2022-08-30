package expr;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;

public class Expr implements Factor {
    private final ArrayList<Term> terms;

    public Expr() {
        this.terms = new ArrayList<>();
    }

    public ArrayList<Term> getTerms() {
        return terms;
    }

    public void addTerm(Term term) {
        this.terms.add(term);
    }

    public Expr addsimplify() {
        HashMap<HashSet<Factor>, BigInteger> map = new HashMap<>();
        HashSet<Factor> set;
        for (Term item : terms) {
            if (item.isSimplified()) {
                item.merge();
                if (item.getCoefficient().equals(BigInteger.ZERO)) {
                    continue;
                }
                set = new HashSet<>(item.getFactors());
                if (map.containsKey(set)) {
                    map.put(set, map.get(set).add(item.getCoefficient()));
                } else {
                    map.put(set, item.getCoefficient());
                }
            } else {
                for (Term subitem : item.multsimplify().terms) {
                    subitem.merge();
                    set = new HashSet<>(subitem.getFactors());
                    if (map.containsKey(set)) {
                        map.put(set, map.get(set).add(subitem.getCoefficient()));
                    } else {
                        map.put(set, subitem.getCoefficient());
                    }
                }
            }
        }
        this.terms.clear();
        for (HashSet<Factor> i : map.keySet()) {
            BigInteger j = map.get(i);
            Term newterm = new Term();
            for (Factor item : i) {
                newterm.addFactor(item);
            }
            newterm.addFactor(new Number(j));
            terms.add(newterm);
        }
        return this;
    }

    public Expr mult(Expr other) {
        Expr multexpr = new Expr();
        Term multterm;
        for (Term thisterm : terms) {
            for (Term otherterm : other.terms) {
                multterm = new Term();
                multterm.addAllFactor(thisterm.getFactors());
                multterm.addAllFactor(otherterm.getFactors());
                multterm.setSimplified(thisterm.isSimplified() && otherterm.isSimplified());
                multexpr.addTerm(multterm);
            }
        }
        return multexpr;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Term item : terms) {
            if (item.toString().startsWith("-")) {
                sb.append(item);
            } else {
                sb.insert(0, "+" + item);
            }
        }
        if (sb.length() == 0) {
            sb.append(0);
        } else {
            if (sb.charAt(0) == '+') {
                sb.deleteCharAt(0);
            }
        }
        return sb.toString();
    }

    public void substitute(String name, Factor argument) {
        for (Term item : terms) {
            item.substitute(name, argument);
        }
    }

    public void substitute(BigInteger i) {
        for (Term item : terms) {
            item.substitute(i);
        }
    }

    public Expr copy() {
        Expr newexpr = new Expr();
        for (Term item : this.terms) {
            newexpr.addTerm(item.copy());
        }
        return newexpr;
    }

    @Override
    public Factor getRadix() {
        return null;
    }

    @Override
    public int getExp() {
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Expr expr = (Expr) o;
        return Objects.equals(terms, expr.terms);
    }

    @Override
    public int hashCode() {
        return Objects.hash(terms);
    }
}
