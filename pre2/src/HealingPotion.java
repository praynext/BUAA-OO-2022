import java.math.BigInteger;

class HealingPotion extends Bottle {
    private double efficiency;

    public double getEfficiency() {
        return efficiency;
    }

    public void setEfficiency(double efficiency) {
        this.efficiency = efficiency;
    }

    public void use(Adventurer adventurer) throws Exception {
        if (!isFilled()) {
            throw new Exception("Failed to use " + getName() + " because it is empty.");
        }
        adventurer.setHealth(adventurer.getHealth() + getCapacity() / 10);
        setFilled(false);
        setPrice(getPrice().divide(BigInteger.TEN));
        adventurer.setHealth(adventurer.getHealth() + getCapacity() * this.efficiency);

        System.out.println(adventurer.getName() +
                " drank " + getName() +
                " and recovered " + getCapacity() / 10 +
                ".\n" + adventurer.getName() +
                " recovered extra " + getCapacity() * this.efficiency +
                ".");
    }
}
