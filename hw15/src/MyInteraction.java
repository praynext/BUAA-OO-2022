import com.oocourse.uml3.interact.common.Pair;
import com.oocourse.uml3.interact.exceptions.user.LifelineCreatedRepeatedlyException;
import com.oocourse.uml3.interact.exceptions.user.LifelineDuplicatedException;
import com.oocourse.uml3.interact.exceptions.user.LifelineNeverCreatedException;
import com.oocourse.uml3.interact.exceptions.user.LifelineNotFoundException;
import com.oocourse.uml3.models.elements.UmlAttribute;
import com.oocourse.uml3.models.elements.UmlEndpoint;
import com.oocourse.uml3.models.elements.UmlInteraction;
import com.oocourse.uml3.models.elements.UmlLifeline;
import com.oocourse.uml3.models.elements.UmlMessage;

import java.util.HashMap;
import java.util.HashSet;

import static com.oocourse.uml3.models.common.MessageSort.CREATE_MESSAGE;
import static com.oocourse.uml3.models.common.MessageSort.DELETE_MESSAGE;

public class MyInteraction {
    private final UmlInteraction umlInteraction;
    private final HashMap<String, String> name2id = new HashMap<>();
    private final HashSet<String> attributes = new HashSet<>();
    private final HashMap<String, MyLifeline> lifelines = new HashMap<>();
    private final HashSet<String> endPoints = new HashSet<>();

    public MyInteraction(UmlInteraction umlInteraction) {
        this.umlInteraction = umlInteraction;
    }

    public void addAttribute(UmlAttribute umlAttribute) {
        attributes.add(umlAttribute.getId());
    }

    public boolean addLifeLine(MyLifeline myLifeline) {
        boolean result = !attributes.contains(myLifeline.getRepresent());
        name2id.merge(myLifeline.getName(), myLifeline.getId(), (a, b) -> "null");
        lifelines.put(myLifeline.getId(), myLifeline);
        return result;
    }

    public int getParticipantCount() {
        return lifelines.size();
    }

    public void addEndPoint(UmlEndpoint umlEndpoint) {
        endPoints.add(umlEndpoint.getId());
    }

    public boolean addMessage(UmlMessage umlMessage) {
        boolean result = false;
        if (lifelines.containsKey(umlMessage.getTarget())) {
            result = lifelines.get(umlMessage.getTarget()).isDeleted();
        }
        if (umlMessage.getMessageSort() == DELETE_MESSAGE) {
            if (lifelines.containsKey(umlMessage.getTarget())) {
                lifelines.get(umlMessage.getTarget()).setDeleted(true);
            }
        }
        if (umlMessage.getMessageSort() == CREATE_MESSAGE) {
            if (lifelines.containsKey(umlMessage.getTarget())) {
                lifelines.get(umlMessage.getTarget()).addCreator(umlMessage.getSource());
            }
        }
        if (endPoints.contains(umlMessage.getSource())) {
            if (lifelines.containsKey(umlMessage.getTarget())) {
                lifelines.get(umlMessage.getTarget()).addFound();
            }
        }
        if (endPoints.contains(umlMessage.getTarget())) {
            if (lifelines.containsKey(umlMessage.getSource())) {
                lifelines.get(umlMessage.getSource()).addLost();
            }
        }
        return result;
    }

    public UmlLifeline getParticipantCreator(String s1)
            throws LifelineNotFoundException, LifelineDuplicatedException,
            LifelineNeverCreatedException, LifelineCreatedRepeatedlyException {
        return lifelines.get(lifelines.get(getId(s1)).getCreator(umlInteraction.getName())).
                getLifeline();
    }

    public Pair<Integer, Integer> getParticipantLostAndFound(String s1)
            throws LifelineDuplicatedException, LifelineNotFoundException {
        return lifelines.get(getId(s1)).getParticipantLostAndFound();
    }

    public String getId(String name) throws LifelineNotFoundException, LifelineDuplicatedException {
        if (!name2id.containsKey(name)) {
            throw new LifelineNotFoundException(umlInteraction.getName(), name);
        } else if (name2id.get(name).equals("null")) {
            throw new LifelineDuplicatedException(umlInteraction.getName(), name);
        } else {
            return name2id.get(name);
        }
    }
}
