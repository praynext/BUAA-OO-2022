import com.oocourse.elevator2.ElevatorRequest;

import java.util.ArrayList;

public class Floor {
    private final int floorID;
    private final ArrayList<HorizontalElevator> elevators = new ArrayList<>();
    private final RequestTable requestTable;

    public Floor(int floorID, RequestTable requestTable) {
        this.floorID = floorID;
        this.requestTable = requestTable;
    }

    public RequestTable getRequestTable() {
        return requestTable;
    }

    public void add(ElevatorRequest request) {
        HorizontalElevator elevator = new HorizontalElevator(
                request.getFloor(), request.getElevatorId(), requestTable);
        elevator.start();
        elevators.add(elevator);
    }

    public void setEnd(boolean isEnd) {
        requestTable.setEnd(isEnd);
    }
}
