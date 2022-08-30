import com.oocourse.uml3.interact.exceptions.user.StateDuplicatedException;
import com.oocourse.uml3.interact.exceptions.user.StateNotFoundException;
import com.oocourse.uml3.interact.exceptions.user.TransitionNotFoundException;
import com.oocourse.uml3.models.elements.UmlEvent;
import com.oocourse.uml3.models.elements.UmlFinalState;
import com.oocourse.uml3.models.elements.UmlStateMachine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class MyMachine {
    private final UmlStateMachine umlStateMachine;
    private MyPseudostate pseudostate = null;
    private final HashMap<String, UmlFinalState> finalStates = new HashMap<>();
    private final HashMap<String, MyState> states = new HashMap<>();
    private final HashMap<String, String> name2state = new HashMap<>();
    private final HashMap<String, MyTransition> transitions = new HashMap<>();
    private final HashMap<String, HashMap<String, HashSet<String>>> trigger2guard = new HashMap<>();
    private final HashMap<HashSet<String>, String> paths = new HashMap<>();
    private boolean isError = false;
    private boolean r009Error = false;

    public MyMachine(UmlStateMachine umlStateMachine) {
        this.umlStateMachine = umlStateMachine;
    }

    public void addPseudostate(MyPseudostate myPseudostate) {
        pseudostate = myPseudostate;
    }

    public void addFinalState(UmlFinalState umlFinalState) {
        finalStates.put(umlFinalState.getId(), umlFinalState);
    }

    public void addState(MyState myState) {
        states.put(myState.getId(), myState);
        name2state.merge(myState.getName(), myState.getId(), (a, b) -> "null");
    }

    public boolean addTransition(MyTransition myTransition) {
        boolean result = finalStates.containsKey(myTransition.getSource());
        transitions.put(myTransition.getId(), myTransition);
        if (pseudostate.getId().equals(myTransition.getSource())) {
            pseudostate.addNextState(myTransition.getTarget());
        } else {
            if (states.containsKey(myTransition.getSource())) {
                states.get(myTransition.getSource()).addNextState(myTransition.getTarget());
            }
        }
        return result;
    }

    public void addEvent(UmlEvent umlEvent) {
        String guard = transitions.get(umlEvent.getParentId()).getGuard();
        if (guard == null || guard.matches("[ \t]*")) {
            guard = "null";
        }
        if (!trigger2guard.containsKey(umlEvent.getName())) {
            HashSet<String> hashSet = new HashSet<>();
            hashSet.add(guard);
            HashMap<String, HashSet<String>> hashMap = new HashMap<>();
            hashMap.put(transitions.get(umlEvent.getParentId()).getSource(), hashSet);
            trigger2guard.put(umlEvent.getName(), hashMap);
        } else {
            HashMap<String, HashSet<String>> map = trigger2guard.get(umlEvent.getName());
            if (map.containsKey(transitions.get(umlEvent.getParentId()).getSource())) {
                HashSet<String> set = map.get(transitions.get(umlEvent.getParentId()).getSource());
                if (set.contains("null") || set.contains(guard) || guard.equals("null")) {
                    r009Error = true;
                }
                set.add(guard);
            } else {
                HashSet<String> hashSet = new HashSet<>();
                hashSet.add(guard);
                map.put(transitions.get(umlEvent.getParentId()).getSource(), hashSet);
            }
        }
        transitions.get(umlEvent.getParentId()).addEvent(umlEvent);
    }

    public void dealPath() {
        for (String string : pseudostate.getNextState()) {
            HashSet<String> path = new HashSet<>();
            path.add(pseudostate.getId());
            path.add(string);
            if (finalStates.containsKey(string)) {
                paths.put(path, string);
            } else {
                generatePath(path, states.get(string));
            }
        }
        if (finalStates.size() == 0 || paths.size() == 0) {
            isError = true;
        }
    }

    public void generatePath(HashSet<String> path, MyState myState) {
        for (String string : myState.getNextState()) {
            if (path.contains(string)) {
                continue;
            }
            HashSet<String> newPath = new HashSet<>(path);
            newPath.add(string);
            if (finalStates.containsKey(string)) {
                paths.put(newPath, string);
            } else {
                generatePath(newPath, states.get(string));
            }
        }
    }

    public int getStateCount() {
        return finalStates.size() + states.size() + 1;
    }

    public boolean getStateIsCriticalPoint(String s1)
            throws StateNotFoundException, StateDuplicatedException {
        String id = getId(s1);
        if (isError) {
            return false;
        } else {
            for (HashSet<String> strings : paths.keySet()) {
                if (!strings.contains(id)) {
                    return false;
                }
            }
            return true;
        }
    }

    public List<String> getTransitionTrigger(String s1, String s2)
            throws StateNotFoundException, StateDuplicatedException, TransitionNotFoundException {
        String id1 = getId(s1);
        String id2 = getId(s2);
        ArrayList<String> result = new ArrayList<>();
        for (MyTransition myTransition : transitions.values()) {
            if (id1.equals(myTransition.getSource()) && id2.equals(myTransition.getTarget())) {
                result.addAll(myTransition.getTrigger());
            }
        }
        if (result.size() == 0) {
            throw new TransitionNotFoundException(umlStateMachine.getName(), s1, s2);
        }
        return result;
    }

    public String getId(String s1)
            throws StateNotFoundException, StateDuplicatedException {
        if (!name2state.containsKey(s1)) {
            throw new StateNotFoundException(umlStateMachine.getName(), s1);
        } else if (name2state.get(s1).equals("null")) {
            throw new StateDuplicatedException(umlStateMachine.getName(), s1);
        } else {
            return name2state.get(s1);
        }
    }

    public boolean checkR009() {
        return r009Error;
    }
}
