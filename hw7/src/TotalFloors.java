import com.oocourse.elevator3.ElevatorRequest;

import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class TotalFloors {
    private final ArrayList<Floor> floors = new ArrayList<>(10);
    private static final TotalFloors TOTAL_FLOORS = new TotalFloors();
    private ReentrantReadWriteLock reentrantLock = new ReentrantReadWriteLock();
    private ReentrantReadWriteLock.ReadLock readLock = reentrantLock.readLock();
    private ReentrantReadWriteLock.WriteLock writeLock = reentrantLock.writeLock();

    public static TotalFloors getInstance() {
        return TOTAL_FLOORS;
    }

    public ArrayList<Floor> getFloors() {
        return floors;
    }

    public int search(MyPersonRequest request) {
        readLock.lock();
        try {
            int distance = 20;
            int transferFloor = 1;
            for (Floor floor : floors) {
                for (HorizontalElevator horizontalElevator : floor.getElevators()) {
                    if (horizontalElevator.reachable(request)) {
                        int temp = Math.abs(request.getPresentFloor() - floor.getFloorID()) +
                                Math.abs(request.getToFloor() - floor.getFloorID());
                        if (temp <= distance) {
                            distance = temp;
                            transferFloor = floor.getFloorID();
                        }
                    }
                }
            }
            return transferFloor;
        } finally {
            readLock.unlock();
        }
    }

    public void add(ElevatorRequest elevatorRequest) {
        writeLock.lock();
        try {
            floors.get(elevatorRequest.getFloor() - 1).add(elevatorRequest);
        } finally {
            writeLock.unlock();
        }
    }

    public void setEnd(boolean isEnd) {
        for (Floor floor : floors) {
            floor.setEnd(isEnd);
        }
    }
}
