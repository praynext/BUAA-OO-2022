package expr;

import java.math.BigInteger;
import java.util.Objects;

public class Trifunction implements Function {
    private String name;
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

    public void setExponent(int exponent) {
        this.exponent = exponent;
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
        if (content instanceof Pow) {
            if (name.equals(((Pow) content).getName())) {
                if (argument instanceof Pow) {
                    int exponent = ((Pow) argument).getExponent() * ((Pow) content).getExponent();
                    content = new Pow(((Pow) argument).getName(), exponent);
                } else if (argument instanceof Number) {
                    content = new Number(((Number) argument).getNum().
                            pow(((Pow) content).getExponent()));
                }
            }
        }
    }

    public void substitute(BigInteger i) {
        if (content instanceof Pow) {
            if ("i".equals(((Pow) content).getName())) {
                content = new Number(i.pow(((Pow) content).getExponent()));
            }
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (content instanceof Number) {
            sb.append(name).append("(").append(((Number) content).getNum());
        } else if (content instanceof Pow) {
            sb.append(name).append("(x");
            if (((Pow) content).getExponent() != 1) {
                sb.append("**").append(((Pow) content).getExponent());
            }
        }
        sb.append(")");
        if (exponent != 1) {
            sb.append("**").append(exponent);
        }
        return sb.toString();
    }

    @Override
    public Trifunction copy() {
        return new Trifunction(this.getName(), this.getExponent(), this.getContent());
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
