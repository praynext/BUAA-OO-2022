import com.oocourse.uml2.models.elements.UmlGeneralization;
import com.oocourse.uml2.models.elements.UmlInterface;

import java.util.HashMap;
import java.util.HashSet;

public class InterfaceProcess {
    private static final InterfaceProcess INTERFACE_PROCESS = new InterfaceProcess();
    private final HashMap<String, MyInterface> id2interface = new HashMap<>();

    public static InterfaceProcess getInstance() {
        return INTERFACE_PROCESS;
    }

    public void dealInterface(HashSet<UmlInterface> umlInterfaces) {
        for (UmlInterface umlInterface : umlInterfaces) {
            id2interface.put(umlInterface.getId(), new MyInterface(umlInterface));
        }
    }

    public void dealGeneralization(UmlGeneralization umlGeneralization) {
        id2interface.get(umlGeneralization.getSource()).addParent(umlGeneralization.getTarget());
    }

    public MyInterface getInterface(String interfaceId) {
        return id2interface.get(interfaceId);
    }
}
