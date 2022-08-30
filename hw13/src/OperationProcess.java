import com.oocourse.uml1.models.common.Direction;
import com.oocourse.uml1.models.common.NameableType;
import com.oocourse.uml1.models.common.NamedType;
import com.oocourse.uml1.models.common.ReferenceType;
import com.oocourse.uml1.models.elements.UmlOperation;
import com.oocourse.uml1.models.elements.UmlParameter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class OperationProcess {
    private static final OperationProcess OPERATION_PROCESS = new OperationProcess();
    private final HashMap<String, UmlOperation> id2operation = new HashMap<>();
    private final HashMap<String, HashMap<NameableType, Integer>> id2inParameters = new HashMap<>();
    private final HashMap<String, HashSet<String>> id2refParameters = new HashMap<>();
    private final HashSet<String> errorId = new HashSet<>();
    private static final HashSet<String> PARAMETER_TYPES = new HashSet<String>() {
        {
            this.add("byte");
            this.add("short");
            this.add("int");
            this.add("long");
            this.add("float");
            this.add("double");
            this.add("char");
            this.add("boolean");
            this.add("String");
        }
    };

    public static OperationProcess getInstance() {
        return OPERATION_PROCESS;
    }

    public void dealOperation(HashSet<UmlOperation> umlOperations) {
        for (UmlOperation umlOperation : umlOperations) {
            id2operation.put(umlOperation.getId(), umlOperation);
        }
    }

    public void dealParameter(HashSet<UmlParameter> umlParameters) {
        for (UmlParameter umlParameter : umlParameters) {
            NameableType type = umlParameter.getType();
            if (umlParameter.getDirection() == Direction.IN) {
                if (id2inParameters.containsKey(umlParameter.getParentId())) {
                    id2inParameters.get(umlParameter.getParentId()).merge(type, 1, Integer::sum);
                } else {
                    HashMap<NameableType, Integer> inParameters = new HashMap<>();
                    inParameters.put(type, 1);
                    id2inParameters.put(umlParameter.getParentId(), inParameters);
                }
            }
            if (type instanceof NamedType) {
                if (isError(umlParameter.getDirection(), (NamedType) type)) {
                    errorId.add(umlParameter.getParentId());
                }
            } else if (type instanceof ReferenceType) {
                if (id2refParameters.containsKey(umlParameter.getParentId())) {
                    id2refParameters.get(umlParameter.getParentId()).
                            add(((ReferenceType) type).getReferenceId());
                } else {
                    HashSet<String> refParameters = new HashSet<>();
                    refParameters.add(((ReferenceType) type).getReferenceId());
                    id2refParameters.put(umlParameter.getParentId(), refParameters);
                }
            }
        }
    }

    private boolean isError(Direction direction, NamedType type) {
        return direction == Direction.IN ? !PARAMETER_TYPES.contains(type.getName()) :
                !PARAMETER_TYPES.contains(type.getName()) && !type.getName().equals("void");
    }

    public List<Integer> getClassOperationCouplingDegree(
            String id, ArrayList<UmlOperation> operations) {
        ArrayList<Integer> result = new ArrayList<>();
        for (UmlOperation umlOperation : operations) {
            int num = 0;
            if (id2refParameters.containsKey(umlOperation.getId())) {
                num = id2refParameters.get(umlOperation.getId()).size();
                if (id2refParameters.get(umlOperation.getId()).contains(id)) {
                    num -= 1;
                }
            }
            result.add(num);
        }
        return result;
    }

    public boolean checkWrong(ArrayList<UmlOperation> umlOperations) {
        for (UmlOperation umlOperation : umlOperations) {
            if (errorId.contains(umlOperation.getId())) {
                return true;
            }
        }
        return false;
    }

    public boolean checkSame(ArrayList<UmlOperation> umlOperations) {
        for (int i = 0; i < umlOperations.size() - 1; ++i) {
            for (int j = i + 1; j < umlOperations.size(); ++j) {
                if (isSame(umlOperations.get(i), umlOperations.get(j))) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isSame(UmlOperation umlOperation1, UmlOperation umlOperation2) {
        HashMap<NameableType, Integer> same = new HashMap<>();
        return !errorId.contains(umlOperation1.getId()) &&
                !errorId.contains(umlOperation2.getId()) &&
                id2inParameters.getOrDefault(umlOperation1.getId(), same).
                        equals(id2inParameters.getOrDefault(umlOperation2.getId(), same));
    }
}
