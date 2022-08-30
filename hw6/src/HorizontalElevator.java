import com.oocourse.elevator2.PersonRequest;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;

public class HorizontalElevator extends Thread {
    private final RequestTable requestTable;
    private char building = 'A';
    private final int id;
    private ArrayList<PersonRequest> persons = new ArrayList<>(6);
    private final HashSet<PersonRequest> getonpersons = new HashSet<>();
    private final HashSet<PersonRequest> getoffpersons = new HashSet<>(6);
    private final int floor;
    private boolean clockwise = true;
    private int flag = 0;
    private long lastmove;
    private boolean whethertomove = true;

    public HorizontalElevator(int floor, int id, RequestTable requestTable) {
        this.floor = floor;
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
                whethertomove = !(requestTable.isEmpty() && persons.isEmpty());
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
        ArrayList<PersonRequest> newpersons = new ArrayList<>(6);
        for (PersonRequest request : persons) {
            if (request.getToBuilding() == building) {
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
            if (request.getFromBuilding() == building && match(request)) {
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
        if (!persons.isEmpty()) {
            PersonRequest mainrequest = persons.get(0);
            int check = Math.abs(mainrequest.getToBuilding() - mainrequest.getFromBuilding() + 1);
            clockwise = check == 2 || check == 3;
        } else if (!requestTable.isEmpty()) {
            int distance = 5;
            PersonRequest mainrequest = null;
            for (PersonRequest request : requestTable.getRequests()) {
                if (Math.abs(request.getToBuilding() - request.getFromBuilding()) < distance) {
                    mainrequest = request;
                }
            }
            int check = Math.abs(mainrequest.getToBuilding() - mainrequest.getFromBuilding() + 1);
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
        sleep(200);
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
        if (interval < 200) {
            sleep(200 - interval);
        }
        OutputThread.println("ARRIVE-" + building + "-" + floor + "-" + id);
    }

    public boolean match(PersonRequest request) {
        int check;
        if (clockwise) {
            check = Math.abs(request.getToBuilding() - request.getFromBuilding() + 1);
        } else {
            check = Math.abs(request.getToBuilding() - request.getFromBuilding() - 1);
        }
        return check == 2 || check == 3;
    }

    public void choose(int i) {
        if (getonpersons.size() > i) {
            ArrayList<PersonRequest> sortedpersons = new ArrayList<>(getonpersons);
            sortedpersons.sort(
                    Comparator.comparingInt(a -> Math.abs(a.getToBuilding() - building)));
            getonpersons.clear();
            for (int j = 0; j < i; j++) {
                getonpersons.add(sortedpersons.get(j));
            }
        }
    }
}
