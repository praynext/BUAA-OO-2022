package expr;

import java.math.BigInteger;
import java.util.Objects;

public class Trifunction implements Function {
    private final String name;
    private int exponent;
    private Factor content;

    public Trifunction(String name, int exponent, Factor content) {
        this.name = name;
        this.exponent = exponent;
        this.content = content;
    }

    public int getExponent() {
        return exponent;
    }

    public void setContent(Factor content) {
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public Factor getContent() {
        return content;
    }

    public void substitute(String name, Factor argument) {
        if (content instanceof Expr) {
            ((Expr) content).substitute(name, argument);
            content = ((Expr) content).addsimplify();
        } else {
            Expr newexpr = new Expr();
            Term newterm = new Term();
            newterm.addFactor(content);
            newexpr.addTerm(newterm);
            newexpr.substitute(name, argument);
            content = newexpr.addsimplify();
        }
        if (((Expr) content).getTerms().size() == 0) {
            content = new Number(BigInteger.ZERO);
        } else if (((Expr) content).getTerms().size() == 1 &&
                ((Expr) content).getTerms().get(0).getFactors().size() == 1) {
            content = ((Expr) content).getTerms().get(0).getFactors().get(0);
        }
    }

    public void substitute(BigInteger i) {
        if (content instanceof Expr) {
            ((Expr) content).substitute(i);
            content = ((Expr) content).addsimplify();
        } else {
            Expr newexpr = new Expr();
            Term newterm = new Term();
            newterm.addFactor(content);
            newexpr.addTerm(newterm);
            newexpr.substitute(i);
            content = newexpr.addsimplify();
        }
        if (((Expr) content).getTerms().size() == 0) {
            content = new Number(BigInteger.ZERO);
        } else if (((Expr) content).getTerms().size() == 1 &&
                ((Expr) content).getTerms().get(0).getFactors().size() == 1) {
            content = ((Expr) content).getTerms().get(0).getFactors().get(0);
        }
    }

    public Factor sintocos() {
        if (content instanceof Expr) {
            Expr sintocos = ((Expr) content.copy()).sintocos();
            Expr costosin = ((Expr) content.copy()).costosin();
            if (sintocos.toString().length() < content.toString().length() &&
                    sintocos.toString().length() < costosin.toString().length()) {
                content = sintocos;
            } else if (costosin.toString().length() < content.toString().length()) {
                content = costosin;
            }
        }
        exponent = exponent - 2;
        Expr expr = new Expr();
        Term term = new Term();
        expr.addTerm(term);
        if (exponent != 0) {
            term.addFactor(this);
        }
        Term numterm = new Term();
        Term triterm = new Term();
        numterm.addFactor(new Number(BigInteger.ONE));
        triterm.addFactor(new Number(BigInteger.ONE.negate()));
        triterm.addFactor(new Trifunction("cos", 2, content.copy()));
        Expr subexpr = new Expr();
        subexpr.addTerm(numterm);
        subexpr.addTerm(triterm);
        term.addFactor(subexpr);
        term.setSimplified(false);
        return expr.addsimplify();
    }

    public Factor costosin() {
        if (content instanceof Expr) {
            Expr sintocos = ((Expr) content.copy()).sintocos();
            Expr costosin = ((Expr) content.copy()).costosin();
            if (sintocos.toString().length() < content.toString().length() &&
                    sintocos.toString().length() < costosin.toString().length()) {
                content = sintocos;
            } else if (costosin.toString().length() < content.toString().length()) {
                content = costosin;
            }
        }
        exponent = exponent - 2;
        Expr expr = new Expr();
        Term term = new Term();
        expr.addTerm(term);
        if (exponent != 0) {
            term.addFactor(this);
        }
        Term numterm = new Term();
        Term triterm = new Term();
        numterm.addFactor(new Number(BigInteger.ONE));
        triterm.addFactor(new Number(BigInteger.ONE.negate()));
        triterm.addFactor(new Trifunction("sin", 2, content.copy()));
        Expr subexpr = new Expr();
        subexpr.addTerm(numterm);
        subexpr.addTerm(triterm);
        term.addFactor(subexpr);
        term.setSimplified(false);
        return expr.addsimplify();
    }

    public void insidechange() {
        Factor sintocos;
        Factor costosin;
        Expr expr;
        if (this.getContent() instanceof Expr) {
            expr = (Expr) this.getContent();
        } else {
            Factor insidefactor = this.getContent();
            expr = new Expr();
            Term insideterm = new Term();
            insideterm.addFactor(insidefactor);
            expr.addTerm(insideterm);
        }
        sintocos = expr.copy().sintocos();
        costosin = expr.copy().costosin();
        if (sintocos.toString().length() < expr.toString().length() &&
                sintocos.toString().length() < costosin.toString().length()) {
            if (((Expr) sintocos).getTerms().size() == 1 &&
                    ((Expr) sintocos).getTerms().get(0).getFactors().size() == 1) {
                sintocos = ((Expr) sintocos).getTerms().get(0).getFactors().get(0);
            }
            this.setContent(sintocos);
        } else if (costosin.toString().length() < expr.toString().length()) {
            if (((Expr) costosin).getTerms().size() == 1 &&
                    ((Expr) costosin).getTerms().get(0).getFactors().size() == 1) {
                costosin = ((Expr) costosin).getTerms().get(0).getFactors().get(0);
            }
            this.setContent(costosin);
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(name).append("(");
        if (content instanceof Number) {
            sb.append(((Number) content).getNum());
        } else if (content instanceof Pow) {
            sb.append("x");
            if (((Pow) content).getExponent() != 1) {
                sb.append("**").append(((Pow) content).getExponent());
            }
        } else if ((content instanceof Trifunction)) {
            sb.append(content);
        } else if (content instanceof Expr) {
            sb.append("(").append(content).append(")");
        }
        sb.append(")");
        if (exponent != 1) {
            sb.append("**").append(exponent);
        }
        return sb.toString();
    }

    @Override
    public Trifunction copy() {
        return new Trifunction(this.getName(), this.getExponent(), this.getContent().copy());
    }

    @Override
    public Factor getRadix() {
        return new Trifunction(this.name, 1, this.content);
    }

    @Override
    public int getExp() {
        return exponent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Trifunction that = (Trifunction) o;
        return exponent == that.exponent &&
                Objects.equals(name, that.name) &&
                Objects.equals(content, that.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, exponent, content);
    }
}
