package exceptions;

import com.oocourse.spec3.exceptions.GroupIdNotFoundException;

import java.util.HashMap;

public class MyGroupIdNotFoundException extends GroupIdNotFoundException {
    private static final HashMap<Integer, Integer> COUNTER = new HashMap<>();
    private static int totalCount = 0;
    private final int id;

    public MyGroupIdNotFoundException(int id) {
        this.id = id;
        COUNTER.merge(id, 1, Integer::sum);
        totalCount++;
    }

    @Override
    public void print() {
        System.out.println("ginf-" + totalCount + ", " + id + "-" + COUNTER.get(id));
    }
}
