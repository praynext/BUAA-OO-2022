import com.oocourse.uml3.models.elements.UmlEvent;
import com.oocourse.uml3.models.elements.UmlTransition;

import java.util.ArrayList;
import java.util.HashSet;

public class MyTransition {
    private final UmlTransition umlTransition;
    private final HashSet<UmlEvent> events = new HashSet<>();

    public MyTransition(UmlTransition umlTransition) {
        this.umlTransition = umlTransition;
    }

    public String getId() {
        return umlTransition.getId();
    }

    public String getSource() {
        return umlTransition.getSource();
    }

    public String getTarget() {
        return umlTransition.getTarget();
    }

    public String getGuard() {
        return umlTransition.getGuard();
    }

    public void addEvent(UmlEvent umlEvent) {
        events.add(umlEvent);
    }

    public ArrayList<String> getTrigger() {
        ArrayList<String> result = new ArrayList<>();
        for (UmlEvent umlEvent : events) {
            result.add(umlEvent.getName());
        }
        return result;
    }
}
