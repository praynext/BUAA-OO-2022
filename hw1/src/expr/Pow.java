package expr;

public class Pow implements Factor {
    private final int exponent;

    public Pow(int exponent) {
        this.exponent = exponent;
    }

    public int getExponent() {
        return exponent;
    }
}
