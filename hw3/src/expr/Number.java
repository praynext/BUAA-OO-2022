package expr;

import java.math.BigInteger;
import java.util.Objects;

public class Number implements Factor {
    private final BigInteger num;

    public Number(BigInteger num) {
        this.num = num;
    }

    public BigInteger getNum() {
        return num;
    }

    @Override
    public Number copy() {
        return new Number(this.getNum());
    }

    @Override
    public Factor getRadix() {
        return this;
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
        Number number = (Number) o;
        return Objects.equals(num, number.num);
    }

    @Override
    public int hashCode() {
        return Objects.hash(num);
    }
}
