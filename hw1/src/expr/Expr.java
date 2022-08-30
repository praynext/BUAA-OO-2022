package expr;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;

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
        HashMap<Integer, BigInteger> answer = new HashMap<>();
        for (Term item : terms) {
            BigInteger coefficient = BigInteger.ONE;
            int exponent = 0;
            if (item.isSimplified()) {
                for (Factor factor : item.getFactors()) {
                    if (factor instanceof Number) {
                        coefficient = coefficient.multiply(((Number) factor).getNum());
                    }
                    if (factor instanceof Pow) {
                        exponent += ((Pow) factor).getExponent();
                    }
                }
                if (answer.containsKey(exponent)) {
                    answer.put(exponent, answer.get(exponent).add(coefficient));
                } else {
                    answer.put(exponent, coefficient);
                }
            } else {
                for (Term subitem : item.multsimplify().terms) {
                    coefficient = BigInteger.ONE;
                    exponent = 0;
                    for (Factor subfactor : subitem.getFactors()) {
                        if (subfactor instanceof Number) {
                            coefficient = coefficient.multiply(((Number) subfactor).getNum());
                        }
                        if (subfactor instanceof Pow) {
                            exponent += ((Pow) subfactor).getExponent();
                        }
                    }
                    if (answer.containsKey(exponent)) {
                        answer.put(exponent, answer.get(exponent).add(coefficient));
                    } else {
                        answer.put(exponent, coefficient);
                    }
                }
            }
        }
        this.terms.clear();
        for (int i : answer.keySet()) {
            BigInteger j = answer.get(i);
            Term newterm = new Term();
            newterm.addFactor(new Pow(i));
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

    public HashMap<Integer, BigInteger> genmap() {
        HashMap<Integer, BigInteger> answer = new HashMap<>();
        for (Term item : terms) {
            BigInteger coefficient = BigInteger.ONE;
            int exponent = 0;
            for (Factor factor : item.getFactors()) {
                if (factor instanceof Number) {
                    coefficient = coefficient.multiply(((Number) factor).getNum());
                }
                if (factor instanceof Pow) {
                    exponent += ((Pow) factor).getExponent();
                }
            }
            if (answer.containsKey(exponent)) {
                answer.put(exponent, answer.get(exponent).add(coefficient));
            } else {
                answer.put(exponent, coefficient);
            }
        }
        return answer;
    }

    public void print(HashMap<Integer, BigInteger> answer) {
        StringBuilder sb = new StringBuilder();
        for (int i : answer.keySet()) {
            BigInteger j = answer.get(i);
            if (j.compareTo(BigInteger.valueOf(0)) > 0) {
                if (i == 0) {
                    sb.insert(0, "+" + j);
                } else if (i == 1) {
                    if (j.equals(BigInteger.valueOf(1))) {
                        sb.insert(0, "+x");
                    } else {
                        sb.insert(0, "+" + j + "*x");
                    }
                } else if (i == 2) {
                    if (j.equals(BigInteger.valueOf(1))) {
                        sb.insert(0, "+x*x");
                    } else {
                        sb.insert(0, "+" + j + "*x*x");
                    }
                } else {
                    if (j.equals(BigInteger.valueOf(1))) {
                        sb.insert(0, "+x**" + i);
                    } else {
                        sb.insert(0, "+" + j + "*x**" + i);
                    }
                }
            }
            if (j.compareTo(BigInteger.valueOf(0)) < 0) {
                if (i == 0) {
                    sb.append(j);
                } else if (i == 1) {
                    if (j.equals(BigInteger.valueOf(-1))) {
                        sb.append("-x");
                    } else {
                        sb.append(j).append("*x");
                    }
                } else if (i == 2) {
                    if (j.equals(BigInteger.valueOf(-1))) {
                        sb.append("-x*x");
                    } else {
                        sb.append(j).append("*x*x");
                    }
                } else {
                    if (j.equals(BigInteger.valueOf(-1))) {
                        sb.append("-x**").append(i);
                    } else {
                        sb.append(j).append("*x**").append(i);
                    }
                }
            }
        }
        if (sb.length() == 0) {
            sb.append(0);
        } else {
            if (sb.charAt(0) == '+') {
                sb.deleteCharAt(0);
            }
        }
        System.out.println(sb);
    }
}
