import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;

public class HorizontalElevator extends Thread {
    private final RequestTable requestTable;
    private char building = 'A';
    private final int id;
    private final int capacity;
    private final long speed;
    private final int switchInfo;
    private ArrayList<MyPersonRequest> persons;
    private final HashSet<MyPersonRequest> getonpersons = new HashSet<>();
    private final HashSet<MyPersonRequest> getoffpersons;
    private final int floor;
    private boolean clockwise = true;
    private int flag = 0;
    private long lastmove;
    private boolean whethertomove = true;

    public HorizontalElevator(int floor, int id, int capacity, double speed,
                              int switchInfo, RequestTable requestTable) {
        this.floor = floor;
        this.id = id;
        this.capacity = capacity;
        this.requestTable = requestTable;
        this.speed = (speed == 0.2) ? 200 :
                (speed == 0.4) ? 400 : 600;
        this.switchInfo = switchInfo;
        this.persons = new ArrayList<>(capacity);
        this.getoffpersons = new HashSet<>(capacity);
    }

    @Override
    public void run() {
        //currentThread().setName("HorizontalElevator" + id);
        while (true) {
            synchronized (requestTable) {
                if (!hasnext() && persons.isEmpty()) {
                    if (requestTable.isEnd()) {
                        return;
                    }
                    try {
                        lastmove = System.currentTimeMillis();
                        requestTable.wait();
                        if (!hasnext()) {
                            continue;
                        }
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
                whethertomove = hasnext() || (!persons.isEmpty());
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
        ArrayList<MyPersonRequest> newpersons = new ArrayList<>(capacity);
        for (MyPersonRequest request : persons) {
            if (request.getNextBuilding() == building) {
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
            if (reachable(request) && request.getPresentBuilding() == building && match(request)) {
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
        if (!persons.isEmpty()) {
            MyPersonRequest mainrequest = persons.get(0);
            int check = Math.abs(mainrequest.getNextBuilding() -
                    mainrequest.getPresentBuilding() + 1);
            clockwise = check == 2 || check == 3;
        } else if (hasnext()) {
            int distance = 5;
            MyPersonRequest mainrequest = null;
            for (MyPersonRequest request : requestTable.getRequests()) {
                if (!reachable(request)) {
                    continue;
                }
                int temp = Math.abs(building - request.getPresentBuilding());
                if (Math.min(temp, 5 - temp) < distance) {
                    mainrequest = request;
                    distance = temp;
                }
            }
            int check;
            if (mainrequest.getPresentBuilding() == building) {
                check = Math.abs(mainrequest.getNextBuilding() -
                        mainrequest.getPresentBuilding() + 1);
            } else {
                check = Math.abs(mainrequest.getFromBuilding() - building + 1);
            }
            clockwise = check == 2 || check == 3;
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
            request.setTransferred(true);
            request.setNextBuilding(building);
            request.setNextFloor(request.getToFloor());
            request.setPresentBuilding(building);
            request.setPresentFloor(floor);
            WaitQueue.getInstance().add(request);
        }
    }

    public void geton() {
        for (MyPersonRequest request : getonpersons) {
            OutputThread.println(
                    "IN-" + request.getPersonId() + "-" + building + "-" + floor + "-" + id);
        }
    }

    public void move() throws InterruptedException {
        if (clockwise) {
            if (building == 'E') {
                building = 'A';
            } else {
                building = (char) (building + 1);
            }
        } else {
            if (building == 'A') {
                building = 'E';
            } else {
                building = (char) (building - 1);
            }
        }
        sleep(speed);
        OutputThread.println("ARRIVE-" + building + "-" + floor + "-" + id);
    }

    public void quantummove() throws InterruptedException {
        if (clockwise) {
            if (building == 'E') {
                building = 'A';
            } else {
                building = (char) (building + 1);
            }
        } else {
            if (building == 'A') {
                building = 'E';
            } else {
                building = (char) (building - 1);
            }
        }
        long interval = System.currentTimeMillis() - lastmove;
        if (interval < speed) {
            sleep(speed - interval);
        }
        OutputThread.println("ARRIVE-" + building + "-" + floor + "-" + id);
    }

    public boolean match(MyPersonRequest request) {
        int check;
        if (clockwise) {
            check = Math.abs(request.getToBuilding() - request.getFromBuilding() + 1);
        } else {
            check = Math.abs(request.getToBuilding() - request.getFromBuilding() - 1);
        }
        return check == 2 || check == 3;
    }

    public boolean reachable(MyPersonRequest request) {
        return ((switchInfo >> (request.getPresentBuilding() - 'A')) & 1) +
                ((switchInfo >> (request.getNextBuilding() - 'A')) & 1) == 2;
    }

    public boolean hasnext() {
        for (MyPersonRequest request : requestTable.getRequests()) {
            if (reachable(request)) {
                return true;
            }
        }
        return false;
    }

    public void choose(int i) {
        if (getonpersons.size() > i) {
            ArrayList<MyPersonRequest> sortedpersons = new ArrayList<>(getonpersons);
            sortedpersons.sort(
                    Comparator.comparingInt(a -> Math.min(Math.abs(a.getToBuilding() - building),
                            5 - Math.abs(a.getToBuilding() - building))));
            getonpersons.clear();
            for (int j = 0; j < i; j++) {
                getonpersons.add(sortedpersons.get(j));
            }
        }
    }
}
