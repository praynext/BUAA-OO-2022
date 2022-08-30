import java.util.ArrayList;
import java.util.HashSet;

import com.oocourse.elevator1.PersonRequest;

public class Elevator extends Thread {
    private final RequestTable requestTable;
    private final Character building;
    private final int id;
    private HashSet<PersonRequest> persons;
    private final HashSet<PersonRequest> getonpersons;
    private final HashSet<PersonRequest> getoffpersons;
    private int floor;
    private boolean up;
    private int flag;
    private long lastmove;

    public Elevator(char building, int id, RequestTable requestTable) {
        this.building = building;
        this.id = id;
        this.requestTable = requestTable;
        this.persons = new HashSet<>(6);
        this.getonpersons = new HashSet<>();
        this.getoffpersons = new HashSet<>(6);
        this.floor = 1;
        this.up = true;
        this.flag = 0;
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
                if (flag == 1 && persons.size() == 0) {
                    flag = 2;
                }
                try {
                    getprint();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            try {
                if (hasnext(up) || !persons.isEmpty()) {
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
        if (persons.size() == 0 && !hasnext(up)) {
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
                if (System.currentTimeMillis() - lastopen >= 400) {
                    break;
                }
                requestTable.wait(400 - (System.currentTimeMillis() - lastopen));
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
            requestTable.remove(request);
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
            sleep(400 - (System.currentTimeMillis() - lastmove));
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
            sortedpersons.sort((a, b) -> {
                if (up) {
                    if ((a.getToFloor() - a.getFromFloor())
                            >= (b.getToFloor() - b.getFromFloor())) {
                        return -1;
                    } else {
                        return 1;
                    }
                } else {
                    if ((a.getFromFloor() - a.getToFloor())
                            >= (b.getFromFloor() - b.getToFloor())) {
                        return -1;
                    } else {
                        return 1;
                    }
                }
            });
            getonpersons.clear();
            for (int j = 0; j < i; j++) {
                getonpersons.add(sortedpersons.get(j));
            }
        }
    }
}