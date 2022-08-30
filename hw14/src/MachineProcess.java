import com.oocourse.uml2.interact.exceptions.user.StateDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.StateMachineDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.StateMachineNotFoundException;
import com.oocourse.uml2.interact.exceptions.user.StateNotFoundException;
import com.oocourse.uml2.interact.exceptions.user.TransitionNotFoundException;
import com.oocourse.uml2.models.elements.UmlEvent;
import com.oocourse.uml2.models.elements.UmlFinalState;
import com.oocourse.uml2.models.elements.UmlPseudostate;
import com.oocourse.uml2.models.elements.UmlRegion;
import com.oocourse.uml2.models.elements.UmlState;
import com.oocourse.uml2.models.elements.UmlStateMachine;
import com.oocourse.uml2.models.elements.UmlTransition;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class MachineProcess {
    private static final MachineProcess MACHINE_PROCESS = new MachineProcess();
    private final HashMap<String, String> name2id = new HashMap<>();
    private final HashMap<String, MyMachine> id2machine = new HashMap<>();
    private final HashMap<String, MyMachine> region2machine = new HashMap<>();
    private final HashMap<String, MyMachine> transition2machine = new HashMap<>();

    public static MachineProcess getInstance() {
        return MACHINE_PROCESS;
    }

    public void dealMachine(HashSet<UmlStateMachine> umlStateMachines) {
        for (UmlStateMachine umlStateMachine : umlStateMachines) {
            name2id.merge(umlStateMachine.getName(), umlStateMachine.getId(), (a, b) -> "null");
            id2machine.put(umlStateMachine.getId(), new MyMachine(umlStateMachine));
        }
    }

    public void dealRegion(HashSet<UmlRegion> umlRegions) {
        for (UmlRegion umlRegion : umlRegions) {
            region2machine.put(umlRegion.getId(), id2machine.get(umlRegion.getParentId()));
        }
    }

    public void dealPseudostate(HashSet<UmlPseudostate> umlPseudostates) {
        for (UmlPseudostate umlPseudostate : umlPseudostates) {
            region2machine.get(umlPseudostate.getParentId()).
                    addPseudostate(new MyPseudostate(umlPseudostate));
        }
    }

    public void dealFinalState(HashSet<UmlFinalState> umlFinalStates) {
        for (UmlFinalState umlFinalState : umlFinalStates) {
            region2machine.get(umlFinalState.getParentId()).addFinalState(umlFinalState);
        }
    }

    public void dealState(HashSet<UmlState> umlStates) {
        for (UmlState umlState : umlStates) {
            region2machine.get(umlState.getParentId()).addState(new MyState(umlState));
        }
    }

    public void dealTransition(HashSet<UmlTransition> umlTransitions) {
        for (UmlTransition umlTransition : umlTransitions) {
            MyMachine myMachine = region2machine.get(umlTransition.getParentId());
            myMachine.addTransition(new MyTransition(umlTransition));
            transition2machine.put(umlTransition.getId(), myMachine);
        }
        for (MyMachine myMachine : id2machine.values()) {
            myMachine.dealPath();
        }
    }

    public void dealEvent(HashSet<UmlEvent> umlEvents) {
        for (UmlEvent umlEvent : umlEvents) {
            transition2machine.get(umlEvent.getParentId()).addEvent(umlEvent);
        }
    }

    public int getStateCount(String s)
            throws StateMachineNotFoundException, StateMachineDuplicatedException {
        return id2machine.get(getId(s)).getStateCount();
    }

    public boolean getStateIsCriticalPoint(String s, String s1)
            throws StateMachineNotFoundException, StateMachineDuplicatedException,
            StateNotFoundException, StateDuplicatedException {
        return id2machine.get(getId(s)).getStateIsCriticalPoint(s1);
    }

    public List<String> getTransitionTrigger(String s, String s1, String s2)
            throws StateMachineNotFoundException, StateMachineDuplicatedException,
            StateNotFoundException, StateDuplicatedException, TransitionNotFoundException {
        return id2machine.get(getId(s)).getTransitionTrigger(s1, s2);
    }

    public String getId(String s)
            throws StateMachineNotFoundException, StateMachineDuplicatedException {
        if (!name2id.containsKey(s)) {
            throw new StateMachineNotFoundException(s);
        } else if (name2id.get(s).equals("null")) {
            throw new StateMachineDuplicatedException(s);
        } else {
            return name2id.get(s);
        }
    }
}
