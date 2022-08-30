import com.oocourse.uml2.models.common.NameableType;
import com.oocourse.uml2.models.common.Visibility;
import com.oocourse.uml2.models.elements.UmlOperation;

import java.util.HashMap;
import java.util.HashSet;

public class MyOperation {
    private final UmlOperation umlOperation;
    private final HashMap<NameableType, Integer> inParameters = new HashMap<>();
    private final HashSet<String> refParameters = new HashSet<>();
    private boolean isError = false;

    public MyOperation(UmlOperation umlOperation) {
        this.umlOperation = umlOperation;
    }

    public String getId() {
        return umlOperation.getId();
    }

    public String getName() {
        return umlOperation.getName();
    }

    public String getParentId() {
        return umlOperation.getParentId();
    }

    public Visibility getVisibility() {
        return umlOperation.getVisibility();
    }

    public void addInParameter(NameableType type) {
        inParameters.merge(type, 1, Integer::sum);
    }

    public void addRefParameter(String referenceId) {
        refParameters.add(referenceId);
    }

    public boolean isError() {
        return isError;
    }

    public void setError() {
        isError = true;
    }

    public boolean isSame(MyOperation myOperation) {
        return !isError && !myOperation.isError && inParameters.equals(myOperation.inParameters);
    }

    public Integer getCouplingDegree() {
        return refParameters.contains(umlOperation.getParentId()) ?
                refParameters.size() - 1 : refParameters.size();
    }
}
