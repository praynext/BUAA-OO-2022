import com.oocourse.elevator2.ElevatorInput;
import com.oocourse.elevator2.ElevatorRequest;
import com.oocourse.elevator2.PersonRequest;
import com.oocourse.elevator2.Request;
import oo.util.TimeInput;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class InputHandler extends Thread {
    private final ArrayList<Building> buildings;
    private final ArrayList<Floor> floors;

    public InputHandler(ArrayList<Building> buildings, ArrayList<Floor> floors) {
        this.buildings = buildings;
        this.floors = floors;
    }

    @Override
    public void run() {
        //ElevatorInput elevatorInput = new ElevatorInput(System.in);
        ElevatorInput elevatorInput;
        try {
            elevatorInput = new ElevatorInput(new TimeInput(
                    new FileInputStream("stdin.txt")).getTimedInputStream());
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        while (true) {
            Request request = elevatorInput.nextRequest();
            if (request == null) {
                for (Building building : buildings) {
                    building.setEnd(true);
                }
                for (Floor floor : floors) {
                    floor.setEnd(true);
                }
                break;
            } else {
                if (request instanceof PersonRequest) {
                    PersonRequest personRequest = (PersonRequest) request;
                    if (personRequest.getFromBuilding() == personRequest.getToBuilding()) {
                        buildings.get(personRequest.getToBuilding() - 'A').
                                getRequestTable().add(personRequest);
                    } else {
                        floors.get(personRequest.getToFloor() - 1).
                                getRequestTable().add(personRequest);
                    }
                } else {
                    ElevatorRequest elevatorRequest = (ElevatorRequest) request;
                    if (elevatorRequest.getType().equals("floor")) {
                        floors.get(elevatorRequest.getFloor() - 1).add(elevatorRequest);
                    } else {
                        buildings.get(elevatorRequest.getBuilding() - 'A').add(elevatorRequest);
                    }
                }
            }
        }
        try {
            elevatorInput.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
