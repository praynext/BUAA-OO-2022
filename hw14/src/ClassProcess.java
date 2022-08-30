import com.oocourse.uml2.interact.exceptions.user.ClassDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.ClassNotFoundException;
import com.oocourse.uml2.interact.exceptions.user.MethodDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.MethodWrongTypeException;
import com.oocourse.uml2.models.common.ReferenceType;
import com.oocourse.uml2.models.common.Visibility;
import com.oocourse.uml2.models.elements.UmlAttribute;
import com.oocourse.uml2.models.elements.UmlClass;
import com.oocourse.uml2.models.elements.UmlGeneralization;
import com.oocourse.uml2.models.elements.UmlInterfaceRealization;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class ClassProcess {
    private static final ClassProcess CLASS_PROCESS = new ClassProcess();
    private int classCount = 0;
    private final HashMap<String, String> name2id = new HashMap<>();
    private final HashMap<String, MyClass> id2class = new HashMap<>();

    public static ClassProcess getInstance() {
        return CLASS_PROCESS;
    }

    public boolean contains(String id) {
        return id2class.containsKey(id);
    }

    public void dealClass(HashSet<UmlClass> umlClasses) {
        for (UmlClass umlClass : umlClasses) {
            name2id.merge(umlClass.getName(), umlClass.getId(), (a, b) -> "null");
            id2class.put(umlClass.getId(), new MyClass(umlClass));
        }
        classCount = umlClasses.size();
    }

    public void dealAttribute(HashSet<UmlAttribute> umlAttributes) {
        for (UmlAttribute umlAttribute : umlAttributes) {
            if (umlAttribute.getType() instanceof ReferenceType) {
                id2class.get(umlAttribute.getParentId()).
                        addReference((ReferenceType) umlAttribute.getType());
            }
        }
    }

    public void dealGeneralization(UmlGeneralization umlGeneralization) {
        id2class.get(umlGeneralization.getSource()).setParent(umlGeneralization.getTarget());
        id2class.get(umlGeneralization.getTarget()).addSubCount();
    }

    public void dealInterfaceRealization(
            HashSet<UmlInterfaceRealization> umlInterfaceRealizations) {
        for (UmlInterfaceRealization umlInterfaceRealization : umlInterfaceRealizations) {
            id2class.get(umlInterfaceRealization.getSource()).
                    addRealization(umlInterfaceRealization.getTarget());
        }
    }

    public void dealOperation(MyOperation myOperation) {
        id2class.get(myOperation.getParentId()).addOperation(myOperation);
    }

    public int getClassCount() {
        return classCount;
    }

    public int getClassSubClassCount(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        return id2class.get(getId(className)).getSubClassCount();
    }

    public int getClassOperationCount(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        return id2class.get(getId(className)).getOperationCount();
    }

    public Map<Visibility, Integer> getClassOperationVisibility(String className, String methodName)
            throws ClassNotFoundException, ClassDuplicatedException {
        return id2class.get(getId(className)).getOperationVisibility(methodName);
    }

    public List<Integer> getClassOperationCouplingDegree(String className, String methodName)
            throws ClassNotFoundException, ClassDuplicatedException,
            MethodWrongTypeException, MethodDuplicatedException {
        return id2class.get(getId(className)).getOperationCouplingDegree(methodName);
    }

    public int getClassAttributeCouplingDegree(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        String id = getId(className);
        HashSet<String> attributeSet = id2class.get(id).getAttributeCouplingSet();
        return attributeSet.contains(id) ? attributeSet.size() - 1 : attributeSet.size();
    }

    public List<String> getClassImplementInterfaceList(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        HashSet<String> interfaceIds = id2class.get(getId(className)).getClassImplementInterface();
        ArrayList<String> result = new ArrayList<>();
        for (String interfaceId : interfaceIds) {
            result.add(InterfaceProcess.getInstance().getInterface(interfaceId).getName());
        }
        return result;
    }

    public int getClassDepthOfInheritance(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        return getDepth(getId(className));
    }

    public int getDepth(String id) {
        if (id2class.get(id).getParent() != null) {
            return getDepth(id2class.get(id).getParent()) + 1;
        } else {
            return 0;
        }
    }

    public MyClass getClass(String id) {
        return id2class.get(id);
    }

    public String getId(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        if (!name2id.containsKey(className)) {
            throw new ClassNotFoundException(className);
        } else if (name2id.get(className).equals("null")) {
            throw new ClassDuplicatedException(className);
        } else {
            return name2id.get(className);
        }
    }
}
