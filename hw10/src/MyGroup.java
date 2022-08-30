import com.oocourse.spec2.main.Group;
import com.oocourse.spec2.main.Person;

import java.util.HashSet;

public class MyGroup implements Group {
    private int id;
    private HashSet<Person> people = new HashSet<>();
    private int ageSum = 0;
    private int ageSqrtSum = 0;
    private int valueSum = 0;

    public MyGroup(int id) {
        this.id = id;
    }

    @Override
    public int getId() {
        return this.id;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof Group)) {
            return false;
        } else {
            return ((Group) obj).getId() == id;
        }
    }

    @Override
    public void addPerson(Person person) {
        people.forEach(p -> valueSum += person.queryValue(p));
        people.add(person);
        ageSum += person.getAge();
        ageSqrtSum += person.getAge() * person.getAge();
    }

    @Override
    public boolean hasPerson(Person person) {
        return people.contains(person);
    }

    @Override
    public int getValueSum() {
        return valueSum << 1;
    }

    @Override
    public int getAgeMean() {
        return (people.size() == 0) ? 0 : ageSum / people.size();
    }

    @Override
    public int getAgeVar() {
        if (people.size() == 0) {
            return 0;
        } else {
            int ageMean = ageSum / people.size();
            return (ageSqrtSum - (ageMean * ageSum << 1) + people.size() * ageMean * ageMean)
                    / people.size();
        }
    }

    @Override
    public void delPerson(Person person) {
        people.remove(person);
        ageSum -= person.getAge();
        ageSqrtSum -= person.getAge() * person.getAge();
        people.forEach(p -> valueSum -= person.queryValue(p));
    }

    @Override
    public int getSize() {
        return people.size();
    }

    public void addRelation(int value) {
        valueSum += value;
    }

    public HashSet<Person> getPeople() {
        return people;
    }
}
