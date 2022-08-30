import com.oocourse.uml1.interact.exceptions.user.ClassDuplicatedException;
import com.oocourse.uml1.interact.exceptions.user.ClassNotFoundException;
import com.oocourse.uml1.interact.exceptions.user.MethodDuplicatedException;
import com.oocourse.uml1.interact.exceptions.user.MethodWrongTypeException;
import com.oocourse.uml1.models.common.ReferenceType;
import com.oocourse.uml1.models.common.Visibility;
import com.oocourse.uml1.models.elements.UmlAttribute;
import com.oocourse.uml1.models.elements.UmlClass;
import com.oocourse.uml1.models.elements.UmlGeneralization;
import com.oocourse.uml1.models.elements.UmlInterfaceRealization;
import com.oocourse.uml1.models.elements.UmlOperation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class ClassProcess {
    private static final ClassProcess CLASS_PROCESS = new ClassProcess();
    private int classCount = 0;
    private final HashMap<String, String> name2id = new HashMap<>();
    private final HashMap<String, UmlClass> id2class = new HashMap<>();
    private final HashMap<String, HashSet<ReferenceType>> id2references = new HashMap<>();
    private final HashMap<String, String> id2parent = new HashMap<>();
    private final HashMap<String, Integer> id2subCount = new HashMap<>();
    private final HashMap<String, HashSet<String>> id2realizations = new HashMap<>();
    private final HashMap<String, HashSet<UmlOperation>> id2operations = new HashMap<>();

    public static ClassProcess getInstance() {
        return CLASS_PROCESS;
    }

    public boolean contains(String id) {
        return id2class.containsKey(id);
    }

    public void dealClass(HashSet<UmlClass> umlClasses) {
        for (UmlClass umlClass : umlClasses) {
            name2id.merge(umlClass.getName(), umlClass.getId(), (a, b) -> "null");
            id2class.put(umlClass.getId(), umlClass);
        }
        classCount = umlClasses.size();
    }

    public void dealAttribute(HashSet<UmlAttribute> umlAttributes) {
        for (UmlAttribute umlAttribute : umlAttributes) {
            if (umlAttribute.getType() instanceof ReferenceType) {
                if (id2references.containsKey(umlAttribute.getParentId())) {
                    id2references.get(umlAttribute.getParentId()).
                            add((ReferenceType) umlAttribute.getType());
                } else {
                    HashSet<ReferenceType> referenceSet = new HashSet<>();
                    referenceSet.add((ReferenceType) umlAttribute.getType());
                    id2references.put(umlAttribute.getParentId(), referenceSet);
                }
            }
        }
    }

    public void dealGeneralization(UmlGeneralization umlGeneralization) {
        id2parent.put(umlGeneralization.getSource(), umlGeneralization.getTarget());
        id2subCount.merge(umlGeneralization.getTarget(), 1, Integer::sum);
    }

    public void dealInterfaceRealization(
            HashSet<UmlInterfaceRealization> umlInterfaceRealizations) {
        for (UmlInterfaceRealization umlInterfaceRealization : umlInterfaceRealizations) {
            if (id2realizations.containsKey(umlInterfaceRealization.getSource())) {
                id2realizations.get(umlInterfaceRealization.getSource()).
                        add(umlInterfaceRealization.getTarget());
            } else {
                HashSet<String> realizationSet = new HashSet<>();
                realizationSet.add(umlInterfaceRealization.getTarget());
                id2realizations.put(umlInterfaceRealization.getSource(), realizationSet);
            }
        }
    }

    public void dealOperation(HashSet<UmlOperation> umlOperations) {
        for (UmlOperation umlOperation : umlOperations) {
            if (id2operations.containsKey(umlOperation.getParentId())) {
                id2operations.get(umlOperation.getParentId()).add(umlOperation);
            } else {
                HashSet<UmlOperation> operationSet = new HashSet<>();
                operationSet.add(umlOperation);
                id2operations.put(umlOperation.getParentId(), operationSet);
            }
        }
    }

    public int getClassCount() {
        return classCount;
    }

    public int getClassSubClassCount(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        if (!id2subCount.containsKey(getId(className))) {
            return 0;
        }
        return id2subCount.get(getId(className));
    }

    public int getClassOperationCount(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        if (!id2operations.containsKey(getId(className))) {
            return 0;
        }
        return id2operations.get(getId(className)).size();
    }

    public Map<Visibility, Integer> getClassOperationVisibility(String className, String methodName)
            throws ClassNotFoundException, ClassDuplicatedException {
        HashMap<Visibility, Integer> result = new HashMap<>();
        if (id2operations.containsKey(getId(className))) {
            HashSet<UmlOperation> umlOperations = id2operations.get(getId(className));
            for (UmlOperation umlOperation : umlOperations) {
                if (methodName.equals(umlOperation.getName())) {
                    result.merge(umlOperation.getVisibility(), 1, Integer::sum);
                }
            }
        }
        return result;
    }

    public List<Integer> getClassOperationCouplingDegree(String className, String methodName)
            throws ClassNotFoundException, ClassDuplicatedException,
            MethodWrongTypeException, MethodDuplicatedException {
        ArrayList<UmlOperation> operations = getOperations(className, methodName);
        if (OperationProcess.getInstance().checkWrong(operations)) {
            throw new MethodWrongTypeException(className, methodName);
        }
        if (OperationProcess.getInstance().checkSame(operations)) {
            throw new MethodDuplicatedException(className, methodName);
        }
        return OperationProcess.getInstance().
                getClassOperationCouplingDegree(getId(className), operations);
    }

    public int getClassAttributeCouplingDegree(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        String id = getId(className);
        HashSet<String> attributeSet = getAttributeCouplingSet(id);
        return attributeSet.contains(id) ? attributeSet.size() - 1 : attributeSet.size();
    }

    public HashSet<String> getAttributeCouplingSet(String id) {
        HashSet<String> result = new HashSet<>();
        if (id2references.containsKey(id)) {
            for (ReferenceType referenceType : id2references.get(id)) {
                result.add(referenceType.getReferenceId());
            }
        }
        if (id2parent.containsKey(id)) {
            result.addAll(getAttributeCouplingSet(id2parent.get(id)));
        }
        return result;
    }

    public List<String> getClassImplementInterfaceList(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        HashSet<String> interfaceIds = getClassImplementInterface(getId(className));
        ArrayList<String> result = new ArrayList<>();
        for (String interfaceId : interfaceIds) {
            result.add(InterfaceProcess.getInstance().getName(interfaceId));
        }
        return result;
    }

    public HashSet<String> getClassImplementInterface(String id) {
        HashSet<String> result = new HashSet<>();
        if (id2parent.containsKey(id)) {
            result.addAll(getClassImplementInterface(id2parent.get(id)));
        }
        if (id2realizations.containsKey(id)) {
            result.addAll(id2realizations.get(id));
            for (String interfaceId : id2realizations.get(id)) {
                result.addAll(InterfaceProcess.getInstance().getParents(interfaceId));
            }
        }
        return result;
    }

    public int getClassDepthOfInheritance(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        if (id2parent.containsKey(getId(className))) {
            return getDepth(id2parent.get(getId(className))) + 1;
        } else {
            return 0;
        }
    }

    public int getDepth(String id) {
        if (id2parent.containsKey(id)) {
            return getDepth(id2parent.get(id)) + 1;
        } else {
            return 0;
        }
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

    public ArrayList<UmlOperation> getOperations(String className, String methodName)
            throws ClassNotFoundException, ClassDuplicatedException {
        ArrayList<UmlOperation> result = new ArrayList<>();
        if (id2operations.containsKey(getId(className))) {
            for (UmlOperation umlOperation : id2operations.get(getId(className))) {
                if (methodName.equals(umlOperation.getName())) {
                    result.add(umlOperation);
                }
            }
        }
        return result;
    }
}
