package exceptions;

import com.oocourse.spec3.exceptions.EqualPersonIdException;

import java.util.HashMap;

public class MyEqualPersonIdException extends EqualPersonIdException {
    private static final HashMap<Integer, Integer> COUNTER = new HashMap<>();
    private static int totalCount = 0;
    private final int id;

    public MyEqualPersonIdException(int id) {
        this.id = id;
        COUNTER.merge(id, 1, Integer::sum);
        totalCount++;
    }

    @Override
    public void print() {
        System.out.println("epi-" + totalCount + ", " + id + "-" + COUNTER.get(id));
    }
}
