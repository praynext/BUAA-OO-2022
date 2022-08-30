import com.oocourse.spec2.main.Message;
import com.oocourse.spec2.main.Person;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class MyPerson implements Person {
    private int id;
    private String name;
    private int age;
    private HashMap<Person, Integer> relations = new HashMap<>();
    private int money = 0;
    private int socialValue = 0;
    private LinkedList<Message> messages = new LinkedList<>();

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
            return ((Person) obj).getId() == id;
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

    @Override
    public void addSocialValue(int num) {
        socialValue += num;
    }

    @Override
    public int getSocialValue() {
        return socialValue;
    }

    @Override
    public List<Message> getMessages() {
        return messages;
    }

    @Override
    public List<Message> getReceivedMessages() {
        return (messages.size() > 3) ? messages.subList(0, 4) : messages;
    }

    @Override
    public void addMoney(int num) {
        money += num;
    }

    @Override
    public int getMoney() {
        return money;
    }

    public void add(Person person, int value) {
        relations.put(person, value);
    }

    public void recieve(Message message) {
        messages.addFirst(message);
    }

    public HashMap<Person, Integer> getRelations() {
        return relations;
    }
}
