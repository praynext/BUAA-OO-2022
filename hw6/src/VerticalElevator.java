import java.util.ArrayList;
import java.util.HashSet;

import com.oocourse.elevator2.PersonRequest;

public class VerticalElevator extends Thread {
    private final RequestTable requestTable;
    private final char building;
    private final int id;
    private HashSet<PersonRequest> persons = new HashSet<>(6);
    private final HashSet<PersonRequest> getonpersons = new HashSet<>();
    private final HashSet<PersonRequest> getoffpersons = new HashSet<>(6);
    private int floor = 1;
    private boolean up = true;
    private int flag = 0;
    private long lastmove;
    private boolean whethertomove = true;

    public VerticalElevator(char building, int id, RequestTable requestTable) {
        this.building = building;
        this.id = id;
        this.requestTable = requestTable;
    }

    @Override
    public void run() {
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
                whethertomove = hasnext(up) || !persons.isEmpty();
            }
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
        HashSet<PersonRequest> newpersons = new HashSet<>(6);
        for (PersonRequest request : persons) {
            if (request.getToFloor() == floor) {
                getoffpersons.add(request);
            } else {
                newpersons.add(request);
            }
        }
        persons = newpersons;
    }

    public void pickup() {
        getonpersons.clear();
        for (PersonRequest request : requestTable.getRequests()) {
            if (request.getFromFloor() == floor && (request.getToFloor() > floor) == up) {
                getonpersons.add(request);
            }
        }
        choose(6 - persons.size());
        persons.addAll(getonpersons);
        for (PersonRequest request : getonpersons) {
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
        for (PersonRequest request : getoffpersons) {
            OutputThread.println(
                    "OUT-" + request.getPersonId() + "-" + building + "-" + floor + "-" + id);
        }
    }

    public void geton() {
        for (PersonRequest request : getonpersons) {
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
        sleep(400);
        OutputThread.println("ARRIVE-" + building + "-" + floor + "-" + id);
    }

    public void quantummove() throws InterruptedException {
        if (up) {
            floor++;
        } else {
            floor--;
        }
        long interval = System.currentTimeMillis() - lastmove;
        if (interval < 400) {
            sleep(400 - interval);
        }
        OutputThread.println("ARRIVE-" + building + "-" + floor + "-" + id);
    }

    public boolean hasnext(boolean up) {
        for (PersonRequest request : requestTable.getRequests()) {
            if (up) {
                if (request.getFromFloor() > floor) {
                    return true;
                }
            } else {
                if (request.getFromFloor() < floor) {
                    return true;
                }
            }
        }
        return false;
    }

    public void choose(int i) {
        if (getonpersons.size() > i) {
            ArrayList<PersonRequest> sortedpersons = new ArrayList<>(getonpersons);
            sortedpersons.sort((a, b) ->
                    Integer.compare(Math.abs(b.getToFloor() - floor),
                            Math.abs(a.getToFloor() - floor)));
            getonpersons.clear();
            for (int j = 0; j < i; j++) {
                getonpersons.add(sortedpersons.get(j));
            }
        }
    }
}
