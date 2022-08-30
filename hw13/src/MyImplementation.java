import com.oocourse.uml1.interact.exceptions.user.ClassDuplicatedException;
import com.oocourse.uml1.interact.exceptions.user.ClassNotFoundException;
import com.oocourse.uml1.interact.exceptions.user.MethodDuplicatedException;
import com.oocourse.uml1.interact.exceptions.user.MethodWrongTypeException;
import com.oocourse.uml1.interact.format.UserApi;
import com.oocourse.uml1.models.common.Visibility;
import com.oocourse.uml1.models.elements.UmlAttribute;
import com.oocourse.uml1.models.elements.UmlClass;
import com.oocourse.uml1.models.elements.UmlElement;
import com.oocourse.uml1.models.elements.UmlGeneralization;
import com.oocourse.uml1.models.elements.UmlInterface;
import com.oocourse.uml1.models.elements.UmlInterfaceRealization;
import com.oocourse.uml1.models.elements.UmlOperation;
import com.oocourse.uml1.models.elements.UmlParameter;

import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class MyImplementation implements UserApi {
    public MyImplementation(UmlElement[] elements) {
        HashSet<UmlClass> umlClasses = new HashSet<>();
        HashSet<UmlAttribute> umlAttributes = new HashSet<>();
        HashSet<UmlOperation> umlOperations = new HashSet<>();
        HashSet<UmlParameter> umlParameters = new HashSet<>();
        HashSet<UmlGeneralization> umlGeneralizations = new HashSet<>();
        HashSet<UmlInterfaceRealization> umlInterfaceRealizations = new HashSet<>();
        HashSet<UmlInterface> umlInterfaces = new HashSet<>();
        for (UmlElement element : elements) {
            switch (element.getElementType()) {
                case UML_CLASS:
                    umlClasses.add((UmlClass) element);
                    break;
                case UML_ATTRIBUTE:
                    umlAttributes.add((UmlAttribute) element);
                    break;
                case UML_OPERATION:
                    umlOperations.add((UmlOperation) element);
                    break;
                case UML_PARAMETER:
                    umlParameters.add((UmlParameter) element);
                    break;
                case UML_GENERALIZATION:
                    umlGeneralizations.add((UmlGeneralization) element);
                    break;
                case UML_INTERFACE_REALIZATION:
                    umlInterfaceRealizations.add((UmlInterfaceRealization) element);
                    break;
                case UML_INTERFACE:
                    umlInterfaces.add((UmlInterface) element);
                    break;
                default:
                    break;
            }
        }
        ClassProcess.getInstance().dealClass(umlClasses);
        InterfaceProcess.getInstance().dealInterface(umlInterfaces);
        ClassProcess.getInstance().dealAttribute(umlAttributes);
        dealGeneralization(umlGeneralizations);
        ClassProcess.getInstance().dealInterfaceRealization(umlInterfaceRealizations);
        OperationProcess.getInstance().dealOperation(umlOperations);
        ClassProcess.getInstance().dealOperation(umlOperations);
        OperationProcess.getInstance().dealParameter(umlParameters);
    }

    private void dealGeneralization(HashSet<UmlGeneralization> umlGeneralizations) {
        for (UmlGeneralization umlGeneralization : umlGeneralizations) {
            if (ClassProcess.getInstance().contains(umlGeneralization.getSource())) {
                ClassProcess.getInstance().dealGeneralization(umlGeneralization);
            } else {
                InterfaceProcess.getInstance().dealGeneralization(umlGeneralization);
            }
        }
    }

    @Override
    public int getClassCount() {
        return ClassProcess.getInstance().getClassCount();
    }

    @Override
    public int getClassSubClassCount(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        return ClassProcess.getInstance().getClassSubClassCount(className);
    }

    @Override
    public int getClassOperationCount(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        return ClassProcess.getInstance().getClassOperationCount(className);
    }

    @Override
    public Map<Visibility, Integer> getClassOperationVisibility(String className, String methodName)
            throws ClassNotFoundException, ClassDuplicatedException {
        return ClassProcess.getInstance().getClassOperationVisibility(className, methodName);
    }

    @Override
    public List<Integer> getClassOperationCouplingDegree(String className, String methodName)
            throws ClassNotFoundException, ClassDuplicatedException,
            MethodWrongTypeException, MethodDuplicatedException {
        return ClassProcess.getInstance().getClassOperationCouplingDegree(className, methodName);
    }

    @Override
    public int getClassAttributeCouplingDegree(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        return ClassProcess.getInstance().getClassAttributeCouplingDegree(className);
    }

    @Override
    public List<String> getClassImplementInterfaceList(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        return ClassProcess.getInstance().getClassImplementInterfaceList(className);
    }

    @Override
    public int getClassDepthOfInheritance(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        return ClassProcess.getInstance().getClassDepthOfInheritance(className);
    }
}