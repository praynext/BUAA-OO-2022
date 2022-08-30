package exceptions;

import com.oocourse.spec3.exceptions.EqualGroupIdException;

import java.util.HashMap;

public class MyEqualGroupIdException extends EqualGroupIdException {
    private static final HashMap<Integer, Integer> COUNTER = new HashMap<>();
    private static int totalCount = 0;
    private final int id;

    public MyEqualGroupIdException(int id) {
        this.id = id;
        COUNTER.merge(id, 1, Integer::sum);
        totalCount++;
    }

    @Override
    public void print() {
        System.out.println("egi-" + totalCount + ", " + id + "-" + COUNTER.get(id));
    }
}
