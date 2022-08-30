import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;

class Adventurer implements Value {
    private int id;
    private String name;
    private double health = 100;
    private double exp = 0;
    private double money = 0;
    private ArrayList<Value> values = new ArrayList<>();

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getHealth() {
        return health;
    }

    public double getExp() {
        return exp;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public void setHealth(double health) {
        this.health = health;
    }

    public void setExp(double exp) {
        this.exp = exp;
    }

    public void setAdventurer(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public void equip(int equiptype, Scanner scanner) {
        switch (equiptype) {
            case 1:
                values.add(equipBottle(scanner));
                break;
            case 2:
                values.add(equipHealingPotion(scanner));
                break;
            case 3:
                values.add(equipExpBottle(scanner));
                break;
            case 4:
                values.add(equipSword(scanner));
                break;
            case 5:
                values.add(equipRareSword(scanner));
                break;
            case 6:
                values.add(equipEpicSword(scanner));
                break;
            default:
                break;
        }
    }

    public Bottle equipBottle(Scanner scanner) {
        int id = scanner.nextInt();
        String name = scanner.next();
        BigInteger price = scanner.nextBigInteger();
        double capacity = scanner.nextDouble();
        Bottle bottle = new Bottle();
        bottle.setBottle(id, name, price, capacity);
        return bottle;
    }

    public HealingPotion equipHealingPotion(Scanner scanner) {
        int id = scanner.nextInt();
        String name = scanner.next();
        BigInteger price = scanner.nextBigInteger();
        double capacity = scanner.nextDouble();
        double efficiency = scanner.nextDouble();
        HealingPotion bottle = new HealingPotion();
        bottle.setBottle(id, name, price, capacity);
        bottle.setEfficiency(efficiency);
        return bottle;
    }

    public ExpBottle equipExpBottle(Scanner scanner) {
        int id = scanner.nextInt();
        String name = scanner.next();
        BigInteger price = scanner.nextBigInteger();
        double capacity = scanner.nextDouble();
        double expRatio = scanner.nextDouble();
        ExpBottle bottle = new ExpBottle();
        bottle.setBottle(id, name, price, capacity);
        bottle.setExpRatio(expRatio);
        return bottle;
    }

    public Sword equipSword(Scanner scanner) {
        int id = scanner.nextInt();
        String name = scanner.next();
        BigInteger price = scanner.nextBigInteger();
        double sharpness = scanner.nextDouble();
        Sword sword = new Sword();
        sword.setSword(id, name, price, sharpness);
        return sword;
    }

    public RareSword equipRareSword(Scanner scanner) {
        int id = scanner.nextInt();
        String name = scanner.next();
        BigInteger price = scanner.nextBigInteger();
        double sharpness = scanner.nextDouble();
        double extraExpBonus = scanner.nextDouble();
        RareSword sword = new RareSword();
        sword.setSword(id, name, price, sharpness);
        sword.setExtraExpBonus(extraExpBonus);
        return sword;
    }

    public EpicSword equipEpicSword(Scanner scanner) {
        int id = scanner.nextInt();
        String name = scanner.next();
        BigInteger price = scanner.nextBigInteger();
        double sharpness = scanner.nextDouble();
        double evolveRatio = scanner.nextDouble();
        EpicSword sword = new EpicSword();
        sword.setSword(id, name, price, sharpness);
        sword.setEvolveRatio(evolveRatio);
        return sword;
    }

    public void remove(int valueid) {
        for (Value item : this.values) {
            if (item.getId() == valueid) {
                values.remove(item);
                break;
            }
        }
    }

    public BigInteger sum() {
        BigInteger sum = new BigInteger("0");
        for (Value item : this.values) {
            sum = sum.add(item.sum());
        }
        return sum;
    }

    public BigInteger max() {
        BigInteger max = new BigInteger("0");
        for (Value item : this.values) {
            if (item.sum().compareTo(max) > 0) {
                max = item.sum();
            }
        }
        return max;
    }

    public int count() {
        return values.size();
    }

    public String toString(int valueid) {
        for (Value item : this.values) {
            if (item.getId() == valueid) {
                return item.toString();
            }
        }
        return null;
    }

    public String toString() {
        return "The adventurer's id is " + this.id +
                ", name is " + this.name +
                ", health is " + this.health +
                ", exp is " + this.exp +
                ", money is " + this.money +
                ".";
    }

    public int compareTo(Value other) {
        if (this.sum().compareTo(other.sum()) != 0) {
            return this.sum().compareTo(other.sum());
        }
        return this.id - other.getId();
    }

    public void use(Adventurer adventurer) {
        values.sort(Comparator.reverseOrder());
        for (Value item : values) {
            try {
                item.use(adventurer);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public String status() {
        return "The adventurer's id is " + this.getId() +
                ", name is " + this.getName() +
                ", health is " + this.getHealth() +
                ", exp is " + this.getExp() +
                ", money is " + this.getMoney() +
                ".";
    }

    public void hire(Adventurer other) {
        this.values.add(other);
    }
}