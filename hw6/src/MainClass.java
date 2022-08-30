import com.oocourse.TimableOutput;

import java.util.ArrayList;

public class MainClass {
    public static void main(String[] args) {
        TimableOutput.initStartTimestamp();
        ArrayList<Building> buildings = new ArrayList<>(5);
        ArrayList<Floor> floors = new ArrayList<>(10);
        RequestTable requestTable;
        VerticalElevator verticalElevator;
        Building building;
        for (int i = 0; i < 5; i++) {
            requestTable = new RequestTable();
            building = new Building((char) ('A' + i), requestTable);
            buildings.add(building);
            verticalElevator = new VerticalElevator((char) ('A' + i), i + 1, requestTable);
            verticalElevator.start();
            building.add(verticalElevator);
        }
        Floor floor;
        for (int i = 0; i < 10; i++) {
            requestTable = new RequestTable();
            floor = new Floor(i + 1, requestTable);
            floors.add(floor);
        }
        InputHandler inputHandler = new InputHandler(buildings, floors);
        inputHandler.start();
    }
}
