import com.oocourse.spec2.exceptions.EqualGroupIdException;
import com.oocourse.spec2.exceptions.EqualMessageIdException;
import com.oocourse.spec2.exceptions.EqualPersonIdException;
import com.oocourse.spec2.exceptions.EqualRelationException;
import com.oocourse.spec2.exceptions.GroupIdNotFoundException;
import com.oocourse.spec2.exceptions.MessageIdNotFoundException;
import com.oocourse.spec2.exceptions.PersonIdNotFoundException;
import com.oocourse.spec2.exceptions.RelationNotFoundException;
import com.oocourse.spec2.main.Group;
import com.oocourse.spec2.main.Message;
import com.oocourse.spec2.main.Network;
import com.oocourse.spec2.main.Person;
import exceptions.MyEqualGroupIdException;
import exceptions.MyEqualMessageIdException;
import exceptions.MyEqualPersonIdException;
import exceptions.MyEqualRelationException;
import exceptions.MyGroupIdNotFoundException;
import exceptions.MyMessageIdNotFoundException;
import exceptions.MyPersonIdNotFoundException;
import exceptions.MyRelationNotFoundException;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;

public class MyNetwork implements Network {
    private HashMap<Integer, Person> people = new HashMap<>();
    private HashMap<Integer, Group> groups = new HashMap<>();
    private HashMap<Integer, Message> messages = new HashMap<>();
    private UnionFind unionFind = new UnionFind();
    private UnionFind mstUnionFind;

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
        if (people.containsKey(person.getId())) {
            throw new MyEqualPersonIdException(person.getId());
        } else {
            people.put(person.getId(), person);
            unionFind.add(person.getId());
        }
    }

    @Override
    public void addRelation(int id1, int id2, int value)
            throws PersonIdNotFoundException, EqualRelationException {
        if (!people.containsKey(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        } else if (!people.containsKey(id2)) {
            throw new MyPersonIdNotFoundException(id2);
        } else if (people.get(id1).isLinked(people.get(id2))) {
            throw new MyEqualRelationException(id1, id2);
        } else {
            MyPerson person1 = (MyPerson) people.get(id1);
            MyPerson person2 = (MyPerson) people.get(id2);
            person1.add(person2, value);
            person2.add(person1, value);
            Collection<Group> values = groups.values();
            for (Group group : values) {
                if (group.hasPerson(person1) && group.hasPerson(person2)) {
                    ((MyGroup) group).addRelation(value);
                }
            }
            unionFind.merge(id1, id2, value);
        }
    }

    @Override
    public int queryValue(int id1, int id2)
            throws PersonIdNotFoundException, RelationNotFoundException {
        if (!people.containsKey(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        } else if (!people.containsKey(id2)) {
            throw new MyPersonIdNotFoundException(id2);
        } else if (!people.get(id1).isLinked(people.get(id2))) {
            throw new MyRelationNotFoundException(id1, id2);
        } else {
            return people.get(id1).queryValue(people.get(id2));
        }
    }

    @Override
    public int queryPeopleSum() {
        return people.size();
    }

    @Override
    public boolean isCircle(int id1, int id2) throws PersonIdNotFoundException {
        if (!people.containsKey(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        } else if (!people.containsKey(id2)) {
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
    public int queryLeastConnection(int id) throws PersonIdNotFoundException {
        if (!people.containsKey(id)) {
            throw new MyPersonIdNotFoundException(id);
        } else {
            if (unionFind.queryLeastConnection(id) != -1) {
                return unionFind.queryLeastConnection(id);
            }
            mstUnionFind = new UnionFind();
            PriorityQueue<Edge> edges = new PriorityQueue<>();
            setEdges(edges, people.get(id), null);
            int totalDistance = 0;
            int totalNum = mstUnionFind.getSize() - 1;
            while (totalNum > 0 && !edges.isEmpty()) {
                Edge edge = edges.poll();
                if (!mstUnionFind.isSameSet(edge.getBegin(), edge.getEnd())) {
                    totalDistance += edge.getDistance();
                    mstUnionFind.merge(edge.getBegin(), edge.getEnd(), 0);
                    totalNum--;
                }
            }
            unionFind.setLeastConnection(id, totalDistance);
            return totalDistance;
        }
    }

    private void setEdges(PriorityQueue<Edge> edges, Person person, Person pre) {
        if (mstUnionFind.contains(person.getId())) {
            return;
        }
        mstUnionFind.add(person.getId());
        for (Person item : ((MyPerson) person).getRelations().keySet()) {
            if (pre == null || item.getId() != pre.getId()) {
                edges.add(new Edge(person.getId(), item.getId(), person.queryValue(item)));
                setEdges(edges, item, person);
            }
        }
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
    public int queryGroupPeopleSum(int id) throws GroupIdNotFoundException {
        if (!groups.containsKey(id)) {
            throw new MyGroupIdNotFoundException(id);
        } else {
            return groups.get(id).getSize();
        }
    }

    @Override
    public int queryGroupValueSum(int id) throws GroupIdNotFoundException {
        if (!groups.containsKey(id)) {
            throw new MyGroupIdNotFoundException(id);
        } else {
            return groups.get(id).getValueSum();
        }
    }

    @Override
    public int queryGroupAgeVar(int id) throws GroupIdNotFoundException {
        if (!groups.containsKey(id)) {
            throw new MyGroupIdNotFoundException(id);
        } else {
            return groups.get(id).getAgeVar();
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

    @Override
    public boolean containsMessage(int id) {
        return messages.containsKey(id);
    }

    @Override
    public void addMessage(Message message)
            throws EqualMessageIdException, EqualPersonIdException {
        if (messages.containsValue(message)) {
            throw new MyEqualMessageIdException(message.getId());
        } else if (message.getType() == 0 &&
                message.getPerson1().getId() == message.getPerson2().getId()) {
            throw new MyEqualPersonIdException(message.getPerson1().getId());
        } else {
            messages.put(message.getId(), message);
        }
    }

    @Override
    public Message getMessage(int id) {
        return messages.get(id);
    }

    @Override
    public void sendMessage(int id) throws RelationNotFoundException,
            MessageIdNotFoundException, PersonIdNotFoundException {
        if (!messages.containsKey(id)) {
            throw new MyMessageIdNotFoundException(id);
        } else {
            Message message = messages.get(id);
            if (message.getType() == 0) {
                if (!(message.getPerson1().isLinked(message.getPerson2()))) {
                    throw new MyRelationNotFoundException(
                            message.getPerson1().getId(), message.getPerson2().getId());
                } else {
                    messages.remove(id);
                    message.getPerson1().addSocialValue(message.getSocialValue());
                    message.getPerson2().addSocialValue(message.getSocialValue());
                    ((MyPerson) message.getPerson2()).recieve(message);
                }
            } else {
                if (!(message.getGroup().hasPerson(message.getPerson1()))) {
                    throw new MyPersonIdNotFoundException(message.getPerson1().getId());
                } else {
                    messages.remove(id);
                    int socialValue = message.getSocialValue();
                    for (Person person : ((MyGroup) message.getGroup()).getPeople()) {
                        person.addSocialValue(socialValue);
                    }
                }
            }
        }
    }

    @Override
    public int querySocialValue(int id) throws PersonIdNotFoundException {
        if (!people.containsKey(id)) {
            throw new MyPersonIdNotFoundException(id);
        } else {
            return people.get(id).getSocialValue();
        }
    }

    @Override
    public List<Message> queryReceivedMessages(int id) throws PersonIdNotFoundException {
        if (!people.containsKey(id)) {
            throw new MyPersonIdNotFoundException(id);
        } else {
            return people.get(id).getReceivedMessages();
        }
    }
}
