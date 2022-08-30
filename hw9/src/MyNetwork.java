import com.oocourse.spec1.exceptions.EqualGroupIdException;
import com.oocourse.spec1.exceptions.EqualPersonIdException;
import com.oocourse.spec1.exceptions.EqualRelationException;
import com.oocourse.spec1.exceptions.GroupIdNotFoundException;
import com.oocourse.spec1.exceptions.PersonIdNotFoundException;
import com.oocourse.spec1.exceptions.RelationNotFoundException;
import com.oocourse.spec1.main.Group;
import com.oocourse.spec1.main.Network;
import com.oocourse.spec1.main.Person;
import exceptions.MyEqualGroupIdException;
import exceptions.MyEqualPersonIdException;
import exceptions.MyEqualRelationException;
import exceptions.MyGroupIdNotFoundException;
import exceptions.MyPersonIdNotFoundException;
import exceptions.MyRelationNotFoundException;

import java.util.Collection;
import java.util.HashMap;

public class MyNetwork implements Network {
    private HashMap<Integer, Person> people = new HashMap<>();
    private HashMap<Integer, Group> groups = new HashMap<>();
    private UnionFind unionFind = new UnionFind();

    public MyNetwork() {
    }

    @Override
    public boolean contains(int id) {
        return people.containsKey(id);
    }

    @Override
    public Person getPerson(int id) {
        return people.get(id);
    }

    @Override
    public void addPerson(Person person) throws EqualPersonIdException {
        if (contains(person.getId())) {
            throw new MyEqualPersonIdException(person.getId());
        } else {
            people.put(person.getId(), person);
            unionFind.add(person.getId());
        }
    }

    @Override
    public void addRelation(int id1, int id2, int value)
            throws PersonIdNotFoundException, EqualRelationException {
        if (!contains(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        } else if (!contains(id2)) {
            throw new MyPersonIdNotFoundException(id2);
        } else if (getPerson(id1).isLinked(getPerson(id2))) {
            throw new MyEqualRelationException(id1, id2);
        } else {
            MyPerson person1 = (MyPerson) getPerson(id1);
            MyPerson person2 = (MyPerson) getPerson(id2);
            (person1).add(person2, value);
            (person2).add(person1, value);
            Collection<Group> values = groups.values();
            for (Group group : values) {
                if (group.hasPerson(person1) && group.hasPerson(person2)) {
                    ((MyGroup) group).addRelation(value);
                }
            }
            unionFind.merge(id1, id2);
        }
    }

    @Override
    public int queryValue(int id1, int id2)
            throws PersonIdNotFoundException, RelationNotFoundException {
        if (!contains(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        } else if (!contains(id2)) {
            throw new MyPersonIdNotFoundException(id2);
        } else if (!getPerson(id1).isLinked(getPerson(id2))) {
            throw new MyRelationNotFoundException(id1, id2);
        } else {
            return getPerson(id1).queryValue(getPerson(id2));
        }
    }

    @Override
    public int queryPeopleSum() {
        return people.size();
    }

    @Override
    public boolean isCircle(int id1, int id2) throws PersonIdNotFoundException {
        if (!contains(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        } else if (!contains(id2)) {
            throw new MyPersonIdNotFoundException(id2);
        } else {
            return unionFind.isSameSet(id1, id2);
        }
    }

    @Override
    public int queryBlockSum() {
        return unionFind.queryBlockSum();
    }

    @Override
    public void addGroup(Group group) throws EqualGroupIdException {
        if (groups.containsKey(group.getId())) {
            throw new MyEqualGroupIdException(group.getId());
        } else {
            groups.put(group.getId(), group);
        }
    }

    @Override
    public Group getGroup(int id) {
        return groups.get(id);
    }

    @Override
    public void addToGroup(int id1, int id2)
            throws GroupIdNotFoundException, PersonIdNotFoundException, EqualPersonIdException {
        if (!groups.containsKey(id2)) {
            throw new MyGroupIdNotFoundException(id2);
        } else if (!people.containsKey(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        } else {
            if (groups.get(id2).hasPerson(people.get(id1))) {
                throw new MyEqualPersonIdException(id1);
            } else {
                if (groups.get(id2).getSize() < 1111) {
                    groups.get(id2).addPerson(people.get(id1));
                }
            }
        }
    }

    @Override
    public void delFromGroup(int id1, int id2)
            throws GroupIdNotFoundException, PersonIdNotFoundException, EqualPersonIdException {
        if (!groups.containsKey(id2)) {
            throw new MyGroupIdNotFoundException(id2);
        } else if (!people.containsKey(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        } else {
            if (!groups.get(id2).hasPerson(people.get(id1))) {
                throw new MyEqualPersonIdException(id1);
            } else {
                groups.get(id2).delPerson(people.get(id1));
            }
        }
    }
}
