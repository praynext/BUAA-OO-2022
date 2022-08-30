import com.oocourse.uml3.models.common.Direction;
import com.oocourse.uml3.models.common.NameableType;
import com.oocourse.uml3.models.common.NamedType;
import com.oocourse.uml3.models.common.ReferenceType;
import com.oocourse.uml3.models.elements.UmlParameter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class OperationProcess {
    private static final OperationProcess OPERATION_PROCESS = new OperationProcess();
    private final HashMap<String, MyOperation> id2operation = new HashMap<>();
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

    public void dealOperation(MyOperation myOperation) {
        id2operation.put(myOperation.getId(), myOperation);
    }

    public void dealParameter(HashSet<UmlParameter> umlParameters) {
        for (UmlParameter umlParameter : umlParameters) {
            NameableType type = umlParameter.getType();
            MyOperation operation = id2operation.get(umlParameter.getParentId());
            if (umlParameter.getDirection() == Direction.IN) {
                operation.addInParameter(type);
            }
            if (type instanceof NamedType) {
                if (isError(umlParameter.getDirection(), (NamedType) type)) {
                    operation.setError();
                }
            } else if (type instanceof ReferenceType) {
                operation.addRefParameter(((ReferenceType) type).getReferenceId());
            }
        }
    }

    private boolean isError(Direction direction, NamedType type) {
        return direction == Direction.IN ? !PARAMETER_TYPES.contains(type.getName()) :
                !PARAMETER_TYPES.contains(type.getName()) && !type.getName().equals("void");
    }

    public boolean checkWrong(ArrayList<MyOperation> umlOperations) {
        for (MyOperation umlOperation : umlOperations) {
            if (umlOperation.isError()) {
                return true;
            }
        }
        return false;
    }

    public boolean checkSame(ArrayList<MyOperation> umlOperations) {
        for (int i = 0; i < umlOperations.size() - 1; ++i) {
            for (int j = i + 1; j < umlOperations.size(); ++j) {
                if (umlOperations.get(i).isSame(umlOperations.get(j))) {
                    return true;
                }
            }
        }
        return false;
    }
}
