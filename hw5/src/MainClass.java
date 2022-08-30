import com.oocourse.TimableOutput;

import java.util.ArrayList;

public class MainClass {
    public static void main(String[] args) {
        TimableOutput.initStartTimestamp();
        ArrayList<RequestTable> requestTables = new ArrayList<>(5);
        RequestTable requestTable;
        Elevator elevator;
        for (int i = 0; i < 5; i++) {
            requestTable = new RequestTable();
            requestTables.add(requestTable);
            elevator = new Elevator((char) ('A' + i), i + 1, requestTable);
            elevator.start();
        }
        InputHandler inputHandler = new InputHandler(requestTables);
        inputHandler.start();
    }
}
