import java.math.BigInteger;

class Sword extends Equipment {
    private double sharpness;

    public double getSharpness() {
        return sharpness;
    }

    public void setSharpness(double sharpness) {
        this.sharpness = sharpness;
    }

    public void setSword(int id, String name, BigInteger price, double sharpness) {
        this.setId(id);
        this.setName(name);
        this.setPrice(price);
        this.sharpness = sharpness;
    }

    public String toString() {
        if (this instanceof RareSword) {
            RareSword sword = (RareSword) this;
            return "The rareSword's id is " + this.getId() +
                    ", name is " + this.getName() +
                    ", sharpness is " + this.sharpness +
                    ", extraExpBonus is " + sword.getExtraExpBonus() +
                    ".";
        } else if (this instanceof EpicSword) {
            EpicSword sword = (EpicSword) this;
            return "The epicSword's id is " + this.getId() +
                    ", name is " + this.getName() +
                    ", sharpness is " + this.sharpness +
                    ", evolveRatio is " + sword.getEvolveRatio() +
                    ".";
        } else {
            return "The sword's id is " + this.getId() +
                    ", name is " + this.getName() +
                    ", sharpness is " + this.sharpness +
                    ".";
        }
    }

    public void use(Adventurer adventurer) {
        adventurer.setHealth(adventurer.getHealth() - 10);
        adventurer.setExp(adventurer.getExp() + 10);
        adventurer.setMoney(adventurer.getMoney() + this.sharpness);

        System.out.println(adventurer.getName() +
                " used " + getName() +
                " and earned " + this.sharpness +
                ".");
    }
}
