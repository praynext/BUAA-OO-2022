package expr;

import java.util.Objects;

public class Pow implements Factor {
    private String name;
    private final int exponent;

    public Pow(String name, int exponent) {
        this.name = name;
        this.exponent = exponent;
    }

    public String getName() {
        return name;
    }

    public int getExponent() {
        return exponent;
    }

    @Override
    public Pow copy() {
        return new Pow(this.getName(), this.getExponent());
    }

    @Override
    public Factor getRadix() {
        return new Pow(this.getName(), 1);
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
        Pow pow = (Pow) o;
        return exponent == pow.exponent && Objects.equals(name, pow.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, exponent);
    }
}
