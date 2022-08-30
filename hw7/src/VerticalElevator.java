import java.util.ArrayList;
import java.util.HashSet;

public class VerticalElevator extends Thread {
    private final RequestTable requestTable;
    private final char building;
    private final int id;
    private final int capacity;
    private final long speed;
    private HashSet<MyPersonRequest> persons;
    private final HashSet<MyPersonRequest> getonpersons = new HashSet<>();
    private final HashSet<MyPersonRequest> getoffpersons;
    private int floor = 1;
    private boolean up = true;
    private int flag = 0;
    private long lastmove;
    private boolean whethertomove = true;

    public VerticalElevator(char building, int id, int capacity,
                            double speed, RequestTable requestTable) {
        this.building = building;
        this.id = id;
        this.capacity = capacity;
        this.requestTable = requestTable;
        this.speed = (speed == 0.2) ? 200 :
                (speed == 0.4) ? 400 : 600;
        this.persons = new HashSet<>(capacity);
        this.getoffpersons = new HashSet<>(capacity);
    }

    @Override
    public void run() {
        //currentThread().setName("VerticalElevator" + id);
        while (true) {
            synchronized (requestTable) {
                if (requestTable.isEmpty() && persons.isEmpty()) {
                    if (requestTable.isEnd()) {
                        return;
                    }
                    try {
                        lastmove = System.currentTimeMillis();
                        requestTable.wait();
                        flag = 1;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                pickoff();
                pickup();
                getchange();
                try {
                    getprint();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (flag == 1 && persons.size() == 0) {
                    flag = 2;
                }
                whethertomove = hasnext(up) || (!persons.isEmpty());
            }
            feedback();
            try {
                if (whethertomove) {
                    if (flag == 2) {
                        quantummove();
                    } else {
                        move();
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            flag = 0;
        }
    }

    public void pickoff() {
        getoffpersons.clear();
        HashSet<MyPersonRequest> newpersons = new HashSet<>(capacity);
        for (MyPersonRequest request : persons) {
            if (request.getNextFloor() == floor) {
                getoffpersons.add(request);
            } else {
                newpersons.add(request);
            }
        }
        persons = newpersons;
    }

    public void pickup() {
        getonpersons.clear();
        for (MyPersonRequest request : requestTable.getRequests()) {
            if (request.getPresentFloor() == floor && (request.getNextFloor() > floor) == up) {
                getonpersons.add(request);
            }
        }
        choose(capacity - persons.size());
        persons.addAll(getonpersons);
        for (MyPersonRequest request : getonpersons) {
            requestTable.remove(request);
        }
    }

    public void getchange() {
        if (persons.isEmpty() && !hasnext(up)) {
            up = !up;
            pickup();
        }
    }

    public void getprint() throws InterruptedException {
        if (!(getoffpersons.isEmpty() && getonpersons.isEmpty())) {
            long lastopen = OutputThread.println("OPEN-" + building + "-" + floor + "-" + id);
            getoff();
            geton();
            while (System.currentTimeMillis() - lastopen < 400) {
                pickup();
                geton();
                long interval = System.currentTimeMillis() - lastopen;
                if (interval >= 400) {
                    break;
                }
                requestTable.wait(400 - interval);
            }
            OutputThread.println("CLOSE-" + building + "-" + floor + "-" + id);
        }
    }

    public void getoff() {
        for (MyPersonRequest request : getoffpersons) {
            OutputThread.println(
                    "OUT-" + request.getPersonId() + "-" + building + "-" + floor + "-" + id);
        }
    }

    public void feedback() {
        for (MyPersonRequest request : getoffpersons) {
            if (request.isTransferred()) {
                WaitQueue.getInstance().decrease();
            } else {
                request.setNextBuilding(request.getToBuilding());
                request.setNextFloor(floor);
                request.setPresentBuilding(building);
                request.setPresentFloor(floor);
                WaitQueue.getInstance().add(request);
            }
        }
    }

    public void geton() {
        for (MyPersonRequest request : getonpersons) {
            OutputThread.println(
                    "IN-" + request.getPersonId() + "-" + building + "-" + floor + "-" + id);
        }
    }

    public void move() throws InterruptedException {
        if (up) {
            floor++;
        } else {
            floor--;
        }
        sleep(speed);
        OutputThread.println("ARRIVE-" + building + "-" + floor + "-" + id);
    }

    public void quantummove() throws InterruptedException {
        if (up) {
            floor++;
        } else {
            floor--;
        }
        long interval = System.currentTimeMillis() - lastmove;
        if (interval < speed) {
            sleep(speed - interval);
        }
        OutputThread.println("ARRIVE-" + building + "-" + floor + "-" + id);
    }

    public boolean hasnext(boolean up) {
        if (up) {
            for (MyPersonRequest request : requestTable.getRequests()) {
                if (request.getPresentFloor() > floor) {
                    return true;
                }
            }
        } else {
            for (MyPersonRequest request : requestTable.getRequests()) {
                if (request.getPresentFloor() < floor) {
                    return true;
                }
            }
        }
        return false;
    }

    public void choose(int i) {
        if (getonpersons.size() > i) {
            ArrayList<MyPersonRequest> sortedpersons = new ArrayList<>(getonpersons);
            sortedpersons.sort((a, b) ->
                    Integer.compare(Math.abs(b.getNextFloor() - floor),
                            Math.abs(a.getNextFloor() - floor)));
            getonpersons.clear();
            for (int j = 0; j < i; j++) {
                getonpersons.add(sortedpersons.get(j));
            }
        }
    }
}
