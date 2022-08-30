import java.math.BigInteger;

interface Value extends Comparable<Value> {
    BigInteger sum();

    void use(Adventurer adventurer) throws Exception;

    String toString();

    int getId();

    int compareTo(Value other);
}