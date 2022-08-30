import com.oocourse.uml3.models.elements.UmlState;

import java.util.HashSet;

public class MyState {
    private final UmlState umlState;
    private final HashSet<String> nextState = new HashSet<>();

    public MyState(UmlState umlState) {
        this.umlState = umlState;
    }

    public String getId() {
        return umlState.getId();
    }

    public String getName() {
        return umlState.getName();
    }

    public void addNextState(String target) {
        nextState.add(target);
    }

    public HashSet<String> getNextState() {
        return nextState;
    }
}
