import com.oocourse.TimableOutput;

public class MainClass {
    public static void main(String[] args) {
        TimableOutput.initStartTimestamp();
        RequestTable requestTable;
        VerticalElevator verticalElevator;
        Building building;
        for (int i = 0; i < 5; i++) {
            requestTable = new RequestTable();
            building = new Building((char) ('A' + i), requestTable);
            TotalBuildings.getInstance().getBuildings().add(building);
            verticalElevator = new VerticalElevator(
                    (char) ('A' + i), i + 1, 8, 0.6, requestTable);
            verticalElevator.start();
            building.add(verticalElevator);
        }
        Floor floor;
        for (int i = 0; i < 10; i++) {
            requestTable = new RequestTable();
            floor = new Floor(i + 1, requestTable);
            TotalFloors.getInstance().getFloors().add(floor);
            if (i == 0) {
                HorizontalElevator horizontalElevator = new HorizontalElevator(
                        1, 6, 8, 0.6, 31, requestTable);
                horizontalElevator.start();
                floor.add(horizontalElevator);
            }
        }
        InputHandler.getInstance().start();
        Scheduler.getInstance().start();
    }
}
