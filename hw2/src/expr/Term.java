package expr;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class Term {
    private ArrayList<Factor> factors;
    private boolean simplified;
    private BigInteger coefficient;

    public ArrayList<Factor> getFactors() {
        return factors;
    }

    public Term() {
        this.factors = new ArrayList<>();
        this.simplified = true;
        this.coefficient = BigInteger.ONE;
    }

    public BigInteger getCoefficient() {
        return coefficient;
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

    public void merge() {
        HashMap<Factor, Integer> map = new HashMap<>();
        BigInteger coefficient = BigInteger.ONE;
        for (Factor item : factors) {
            if (item instanceof Number) {
                coefficient = coefficient.multiply(((Number) item).getNum());
            } else {
                Factor content = item.getRadix();
                int exponent = item.getExp();
                if (map.containsKey(content)) {
                    map.put(content, map.get(content) + exponent);
                } else {
                    map.put(content, exponent);
                }
            }
        }
        this.coefficient = this.coefficient.multiply(coefficient);
        ArrayList<Factor> merged = new ArrayList<>();
        for (Factor key : map.keySet()) {
            if (key instanceof Pow) {
                if (map.get(key) != 0) {
                    merged.add(new Pow("x", map.get(key)));
                }
            } else if (key instanceof Trifunction) {
                if (map.get(key) != 0) {
                    merged.add(new Trifunction(((Trifunction) key).getName(),
                            map.get(key),
                            ((Trifunction) key).getContent()));
                }
            }
        }
        this.factors = merged;
    }

    public void substitute(String name, Factor argument) {
        for (Factor item : factors) {
            if (item instanceof Pow) {
                if (name.equals(((Pow) item).getName())) {
                    if (argument instanceof Number) {
                        BigInteger temp = ((Number) argument).getNum().
                                pow(((Pow) item).getExponent());
                        Factor newitem = new Number(temp);
                        factors.set(factors.indexOf(item), newitem);
                    }
                    if (argument instanceof Pow) {
                        int exponent = ((Pow) argument).getExponent() * ((Pow) item).getExponent();
                        Factor newitem = new Pow(((Pow) argument).getName(), exponent);
                        factors.set(factors.indexOf(item), newitem);
                    }
                    if (argument instanceof Trifunction) {
                        int exponent = ((Trifunction) argument).getExponent()
                                * ((Pow) item).getExponent();
                        Factor newitem = new Trifunction(((Trifunction) argument).getName(),
                                exponent, ((Trifunction) argument).getContent());
                        factors.set(factors.indexOf(item), newitem);
                    }
                }
            } else if (item instanceof Trifunction) {
                ((Trifunction) item).substitute(name, argument);
                Factor contents = ((Trifunction) item).getContent();
                if (contents instanceof Number) {
                    if (((Number) contents).getNum().equals(BigInteger.ZERO)) {
                        if (((Trifunction) item).getName().equals("sin")) {
                            factors.set(factors.indexOf(item), new Number(BigInteger.ZERO));
                        } else if (((Trifunction) item).getName().equals("cos")) {
                            factors.set(factors.indexOf(item), new Number(BigInteger.ONE));
                        }
                    } else if (((Number) contents).getNum().compareTo(BigInteger.ZERO) < 0) {
                        if (((Trifunction) item).getName().equals("sin")) {
                            Expr exprs = new Expr();
                            Term terms = new Term();
                            exprs.addTerm(terms);
                            ((Trifunction) item).
                                    setContent(new Number(((Number) contents).getNum().abs()));
                            terms.addFactor(item);
                            terms.addFactor(new Number(BigInteger.ONE.negate().
                                    pow(((Trifunction) item).getExponent())));
                            factors.set(factors.indexOf(item), exprs);
                            this.setSimplified(false);
                        } else if (((Trifunction) item).getName().equals("cos")) {
                            ((Trifunction) item).
                                    setContent(new Number(((Number) contents).getNum().abs()));
                        }
                    }
                }
            } else if (item instanceof Expr) {
                ((Expr) item).substitute(name, argument);
            }
        }
    }

    public void substitute(BigInteger i) {
        for (Factor item : factors) {
            if (item instanceof Pow) {
                if ("i".equals(((Pow) item).getName())) {
                    Number newfactor = new Number(i.pow(((Pow) item).getExponent()));
                    factors.set(factors.indexOf(item), newfactor);
                }
            } else if (item instanceof Trifunction) {
                ((Trifunction) item).substitute(i);
                Factor contents = ((Trifunction) item).getContent();
                if (contents instanceof Number) {
                    if (((Number) contents).getNum().equals(BigInteger.ZERO)) {
                        if (((Trifunction) item).getName().equals("sin")) {
                            factors.set(factors.indexOf(item), new Number(BigInteger.ZERO));
                        } else if (((Trifunction) item).getName().equals("cos")) {
                            factors.set(factors.indexOf(item), new Number(BigInteger.ONE));
                        }
                    } else if (((Number) contents).getNum().compareTo(BigInteger.ZERO) < 0) {
                        if (((Trifunction) item).getName().equals("sin")) {
                            Expr exprs = new Expr();
                            Term terms = new Term();
                            exprs.addTerm(terms);
                            ((Trifunction) item).
                                    setContent(new Number(((Number) contents).getNum().abs()));
                            terms.addFactor(item);
                            terms.addFactor(new Number(BigInteger.ONE.negate().
                                    pow(((Trifunction) item).getExponent())));
                            factors.set(factors.indexOf(item), exprs);
                            this.setSimplified(false);
                        } else if (((Trifunction) item).getName().equals("cos")) {
                            ((Trifunction) item).
                                    setContent(new Number(((Number) contents).getNum().abs()));
                        }
                    }
                }
            } else if (item instanceof Expr) {
                ((Expr) item).substitute(i);
            }
        }
    }

    public Term copy() {
        Term newterm = new Term();
        for (Factor item : factors) {
            newterm.addFactor(item.copy());
        }
        newterm.setSimplified(this.isSimplified());
        return newterm;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Factor item : factors) {
            if (item instanceof Pow) {
                int exponent = ((Pow) item).getExponent();
                if (exponent == 1) {
                    sb.append("x*");
                } else if (exponent == 2) {
                    sb.append("x*x*");
                } else {
                    sb.append("x**").append(exponent).append("*");
                }
            } else if (item instanceof Trifunction) {
                sb.append(item).append("*");
            } else if (item instanceof Number) {
                if (!((Number) item).getNum().equals(BigInteger.ONE)) {
                    if (((Number) item).getNum().equals(BigInteger.ONE.negate())) {
                        sb.insert(0, "-");
                    } else {
                        sb.insert(0, ((Number) item).getNum() + "*");
                    }
                }
            }
        }
        if (sb.toString().equals("-")) {
            sb.append("1");
        }
        if (sb.toString().equals("")) {
            sb.append("1");
        }
        if (sb.charAt(sb.length() - 1) == '*') {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Term term = (Term) o;
        return simplified == term.simplified && Objects.equals(factors, term.factors);
    }

    @Override
    public int hashCode() {
        return Objects.hash(factors, simplified);
    }
}
