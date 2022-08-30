import com.oocourse.uml3.interact.common.Pair;
import com.oocourse.uml3.interact.exceptions.user.InteractionDuplicatedException;
import com.oocourse.uml3.interact.exceptions.user.InteractionNotFoundException;
import com.oocourse.uml3.interact.exceptions.user.LifelineCreatedRepeatedlyException;
import com.oocourse.uml3.interact.exceptions.user.LifelineDuplicatedException;
import com.oocourse.uml3.interact.exceptions.user.LifelineNeverCreatedException;
import com.oocourse.uml3.interact.exceptions.user.LifelineNotFoundException;
import com.oocourse.uml3.models.elements.UmlAttribute;
import com.oocourse.uml3.models.elements.UmlEndpoint;
import com.oocourse.uml3.models.elements.UmlInteraction;
import com.oocourse.uml3.models.elements.UmlLifeline;
import com.oocourse.uml3.models.elements.UmlMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class InteractionProcess {
    private static final InteractionProcess INTERACTION_PROCESS = new InteractionProcess();
    private final HashMap<String, String> name2id = new HashMap<>();
    private final HashMap<String, MyInteraction> id2Interaction = new HashMap<>();
    private final HashMap<String, String> collaboration2id = new HashMap<>();

    public static InteractionProcess getInstance() {
        return INTERACTION_PROCESS;
    }

    public void dealInteraction(HashSet<UmlInteraction> umlInteractions) {
        for (UmlInteraction umlInteraction : umlInteractions) {
            name2id.merge(umlInteraction.getName(), umlInteraction.getId(), (a, b) -> "null");
            id2Interaction.put(umlInteraction.getId(), new MyInteraction(umlInteraction));
            collaboration2id.put(umlInteraction.getParentId(), umlInteraction.getId());
        }
    }

    public void dealAttribute(HashSet<UmlAttribute> umlAttributes) {
        for (UmlAttribute umlAttribute : umlAttributes) {
            if (!collaboration2id.containsKey(umlAttribute.getParentId())) {
                continue;
            }
            id2Interaction.get(collaboration2id.get(umlAttribute.getParentId())).
                    addAttribute(umlAttribute);
        }
    }

    public boolean dealLifeLine(HashSet<UmlLifeline> umlLifeLines) {
        boolean result = false;
        for (UmlLifeline umlLifeline : umlLifeLines) {
            if (id2Interaction.get(umlLifeline.getParentId()).
                    addLifeLine(new MyLifeline(umlLifeline))) {
                result = true;
            }
        }
        return result;
    }

    public void dealEndPoint(HashSet<UmlEndpoint> umlEndPoints) {
        for (UmlEndpoint umlEndpoint : umlEndPoints) {
            id2Interaction.get(umlEndpoint.getParentId()).addEndPoint(umlEndpoint);
        }
    }

    public boolean dealMessage(ArrayList<UmlMessage> umlMessages) {
        boolean result = false;
        for (UmlMessage umlMessage : umlMessages) {
            if (id2Interaction.get(umlMessage.getParentId()).addMessage(umlMessage)) {
                result = true;
            }
        }
        return result;
    }

    public int getParticipantCount(String s)
            throws InteractionNotFoundException, InteractionDuplicatedException {
        return id2Interaction.get(getId(s)).getParticipantCount();
    }

    public UmlLifeline getParticipantCreator(String s, String s1)
            throws InteractionNotFoundException, InteractionDuplicatedException,
            LifelineNotFoundException, LifelineDuplicatedException,
            LifelineNeverCreatedException, LifelineCreatedRepeatedlyException {
        return id2Interaction.get(getId(s)).getParticipantCreator(s1);
    }

    public Pair<Integer, Integer> getParticipantLostAndFound(String s, String s1)
            throws InteractionNotFoundException, InteractionDuplicatedException,
            LifelineNotFoundException, LifelineDuplicatedException {
        return id2Interaction.get(getId(s)).getParticipantLostAndFound(s1);
    }

    public String getId(String interactionName)
            throws InteractionNotFoundException, InteractionDuplicatedException {
        if (!name2id.containsKey(interactionName)) {
            throw new InteractionNotFoundException(interactionName);
        } else if (name2id.get(interactionName).equals("null")) {
            throw new InteractionDuplicatedException(interactionName);
        } else {
            return name2id.get(interactionName);
        }
    }
}
