import java.math.BigInteger;

class Bottle extends Equipment {
    private double capacity;
    private boolean filled = true;

    public double getCapacity() {
        return capacity;
    }

    public boolean isFilled() {
        return filled;
    }

    public void setFilled(boolean filled) {
        this.filled = filled;
    }

    public void setBottle(int id, String name, BigInteger price, double capacity) {
        this.setId(id);
        this.setName(name);
        this.setPrice(price);
        this.capacity = capacity;
    }

    public String toString() {
        if (this instanceof HealingPotion) {
            HealingPotion bottle = (HealingPotion) this;
            return "The healingPotion's id is " + this.getId() +
                    ", name is " + this.getName() +
                    ", capacity is " + this.capacity +
                    ", filled is " + this.filled +
                    ", efficiency is " + bottle.getEfficiency() +
                    ".";
        } else if (this instanceof ExpBottle) {
            ExpBottle bottle = (ExpBottle) this;
            return "The expBottle's id is " + this.getId() +
                    ", name is " + this.getName() +
                    ", capacity is " + this.capacity +
                    ", filled is " + this.filled +
                    ", expRatio is " + bottle.getExpRatio() +
                    ".";
        } else {
            return "The bottle's id is " + this.getId() +
                    ", name is " + this.getName() +
                    ", capacity is " + this.capacity +
                    ", filled is " + this.filled +
                    ".";
        }
    }

    public void use(Adventurer adventurer) throws Exception {
        if (!isFilled()) {
            throw new Exception("Failed to use " + getName() + " because it is empty.");
        }
        adventurer.setHealth(adventurer.getHealth() + capacity / 10);
        setFilled(false);
        setPrice(getPrice().divide(BigInteger.TEN));

        System.out.println(adventurer.getName() +
                " drank " + getName() +
                " and recovered " + capacity / 10 +
                ".");
    }
}
