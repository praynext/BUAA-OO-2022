package exceptions;

import com.oocourse.spec3.exceptions.PersonIdNotFoundException;

import java.util.HashMap;

public class MyPersonIdNotFoundException extends PersonIdNotFoundException {
    private static final HashMap<Integer, Integer> COUNTER = new HashMap<>();
    private static int totalCount = 0;
    private final int id;

    public MyPersonIdNotFoundException(int id) {
        this.id = id;
        COUNTER.merge(id, 1, Integer::sum);
        totalCount++;
    }

    @Override
    public void print() {
        System.out.println("pinf-" + totalCount + ", " + id + "-" + COUNTER.get(id));
    }
}
