import com.oocourse.uml1.models.elements.UmlGeneralization;
import com.oocourse.uml1.models.elements.UmlInterface;

import java.util.HashMap;
import java.util.HashSet;

public class InterfaceProcess {
    private static final InterfaceProcess INTERFACE_PROCESS = new InterfaceProcess();
    private final HashMap<String, UmlInterface> id2interface = new HashMap<>();
    private final HashMap<String, HashSet<String>> id2parents = new HashMap<>();

    public static InterfaceProcess getInstance() {
        return INTERFACE_PROCESS;
    }

    public void dealInterface(HashSet<UmlInterface> umlInterfaces) {
        for (UmlInterface umlInterface : umlInterfaces) {
            id2interface.put(umlInterface.getId(), umlInterface);
        }
    }

    public void dealGeneralization(UmlGeneralization umlGeneralization) {
        if (id2parents.containsKey(umlGeneralization.getSource())) {
            id2parents.get(umlGeneralization.getSource()).add(umlGeneralization.getTarget());
        } else {
            HashSet<String> parents = new HashSet<>();
            parents.add(umlGeneralization.getTarget());
            id2parents.put(umlGeneralization.getSource(), parents);
        }
    }

    public String getName(String id) {
        return id2interface.get(id).getName();
    }

    public HashSet<String> getParents(String id) {
        HashSet<String> result = new HashSet<>();
        if (id2parents.containsKey(id)) {
            for (String parentId : id2parents.get(id)) {
                result.add(parentId);
                result.addAll(getParents(parentId));
            }
        }
        return result;
    }
}
