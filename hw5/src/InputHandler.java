import com.oocourse.elevator1.ElevatorInput;
import com.oocourse.elevator1.PersonRequest;

import java.io.IOException;
import java.util.ArrayList;

public class InputHandler extends Thread {
    private final ArrayList<RequestTable> requestTables;

    public InputHandler(ArrayList<RequestTable> requestTables) {
        this.requestTables = requestTables;
    }

    @Override
    public void run() {
        ElevatorInput elevatorInput = new ElevatorInput(System.in);
        PersonRequest request;
        while (true) {
            request = elevatorInput.nextPersonRequest();
            if (request == null) {
                for (RequestTable requestTable : requestTables) {
                    requestTable.setEnd(true);
                }
                break;
            } else {
                requestTables.get(request.getFromBuilding() - 'A').add(request);
            }
        }
        try {
            elevatorInput.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
