import com.oocourse.uml2.models.elements.UmlPseudostate;

import java.util.HashSet;

public class MyPseudostate {
    private final UmlPseudostate umlPseudostate;
    private final HashSet<String> nextState = new HashSet<>();

    public MyPseudostate(UmlPseudostate umlPseudostate) {
        this.umlPseudostate = umlPseudostate;
    }

    public String getId() {
        return umlPseudostate.getId();
    }

    public void addNextState(String target) {
        nextState.add(target);
    }

    public HashSet<String> getNextState() {
        return nextState;
    }
}
