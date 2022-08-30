import com.oocourse.uml2.models.elements.UmlInterface;

import java.util.HashSet;

public class MyInterface {
    private final UmlInterface umlInterface;
    private final HashSet<String> parents = new HashSet<>();

    public MyInterface(UmlInterface umlInterface) {
        this.umlInterface = umlInterface;
    }

    public String getName() {
        return umlInterface.getName();
    }

    public void addParent(String target) {
        parents.add(target);
    }

    public HashSet<String> getParents() {
        HashSet<String> result = new HashSet<>();
        for (String parentId : parents) {
            result.add(parentId);
            result.addAll(InterfaceProcess.getInstance().getInterface(parentId).getParents());
        }
        return result;
    }
}
