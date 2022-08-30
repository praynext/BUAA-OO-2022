import com.oocourse.uml3.interact.common.Pair;
import com.oocourse.uml3.interact.exceptions.user.LifelineCreatedRepeatedlyException;
import com.oocourse.uml3.interact.exceptions.user.LifelineNeverCreatedException;
import com.oocourse.uml3.models.elements.UmlLifeline;

public class MyLifeline {
    private final UmlLifeline umlLifeline;
    private String creator = null;
    private int found = 0;
    private int lost = 0;
    private boolean deleted = false;

    public MyLifeline(UmlLifeline umlLifeline) {
        this.umlLifeline = umlLifeline;
    }

    public UmlLifeline getLifeline() {
        return umlLifeline;
    }

    public String getId() {
        return umlLifeline.getId();
    }

    public String getName() {
        return umlLifeline.getName();
    }

    public String getRepresent() {
        return umlLifeline.getRepresent();
    }

    public void addCreator(String source) {
        if (creator == null) {
            creator = source;
        } else {
            creator = "null";
        }
    }

    public void addFound() {
        found++;
    }

    public void addLost() {
        lost++;
    }

    public String getCreator(String name)
            throws LifelineNeverCreatedException, LifelineCreatedRepeatedlyException {
        if (creator == null) {
            throw new LifelineNeverCreatedException(name, umlLifeline.getName());
        } else if (creator.equals("null")) {
            throw new LifelineCreatedRepeatedlyException(name, umlLifeline.getName());
        } else {
            return creator;
        }
    }

    public Pair<Integer, Integer> getParticipantLostAndFound() {
        return new Pair<>(found, lost);
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
