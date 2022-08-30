import com.oocourse.elevator3.PersonRequest;

public class MyPersonRequest extends PersonRequest {
    private int nextFloor;
    private char nextBuilding;
    private int presentFloor;
    private char presentBuilding;
    private boolean transferred = false;

    public MyPersonRequest(PersonRequest personRequest) {
        super(personRequest.getFromFloor(), personRequest.getToFloor(),
                personRequest.getFromBuilding(), personRequest.getToBuilding(),
                personRequest.getPersonId());
        this.presentFloor = personRequest.getFromFloor();
        this.presentBuilding = personRequest.getFromBuilding();
        this.nextFloor = personRequest.getToFloor();
        this.nextBuilding = personRequest.getToBuilding();
    }

    public int getNextFloor() {
        return nextFloor;
    }

    public void setNextFloor(int nextFloor) {
        this.nextFloor = nextFloor;
    }

    public char getNextBuilding() {
        return nextBuilding;
    }

    public void setNextBuilding(char nextBuilding) {
        this.nextBuilding = nextBuilding;
    }

    public int getPresentFloor() {
        return presentFloor;
    }

    public void setPresentFloor(int presentFloor) {
        this.presentFloor = presentFloor;
    }

    public char getPresentBuilding() {
        return presentBuilding;
    }

    public void setPresentBuilding(char presentBuilding) {
        this.presentBuilding = presentBuilding;
    }

    public boolean isTransferred() {
        return transferred;
    }

    public void setTransferred(boolean transferred) {
        this.transferred = transferred;
    }
}
