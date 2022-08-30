import com.oocourse.uml3.models.elements.UmlAssociation;
import com.oocourse.uml3.models.elements.UmlAssociationEnd;

import java.util.HashMap;
import java.util.HashSet;

public class AssociationProcess {
    private static final AssociationProcess ASSOCIATION_PROCESS = new AssociationProcess();
    private final HashMap<String, UmlAssociationEnd> id2associationEnd = new HashMap<>();

    public static AssociationProcess getInstance() {
        return ASSOCIATION_PROCESS;
    }

    public void dealAssociation(HashSet<UmlAssociation> umlAssociations) {
        for (UmlAssociation umlAssociation : umlAssociations) {
            UmlAssociationEnd associationEnd1 = id2associationEnd.get(umlAssociation.getEnd1());
            UmlAssociationEnd associationEnd2 = id2associationEnd.get(umlAssociation.getEnd2());
            MyClass myClass1 = ClassProcess.getInstance().getClass(associationEnd1.getReference());
            if (myClass1 != null) {
                myClass1.addAttribute(associationEnd2.getName());
            }
            MyClass myClass2 = ClassProcess.getInstance().getClass(associationEnd2.getReference());
            if (myClass2 != null) {
                myClass2.addAttribute(associationEnd1.getName());
            }
        }
    }

    public void dealAssociationEnd(HashSet<UmlAssociationEnd> umlAssociationEnds) {
        for (UmlAssociationEnd umlAssociationEnd : umlAssociationEnds) {
            id2associationEnd.put(umlAssociationEnd.getId(), umlAssociationEnd);
        }
    }
}
