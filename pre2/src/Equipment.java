import java.math.BigInteger;

class Equipment implements Value {
    private int id;
    private String name;
    private BigInteger price;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigInteger getPrice() {
        return price;
    }

    public void setPrice(BigInteger price) {
        this.price = price;
    }

    public int compareTo(Value other) {
        if (this.sum().compareTo(other.sum()) != 0) {
            return this.sum().compareTo(other.sum());
        }
        return this.id - other.getId();
    }

    public void use(Adventurer adventurer) throws Exception {
    }

    public BigInteger sum() {
        return this.price;
    }
}
