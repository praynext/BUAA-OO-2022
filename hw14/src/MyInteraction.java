import com.oocourse.uml2.interact.common.Pair;
import com.oocourse.uml2.interact.exceptions.user.LifelineCreatedRepeatedlyException;
import com.oocourse.uml2.interact.exceptions.user.LifelineDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.LifelineNeverCreatedException;
import com.oocourse.uml2.interact.exceptions.user.LifelineNotFoundException;
import com.oocourse.uml2.models.elements.UmlEndpoint;
import com.oocourse.uml2.models.elements.UmlInteraction;
import com.oocourse.uml2.models.elements.UmlLifeline;
import com.oocourse.uml2.models.elements.UmlMessage;

import java.util.HashMap;
import java.util.HashSet;

import static com.oocourse.uml2.models.common.MessageSort.CREATE_MESSAGE;

public class MyInteraction {
    private final UmlInteraction umlInteraction;
    private final HashMap<String, String> name2id = new HashMap<>();
    private final HashMap<String, MyLifeline> lifelines = new HashMap<>();
    private final HashSet<String> endPoints = new HashSet<>();

    public MyInteraction(UmlInteraction umlInteraction) {
        this.umlInteraction = umlInteraction;
    }

    public void addLifeLine(MyLifeline myLifeline) {
        name2id.merge(myLifeline.getName(), myLifeline.getId(), (a, b) -> "null");
        lifelines.put(myLifeline.getId(), myLifeline);
    }

    public int getParticipantCount() {
        return lifelines.size();
    }

    public void addEndPoint(UmlEndpoint umlEndpoint) {
        endPoints.add(umlEndpoint.getId());
    }

    public void addMessage(UmlMessage umlMessage) {
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
