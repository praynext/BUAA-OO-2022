import com.oocourse.elevator2.ElevatorRequest;

import java.util.ArrayList;

public class Building {
    private final char buildingID;
    private final ArrayList<VerticalElevator> elevators = new ArrayList<>();
    private final RequestTable requestTable;

    public Building(char buildingID, RequestTable requestTable) {
        this.buildingID = buildingID;
        this.requestTable = requestTable;
    }

    public RequestTable getRequestTable() {
        return requestTable;
    }

    public void add(ElevatorRequest request) {
        VerticalElevator elevator = new VerticalElevator(
                request.getBuilding(), request.getElevatorId(), requestTable);
        elevator.start();
        elevators.add(elevator);
    }

    public void add(VerticalElevator elevator) {
        elevators.add(elevator);
    }

    public void setEnd(boolean isEnd) {
        requestTable.setEnd(isEnd);
    }
}
