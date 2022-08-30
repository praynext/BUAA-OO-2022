import com.oocourse.spec3.exceptions.EmojiIdNotFoundException;
import com.oocourse.spec3.exceptions.EqualEmojiIdException;
import com.oocourse.spec3.exceptions.EqualGroupIdException;
import com.oocourse.spec3.exceptions.EqualMessageIdException;
import com.oocourse.spec3.exceptions.EqualPersonIdException;
import com.oocourse.spec3.exceptions.EqualRelationException;
import com.oocourse.spec3.exceptions.GroupIdNotFoundException;
import com.oocourse.spec3.exceptions.MessageIdNotFoundException;
import com.oocourse.spec3.exceptions.PersonIdNotFoundException;
import com.oocourse.spec3.exceptions.RelationNotFoundException;
import com.oocourse.spec3.main.EmojiMessage;
import com.oocourse.spec3.main.Group;
import com.oocourse.spec3.main.Message;
import com.oocourse.spec3.main.Network;
import com.oocourse.spec3.main.NoticeMessage;
import com.oocourse.spec3.main.Person;
import com.oocourse.spec3.main.RedEnvelopeMessage;
import exceptions.MyEmojiIdNotFoundException;
import exceptions.MyEqualEmojiIdException;
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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

public class MyNetwork implements Network {
    private final HashMap<Integer, Person> people = new HashMap<>();
    private final HashMap<Integer, Group> groups = new HashMap<>();
    private final HashMap<Integer, Message> messages = new HashMap<>();
    private final HashMap<Integer, Integer> emojiMessages = new HashMap<>();
    private final UnionFind unionFind = new UnionFind();
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
            throws EqualMessageIdException, EmojiIdNotFoundException, EqualPersonIdException {
        if (messages.containsValue(message)) {
            throw new MyEqualMessageIdException(message.getId());
        } else if (message instanceof EmojiMessage &&
                !emojiMessages.containsKey(((EmojiMessage) message).getEmojiId())) {
            throw new MyEmojiIdNotFoundException(((EmojiMessage) message).getEmojiId());
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
                    MyPerson person1 = (MyPerson) message.getPerson1();
                    MyPerson person2 = (MyPerson) message.getPerson2();
                    person1.addSocialValue(message.getSocialValue());
                    person2.addSocialValue(message.getSocialValue());
                    person2.recieve(message);
                    if (message instanceof RedEnvelopeMessage) {
                        person1.addMoney(-((RedEnvelopeMessage) message).getMoney());
                        person2.addMoney(((RedEnvelopeMessage) message).getMoney());
                    } else if (message instanceof EmojiMessage) {
                        emojiMessages.merge(((EmojiMessage) message).getEmojiId(), 1, Integer::sum);
                    }
                }
            } else {
                if (!(message.getGroup().hasPerson(message.getPerson1()))) {
                    throw new MyPersonIdNotFoundException(message.getPerson1().getId());
                } else {
                    messages.remove(id);
                    int socialValue = message.getSocialValue();
                    if (message instanceof RedEnvelopeMessage) {
                        int money = ((RedEnvelopeMessage) message).getMoney() /
                                message.getGroup().getSize();
                        for (Person person : ((MyGroup) message.getGroup()).getPeople()) {
                            person.addSocialValue(socialValue);
                            person.addMoney(money);
                        }
                        message.getPerson1().addMoney(-money * message.getGroup().getSize());
                    } else if (message instanceof EmojiMessage) {
                        for (Person person : ((MyGroup) message.getGroup()).getPeople()) {
                            person.addSocialValue(socialValue);
                        }
                        emojiMessages.merge(((EmojiMessage) message).getEmojiId(), 1, Integer::sum);
                    } else {
                        for (Person person : ((MyGroup) message.getGroup()).getPeople()) {
                            person.addSocialValue(socialValue);
                        }
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

    @Override
    public boolean containsEmojiId(int id) {
        return emojiMessages.containsKey(id);
    }

    @Override
    public void storeEmojiId(int id) throws EqualEmojiIdException {
        if (emojiMessages.containsKey(id)) {
            throw new MyEqualEmojiIdException(id);
        } else {
            emojiMessages.put(id, 0);
        }
    }

    @Override
    public int queryMoney(int id) throws PersonIdNotFoundException {
        if (!people.containsKey(id)) {
            throw new MyPersonIdNotFoundException(id);
        } else {
            return people.get(id).getMoney();
        }
    }

    @Override
    public int queryPopularity(int id) throws EmojiIdNotFoundException {
        if (!emojiMessages.containsKey(id)) {
            throw new MyEmojiIdNotFoundException(id);
        } else {
            return emojiMessages.get(id);
        }
    }

    @Override
    public int deleteColdEmoji(int limit) {
        emojiMessages.entrySet().removeIf(entry -> entry.getValue() < limit);
        messages.entrySet().removeIf(entry ->
                entry.getValue() instanceof EmojiMessage &&
                        !emojiMessages.containsKey(((EmojiMessage) entry.getValue()).getEmojiId()));
        return emojiMessages.size();
    }

    @Override
    public void clearNotices(int personId) throws PersonIdNotFoundException {
        if (!people.containsKey(personId)) {
            throw new MyPersonIdNotFoundException(personId);
        } else {
            people.get(personId).getMessages().removeIf(message ->
                    message instanceof NoticeMessage);
        }
    }

    @Override
    public int sendIndirectMessage(int id) throws MessageIdNotFoundException {
        if (!messages.containsKey(id) || messages.get(id).getType() == 1) {
            throw new MyMessageIdNotFoundException(id);
        } else {
            Message message = messages.get(id);
            if (!unionFind.isSameSet(message.getPerson1().getId(), message.getPerson2().getId())) {
                return -1;
            } else {
                messages.remove(id);
                MyPerson person1 = (MyPerson) message.getPerson1();
                MyPerson person2 = (MyPerson) message.getPerson2();
                person1.addSocialValue(message.getSocialValue());
                person2.addSocialValue(message.getSocialValue());
                person2.recieve(message);
                if (message instanceof RedEnvelopeMessage) {
                    person1.addMoney(-((RedEnvelopeMessage) message).getMoney());
                    person2.addMoney(((RedEnvelopeMessage) message).getMoney());
                } else if (message instanceof EmojiMessage) {
                    emojiMessages.merge(((EmojiMessage) message).getEmojiId(), 1, Integer::sum);
                }
                HashSet<Integer> finished = new HashSet<>();
                PriorityQueue<Edge> edges = new PriorityQueue<>();
                HashMap<Integer, Integer> distance = new HashMap<>();
                distance.put(person1.getId(), 0);
                finished.add(person1.getId());
                for (Map.Entry<Person, Integer> entry : person1.getRelations().entrySet()) {
                    edges.add(new Edge(person1.getId(), entry.getKey().getId(), entry.getValue()));
                    distance.put(entry.getKey().getId(), entry.getValue());
                }
                while (!edges.isEmpty()) {
                    Edge newEdge = edges.poll();
                    int end = newEdge.getEnd();
                    if (finished.contains(end)) {
                        continue;
                    }
                    if (end == person2.getId()) {
                        return newEdge.getDistance();
                    }
                    finished.add(end);
                    for (Map.Entry<Person, Integer> entry :
                            ((MyPerson) people.get(end)).getRelations().entrySet()) {
                        int next = entry.getKey().getId();
                        if (!finished.contains(next) &&
                                (distance.getOrDefault(next, 2147483647) >
                                        distance.get(end) + entry.getValue())) {
                            distance.put(next, distance.get(end) + entry.getValue());
                            edges.add(new Edge(end, next, distance.get(next)));
                        }
                    }
                }
                return 0;
            }
        }
    }
}
