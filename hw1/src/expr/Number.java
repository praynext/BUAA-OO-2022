package expr;

import java.math.BigInteger;

public class Number implements Factor {
    private final BigInteger num;

    public Number(BigInteger num) {
        this.num = num;
    }

    public BigInteger getNum() {
        return num;
    }

}
