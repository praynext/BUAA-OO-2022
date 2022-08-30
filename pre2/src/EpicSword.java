class EpicSword extends Sword {
    private double evolveRatio;

    public double getEvolveRatio() {
        return evolveRatio;
    }

    public void setEvolveRatio(double evolveRatio) {
        this.evolveRatio = evolveRatio;
    }

    public void use(Adventurer adventurer) {
        adventurer.setHealth(adventurer.getHealth() - 10);
        adventurer.setExp(adventurer.getExp() + 10);
        adventurer.setMoney(adventurer.getMoney() + this.getSharpness());
        this.setSharpness(this.getSharpness() * this.evolveRatio);

        System.out.println(adventurer.getName() +
                " used " + getName() +
                " and earned " + this.getSharpness() / this.evolveRatio +
                ".\n" + getName() +
                "'s sharpness became " + this.getSharpness() +
                ".");
    }
}
