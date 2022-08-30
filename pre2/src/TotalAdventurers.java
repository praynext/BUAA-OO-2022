import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Scanner;

public class TotalAdventurers {
    private ArrayList<Adventurer> adventurers = new ArrayList<>();

    public void add(int advid, String name) {
        Adventurer adventurer = new Adventurer();
        adventurer.setAdventurer(advid, name);
        this.adventurers.add(adventurer);
    }

    public void equip(int advid, int equiptype, Scanner scanner) {
        for (Adventurer item : this.adventurers) {
            if (item.getId() == advid) {
                item.equip(equiptype, scanner);
                break;
            }
        }
    }

    public void remove(int advid, int valueid) {
        for (Adventurer item : this.adventurers) {
            if (item.getId() == advid) {
                item.remove(valueid);
                break;
            }
        }
    }

    public BigInteger sum(int advid) {
        for (Adventurer item : adventurers) {
            if (item.getId() == advid) {
                return item.sum();
            }
        }
        return BigInteger.valueOf(0);
    }

    public BigInteger max(int advid) {
        for (Adventurer item : adventurers) {
            if (item.getId() == advid) {
                return item.max();
            }
        }
        return BigInteger.valueOf(0);
    }

    public int count(int advid) {
        for (Adventurer item : adventurers) {
            if (item.getId() == advid) {
                return item.count();
            }
        }
        return 0;
    }

    public String toString(int advid, int valueid) {
        for (Adventurer item : adventurers) {
            if (item.getId() == advid) {
                return item.toString(valueid);
            }
        }
        return null;
    }

    public void use(int advid) {
        for (Adventurer item : adventurers) {
            if (item.getId() == advid) {
                item.use(item);
                break;
            }
        }
    }

    public String status(int advid) {
        for (Adventurer item : adventurers) {
            if (item.getId() == advid) {
                return item.status();
            }
        }
        return null;
    }

    public void hire(int advid, int otherid) {
        for (Adventurer item : adventurers) {
            if (item.getId() == advid) {
                for (Adventurer other : adventurers) {
                    if (other.getId() == otherid) {
                        item.hire(other);
                        break;
                    }
                }
                break;
            }
        }
    }
}
