import com.oocourse.uml3.interact.common.AttributeClassInformation;
import com.oocourse.uml3.interact.exceptions.user.MethodDuplicatedException;
import com.oocourse.uml3.interact.exceptions.user.MethodWrongTypeException;
import com.oocourse.uml3.models.common.ReferenceType;
import com.oocourse.uml3.models.common.Visibility;
import com.oocourse.uml3.models.elements.UmlClass;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class MyClass {
    private final UmlClass umlClass;
    private String parent = null;
    private int subCount = 0;
    private final HashSet<ReferenceType> references = new HashSet<>();
    private final HashSet<String> realizations = new HashSet<>();
    private final HashSet<MyOperation> operations = new HashSet<>();
    private final HashMap<String, Integer> attributes = new HashMap<>();

    public MyClass(UmlClass umlClass) {
        this.umlClass = umlClass;
    }

    public UmlClass getUmlClass() {
        return umlClass;
    }

    public String getId() {
        return umlClass.getId();
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String target) {
        parent = target;
    }

    public int getSubClassCount() {
        return subCount;
    }

    public void addSubCount() {
        subCount++;
    }

    public void addAttribute(String name) {
        attributes.merge(name, 1, Integer::sum);
    }

    public void addReference(ReferenceType type) {
        references.add(type);
    }

    public void addRealization(String target) {
        realizations.add(target);
    }

    public void addOperation(MyOperation myOperation) {
        operations.add(myOperation);
    }

    public int getOperationCount() {
        return operations.size();
    }

    public HashSet<String> getAttributeCouplingSet() {
        HashSet<String> result = new HashSet<>();
        for (ReferenceType referenceType : references) {
            result.add(referenceType.getReferenceId());
        }
        if (parent != null) {
            result.addAll(ClassProcess.getInstance().getClass(parent).getAttributeCouplingSet());
        }
        return result;
    }

    public HashSet<String> getClassImplementInterface() {
        HashSet<String> result = new HashSet<>();
        if (parent != null) {
            result.addAll(ClassProcess.getInstance().getClass(parent).getClassImplementInterface());
        }
        result.addAll(realizations);
        for (String interfaceId : realizations) {
            result.addAll(InterfaceProcess.getInstance().getInterface(interfaceId).getParents());
        }
        return result;
    }

    public ArrayList<MyOperation> getOperations(String methodName) {
        ArrayList<MyOperation> result = new ArrayList<>();
        for (MyOperation umlOperation : operations) {
            if (methodName.equals(umlOperation.getName())) {
                result.add(umlOperation);
            }
        }
        return result;
    }

    public Map<Visibility, Integer> getOperationVisibility(String methodName) {
        HashMap<Visibility, Integer> result = new HashMap<>();
        for (MyOperation umlOperation : operations) {
            if (methodName.equals(umlOperation.getName())) {
                result.merge(umlOperation.getVisibility(), 1, Integer::sum);
            }
        }
        return result;
    }

    public List<Integer> getOperationCouplingDegree(String methodName)
            throws MethodWrongTypeException, MethodDuplicatedException {
        ArrayList<MyOperation> operations = getOperations(methodName);
        if (OperationProcess.getInstance().checkWrong(operations)) {
            throw new MethodWrongTypeException(umlClass.getName(), methodName);
        }
        if (OperationProcess.getInstance().checkSame(operations)) {
            throw new MethodDuplicatedException(umlClass.getName(), methodName);
        }
        ArrayList<Integer> result = new ArrayList<>();
        for (MyOperation umlOperation : operations) {
            result.add(umlOperation.getCouplingDegree());
        }
        return result;
    }

    public void checkR002(HashSet<AttributeClassInformation> result) {
        attributes.forEach((key, value) -> {
            if (value > 1 && key != null && !key.matches("[ \t]*")) {
                result.add(new AttributeClassInformation(key, umlClass.getName()));
            }
        });
    }
}
