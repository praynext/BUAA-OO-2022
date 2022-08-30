import com.oocourse.elevator3.ElevatorInput;
import com.oocourse.elevator3.ElevatorRequest;
import com.oocourse.elevator3.PersonRequest;
import com.oocourse.elevator3.Request;
import oo.util.TimeInput;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class InputHandler extends Thread {
    private static final InputHandler INPUT_HANDLER = new InputHandler();

    public static InputHandler getInstance() {
        return INPUT_HANDLER;
    }

    @Override
    public void run() {
        //currentThread().setName("InputHandler");
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
                WaitQueue.getInstance().setEnd(true);
                break;
            } else {
                if (request instanceof PersonRequest) {
                    MyPersonRequest myPersonRequest = new MyPersonRequest((PersonRequest) request);
                    synchronized (WaitQueue.getInstance()) {
                        WaitQueue.getInstance().add(myPersonRequest);
                        WaitQueue.getInstance().increase();
                    }
                } else {
                    ElevatorRequest elevatorRequest = (ElevatorRequest) request;
                    if (elevatorRequest.getType().equals("floor")) {
                        TotalFloors.getInstance().add(elevatorRequest);
                        TotalBuildings.getInstance().flush(elevatorRequest);
                    } else {
                        TotalBuildings.getInstance().getBuildings().
                                get(elevatorRequest.getBuilding() - 'A').add(elevatorRequest);
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
