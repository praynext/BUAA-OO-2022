import com.oocourse.uml2.interact.common.Pair;
import com.oocourse.uml2.interact.exceptions.user.ClassDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.ClassNotFoundException;
import com.oocourse.uml2.interact.exceptions.user.InteractionDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.InteractionNotFoundException;
import com.oocourse.uml2.interact.exceptions.user.LifelineCreatedRepeatedlyException;
import com.oocourse.uml2.interact.exceptions.user.LifelineDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.LifelineNeverCreatedException;
import com.oocourse.uml2.interact.exceptions.user.LifelineNotFoundException;
import com.oocourse.uml2.interact.exceptions.user.MethodDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.MethodWrongTypeException;
import com.oocourse.uml2.interact.exceptions.user.StateDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.StateMachineDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.StateMachineNotFoundException;
import com.oocourse.uml2.interact.exceptions.user.StateNotFoundException;
import com.oocourse.uml2.interact.exceptions.user.TransitionNotFoundException;
import com.oocourse.uml2.interact.format.UserApi;
import com.oocourse.uml2.models.common.Visibility;
import com.oocourse.uml2.models.elements.UmlAttribute;
import com.oocourse.uml2.models.elements.UmlClass;
import com.oocourse.uml2.models.elements.UmlElement;
import com.oocourse.uml2.models.elements.UmlEndpoint;
import com.oocourse.uml2.models.elements.UmlEvent;
import com.oocourse.uml2.models.elements.UmlFinalState;
import com.oocourse.uml2.models.elements.UmlGeneralization;
import com.oocourse.uml2.models.elements.UmlInteraction;
import com.oocourse.uml2.models.elements.UmlInterface;
import com.oocourse.uml2.models.elements.UmlInterfaceRealization;
import com.oocourse.uml2.models.elements.UmlLifeline;
import com.oocourse.uml2.models.elements.UmlMessage;
import com.oocourse.uml2.models.elements.UmlOperation;
import com.oocourse.uml2.models.elements.UmlParameter;
import com.oocourse.uml2.models.elements.UmlPseudostate;
import com.oocourse.uml2.models.elements.UmlRegion;
import com.oocourse.uml2.models.elements.UmlState;
import com.oocourse.uml2.models.elements.UmlStateMachine;
import com.oocourse.uml2.models.elements.UmlTransition;

import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class MyImplementation implements UserApi {
    private final HashSet<UmlClass> umlClasses = new HashSet<>();
    private final HashSet<UmlAttribute> umlAttributes = new HashSet<>();
    private final HashSet<UmlOperation> umlOperations = new HashSet<>();
    private final HashSet<UmlParameter> umlParameters = new HashSet<>();
    private final HashSet<UmlGeneralization> umlGeneralizations = new HashSet<>();
    private final HashSet<UmlInterfaceRealization> umlInterfaceRealizations = new HashSet<>();
    private final HashSet<UmlInterface> umlInterfaces = new HashSet<>();
    private final HashSet<UmlStateMachine> umlStateMachines = new HashSet<>();
    private final HashSet<UmlRegion> umlRegions = new HashSet<>();
    private final HashSet<UmlPseudostate> umlPseudostates = new HashSet<>();
    private final HashSet<UmlFinalState> umlFinalStates = new HashSet<>();
    private final HashSet<UmlState> umlStates = new HashSet<>();
    private final HashSet<UmlTransition> umlTransitions = new HashSet<>();
    private final HashSet<UmlEvent> umlEvents = new HashSet<>();
    private final HashSet<UmlInteraction> umlInteractions = new HashSet<>();
    private final HashSet<UmlLifeline> umlLifeLines = new HashSet<>();
    private final HashSet<UmlMessage> umlMessages = new HashSet<>();
    private final HashSet<UmlEndpoint> umlEndPoints = new HashSet<>();

    public MyImplementation(UmlElement[] elements) {
        for (UmlElement element : elements) {
            switch (element.getElementType()) {
                case UML_CLASS:
                case UML_ATTRIBUTE:
                case UML_OPERATION:
                case UML_PARAMETER:
                case UML_GENERALIZATION:
                case UML_INTERFACE_REALIZATION:
                case UML_INTERFACE:
                    switchClassDiagram(element);
                    break;
                case UML_STATE_MACHINE:
                case UML_REGION:
                case UML_PSEUDOSTATE:
                case UML_FINAL_STATE:
                case UML_STATE:
                case UML_TRANSITION:
                case UML_EVENT:
                    switchStateDiagram(element);
                    break;
                case UML_INTERACTION:
                case UML_LIFELINE:
                case UML_MESSAGE:
                case UML_ENDPOINT:
                    switchSequenceDiagram(element);
                    break;
                default:
                    break;
            }
        }
        dealClassDiagram();
        dealStateDiagram();
        dealSequenceDiagram();
    }

    private void switchClassDiagram(UmlElement element) {
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

    private void switchStateDiagram(UmlElement element) {
        switch (element.getElementType()) {
            case UML_STATE_MACHINE:
                umlStateMachines.add((UmlStateMachine) element);
                break;
            case UML_REGION:
                umlRegions.add((UmlRegion) element);
                break;
            case UML_PSEUDOSTATE:
                umlPseudostates.add((UmlPseudostate) element);
                break;
            case UML_FINAL_STATE:
                umlFinalStates.add((UmlFinalState) element);
                break;
            case UML_STATE:
                umlStates.add((UmlState) element);
                break;
            case UML_TRANSITION:
                umlTransitions.add((UmlTransition) element);
                break;
            case UML_EVENT:
                umlEvents.add((UmlEvent) element);
                break;
            default:
                break;
        }
    }

    private void switchSequenceDiagram(UmlElement element) {
        switch (element.getElementType()) {
            case UML_INTERACTION:
                umlInteractions.add((UmlInteraction) element);
                break;
            case UML_LIFELINE:
                umlLifeLines.add((UmlLifeline) element);
                break;
            case UML_MESSAGE:
                umlMessages.add((UmlMessage) element);
                break;
            case UML_ENDPOINT:
                umlEndPoints.add((UmlEndpoint) element);
                break;
            default:
                break;
        }
    }

    private void dealClassDiagram() {
        ClassProcess.getInstance().dealClass(umlClasses);
        InterfaceProcess.getInstance().dealInterface(umlInterfaces);
        ClassProcess.getInstance().dealAttribute(umlAttributes);
        dealGeneralization();
        dealOperation();
        ClassProcess.getInstance().dealInterfaceRealization(umlInterfaceRealizations);
        OperationProcess.getInstance().dealParameter(umlParameters);
    }

    private void dealGeneralization() {
        for (UmlGeneralization umlGeneralization : umlGeneralizations) {
            if (ClassProcess.getInstance().contains(umlGeneralization.getSource())) {
                ClassProcess.getInstance().dealGeneralization(umlGeneralization);
            } else {
                InterfaceProcess.getInstance().dealGeneralization(umlGeneralization);
            }
        }
    }

    private void dealOperation() {
        for (UmlOperation umlOperation : umlOperations) {
            MyOperation myOperation = new MyOperation(umlOperation);
            OperationProcess.getInstance().dealOperation(myOperation);
            ClassProcess.getInstance().dealOperation(myOperation);
        }
    }

    private void dealStateDiagram() {
        MachineProcess.getInstance().dealMachine(umlStateMachines);
        MachineProcess.getInstance().dealRegion(umlRegions);
        MachineProcess.getInstance().dealPseudostate(umlPseudostates);
        MachineProcess.getInstance().dealFinalState(umlFinalStates);
        MachineProcess.getInstance().dealState(umlStates);
        MachineProcess.getInstance().dealTransition(umlTransitions);
        MachineProcess.getInstance().dealEvent(umlEvents);
    }

    private void dealSequenceDiagram() {
        InteractionProcess.getInstance().dealInteraction(umlInteractions);
        InteractionProcess.getInstance().dealLifeLine(umlLifeLines);
        InteractionProcess.getInstance().dealEndPoint(umlEndPoints);
        InteractionProcess.getInstance().dealMessage(umlMessages);
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

    @Override
    public int getParticipantCount(String s)
            throws InteractionNotFoundException, InteractionDuplicatedException {
        return InteractionProcess.getInstance().getParticipantCount(s);
    }

    @Override
    public UmlLifeline getParticipantCreator(String s, String s1)
            throws InteractionNotFoundException, InteractionDuplicatedException,
            LifelineNotFoundException, LifelineDuplicatedException,
            LifelineNeverCreatedException, LifelineCreatedRepeatedlyException {
        return InteractionProcess.getInstance().getParticipantCreator(s, s1);
    }

    @Override
    public Pair<Integer, Integer> getParticipantLostAndFound(String s, String s1)
            throws InteractionNotFoundException, InteractionDuplicatedException,
            LifelineNotFoundException, LifelineDuplicatedException {
        return InteractionProcess.getInstance().getParticipantLostAndFound(s, s1);
    }

    @Override
    public int getStateCount(String s)
            throws StateMachineNotFoundException, StateMachineDuplicatedException {
        return MachineProcess.getInstance().getStateCount(s);
    }

    @Override
    public boolean getStateIsCriticalPoint(String s, String s1)
            throws StateMachineNotFoundException, StateMachineDuplicatedException,
            StateNotFoundException, StateDuplicatedException {
        return MachineProcess.getInstance().getStateIsCriticalPoint(s, s1);
    }

    @Override
    public List<String> getTransitionTrigger(String s, String s1, String s2)
            throws StateMachineNotFoundException, StateMachineDuplicatedException,
            StateNotFoundException, StateDuplicatedException, TransitionNotFoundException {
        return MachineProcess.getInstance().getTransitionTrigger(s, s1, s2);
    }
}