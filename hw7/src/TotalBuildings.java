import com.oocourse.elevator3.ElevatorRequest;

import java.util.ArrayList;
import java.util.HashSet;

public class TotalBuildings {
    private final ArrayList<Building> buildings = new ArrayList<>(5);
    private boolean flushed;
    private final HashSet<MyPersonRequest> flushedRequest = new HashSet<>();
    private final HashSet<MyPersonRequest> removedRequest = new HashSet<>();
    private static final TotalBuildings TOTAL_BUILDINGS = new TotalBuildings();

    public static TotalBuildings getInstance() {
        return TOTAL_BUILDINGS;
    }

    public ArrayList<Building> getBuildings() {
        return buildings;
    }

    public void setEnd(boolean isEnd) {
        for (Building building : buildings) {
            building.setEnd(isEnd);
        }
    }

    public void flush(ElevatorRequest elevatorRequest) {
        flushed = false;
        flushedRequest.clear();
        removedRequest.clear();
        for (Building building : buildings) {
            synchronized (building.getRequestTable()) {
                for (MyPersonRequest request : building.getRequestTable().getRequests()) {
                    if (!request.isTransferred() && reachable(elevatorRequest, request)) {
                        int distance = Math.abs(request.getFromFloor() - request.getNextFloor()) +
                                Math.abs(request.getToFloor() - request.getNextFloor());
                        int temp = Math.abs(request.getFromFloor() - elevatorRequest.getFloor()) +
                                Math.abs(request.getToFloor() - elevatorRequest.getFloor());
                        if (temp <= distance) {
                            request.setNextFloor(elevatorRequest.getFloor());
                            if (request.getPresentFloor() == elevatorRequest.getFloor()) {
                                request.setNextBuilding(request.getToBuilding());
                                removedRequest.add(request);
                                flushedRequest.add(request);
                                flushed = true;
                            }
                        }
                    }
                }
                for (MyPersonRequest request : removedRequest) {
                    building.getRequestTable().remove(request);
                }
            }
        }
        if (flushed) {
            synchronized (WaitQueue.getInstance()) {
                for (MyPersonRequest request : flushedRequest) {
                    WaitQueue.getInstance().add(request);
                }
            }
        }
    }

    public boolean reachable(ElevatorRequest elevatorRequest, MyPersonRequest request) {
        return ((elevatorRequest.getSwitchInfo() >> (request.getFromBuilding() - 'A')) & 1) +
                ((elevatorRequest.getSwitchInfo() >> (request.getToBuilding() - 'A')) & 1) == 2;
    }
}
