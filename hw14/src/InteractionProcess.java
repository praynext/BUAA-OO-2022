import com.oocourse.uml2.interact.common.Pair;
import com.oocourse.uml2.interact.exceptions.user.InteractionDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.InteractionNotFoundException;
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

public class InteractionProcess {
    private static final InteractionProcess INTERACTION_PROCESS = new InteractionProcess();
    private final HashMap<String, String> name2id = new HashMap<>();
    private final HashMap<String, MyInteraction> id2Interaction = new HashMap<>();

    public static InteractionProcess getInstance() {
        return INTERACTION_PROCESS;
    }

    public void dealInteraction(HashSet<UmlInteraction> umlInteractions) {
        for (UmlInteraction umlInteraction : umlInteractions) {
            name2id.merge(umlInteraction.getName(), umlInteraction.getId(), (a, b) -> "null");
            id2Interaction.put(umlInteraction.getId(), new MyInteraction(umlInteraction));
        }
    }

    public void dealLifeLine(HashSet<UmlLifeline> umlLifeLines) {
        for (UmlLifeline umlLifeline : umlLifeLines) {
            id2Interaction.get(umlLifeline.getParentId()).addLifeLine(new MyLifeline(umlLifeline));
        }
    }

    public void dealEndPoint(HashSet<UmlEndpoint> umlEndPoints) {
        for (UmlEndpoint umlEndpoint : umlEndPoints) {
            id2Interaction.get(umlEndpoint.getParentId()).addEndPoint(umlEndpoint);
        }
    }

    public void dealMessage(HashSet<UmlMessage> umlMessages) {
        for (UmlMessage umlMessage : umlMessages) {
            id2Interaction.get(umlMessage.getParentId()).addMessage(umlMessage);
        }
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
