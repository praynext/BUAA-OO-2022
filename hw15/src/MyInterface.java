import com.oocourse.uml3.models.elements.UmlClassOrInterface;
import com.oocourse.uml3.models.elements.UmlInterface;

import java.util.ArrayList;
import java.util.HashSet;

public class MyInterface {
    private final UmlInterface umlInterface;
    private final ArrayList<String> parents = new ArrayList<>();

    public MyInterface(UmlInterface umlInterface) {
        this.umlInterface = umlInterface;
    }

    public UmlInterface getUmlInterface() {
        return umlInterface;
    }

    public String getId() {
        return umlInterface.getId();
    }

    public String getName() {
        return umlInterface.getName();
    }

    public void addParent(String target) {
        parents.add(target);
    }

    public ArrayList<String> getOther() {
        return parents;
    }

    public HashSet<String> getParents() {
        HashSet<String> result = new HashSet<>();
        for (String parentId : parents) {
            result.add(parentId);
            result.addAll(InterfaceProcess.getInstance().getInterface(parentId).getParents());
        }
        return result;
    }

    public boolean checkR004(HashSet<UmlClassOrInterface> path) {
        if (path.contains(umlInterface)) {
            return true;
        }
        path.add(umlInterface);
        for (String s : parents) {
            if (InterfaceProcess.getInstance().getInterface(s).checkR004(path)) {
                return true;
            }
        }
        return false;
    }
}
