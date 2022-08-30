import com.oocourse.uml3.models.common.Visibility;
import com.oocourse.uml3.models.elements.UmlAttribute;
import com.oocourse.uml3.models.elements.UmlClassOrInterface;
import com.oocourse.uml3.models.elements.UmlGeneralization;
import com.oocourse.uml3.models.elements.UmlInterface;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Objects;

public class InterfaceProcess {
    private static final InterfaceProcess INTERFACE_PROCESS = new InterfaceProcess();
    private final HashMap<String, MyInterface> id2interface = new HashMap<>();
    private final HashMap<MyInterface, Integer> dfn = new HashMap<>();
    private final HashMap<MyInterface, Integer> low = new HashMap<>();
    private final LinkedList<MyInterface> stack = new LinkedList<>();
    private final HashSet<MyInterface> vis = new HashSet<>();
    private HashSet<UmlClassOrInterface> result;
    private int count = 0;

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

    public boolean dealAttribute(HashSet<UmlAttribute> umlAttributes) {
        boolean result = false;
        for (UmlAttribute umlAttribute : umlAttributes) {
            if (id2interface.containsKey(umlAttribute.getParentId()) &&
                    umlAttribute.getVisibility() != Visibility.PUBLIC) {
                result = true;
            }
        }
        return result;
    }

    public MyInterface getInterface(String interfaceId) {
        return id2interface.get(interfaceId);
    }

    public void checkR003(HashSet<UmlClassOrInterface> result) {
        this.result = result;
        for (MyInterface myInterface : id2interface.values()) {
            if (!dfn.containsKey(myInterface)) {
                stack.clear();
                getCircle(myInterface);
            }
            if (myInterface.getOther().contains(myInterface.getId())) {
                result.add(myInterface.getUmlInterface());
            }
        }
    }

    private void getCircle(MyInterface myInterface) {
        dfn.put(myInterface, ++count);
        low.put(myInterface, count);
        stack.addLast(myInterface);
        vis.add(myInterface);
        for (String s : myInterface.getOther()) {
            MyInterface otherInterface = id2interface.get(s);
            if (!dfn.containsKey(otherInterface)) {
                getCircle(otherInterface);
                low.merge(myInterface, low.get(otherInterface), Integer::min);
            } else if (vis.contains(otherInterface)) {
                low.merge(myInterface, dfn.get(otherInterface), Integer::min);
            }
        }
        HashSet<UmlClassOrInterface> ans = new HashSet<>();
        if (Objects.equals(dfn.get(myInterface), low.get(myInterface))) {
            MyInterface tmp;
            do {
                tmp = stack.removeLast();
                vis.remove(tmp);
                ans.add(tmp.getUmlInterface());
            } while (!tmp.equals(myInterface));
        }
        if (ans.size() > 1) {
            result.addAll(ans);
        }
    }

    public boolean checkR001(HashSet<UmlAttribute> umlAttributes) {
        boolean result = false;
        for (UmlAttribute umlAttribute : umlAttributes) {
            if (id2interface.containsKey(umlAttribute.getParentId())) {
                if (umlAttribute.getName() == null || umlAttribute.getName().matches("[ \t]*")) {
                    result = true;
                }
            }
        }
        return result;
    }

    public void checkR004(HashSet<UmlClassOrInterface> result) {
        for (MyInterface myInterface : id2interface.values()) {
            if (myInterface.checkR004(new HashSet<>())) {
                result.add(myInterface.getUmlInterface());
            }
        }
    }
}
