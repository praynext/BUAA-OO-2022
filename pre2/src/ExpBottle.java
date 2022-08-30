import java.math.BigInteger;

class ExpBottle extends Bottle {
    private double expRatio;

    public double getExpRatio() {
        return expRatio;
    }

    public void setExpRatio(double expRatio) {
        this.expRatio = expRatio;
    }

    public void use(Adventurer adventurer) throws Exception {
        if (!isFilled()) {
            throw new Exception("Failed to use " + getName() + " because it is empty.");
        }
        adventurer.setHealth(adventurer.getHealth() + getCapacity() / 10);
        setFilled(false);
        setPrice(getPrice().divide(BigInteger.TEN));
        adventurer.setExp(adventurer.getExp() * this.expRatio);

        System.out.println(adventurer.getName() +
                " drank " + getName() +
                " and recovered " + getCapacity() / 10 +
                ".\n" + adventurer.getName() +
                "'s exp became " + adventurer.getExp() +
                ".");
    }
}
