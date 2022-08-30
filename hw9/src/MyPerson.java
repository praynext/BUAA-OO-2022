import com.oocourse.spec1.main.Person;

import java.util.HashMap;

public class MyPerson implements Person {
    private int id;
    private String name;
    private int age;
    private HashMap<Person, Integer> relations = new HashMap<>();

    public MyPerson(int id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public int getAge() {
        return this.age;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof Person)) {
            return false;
        } else {
            return ((Person) obj).getId() == this.id;
        }
    }

    @Override
    public boolean isLinked(Person person) {
        if (person.getId() == id) {
            return true;
        }
        return relations.containsKey(person);
    }

    @Override
    public int queryValue(Person person) {
        return relations.getOrDefault(person, 0);
    }

    @Override
    public int compareTo(Person p2) {
        return this.name.compareTo(p2.getName());
    }

    public void add(Person person, int value) {
        relations.put(person, value);
    }
}
