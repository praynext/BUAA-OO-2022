class RareSword extends Sword {
    private double extraExpBonus;

    public double getExtraExpBonus() {
        return extraExpBonus;
    }

    public void setExtraExpBonus(double extraExpBonus) {
        this.extraExpBonus = extraExpBonus;
    }

    public void use(Adventurer adventurer) {
        adventurer.setHealth(adventurer.getHealth() - 10);
        adventurer.setExp(adventurer.getExp() + 10);
        adventurer.setMoney(adventurer.getMoney() + this.getSharpness());
        adventurer.setExp(adventurer.getExp() + this.extraExpBonus);

        System.out.println(adventurer.getName() +
                " used " + getName() +
                " and earned " + this.getSharpness() +
                ".\n" + adventurer.getName() +
                " got extra exp " + this.extraExpBonus +
                ".");
    }
}
