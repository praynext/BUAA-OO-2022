package exceptions;

import com.oocourse.spec3.exceptions.EqualRelationException;

import java.util.HashMap;

public class MyEqualRelationException extends EqualRelationException {
    private static final HashMap<Integer, Integer> COUNTER = new HashMap<>();
    private static int totalCount = 0;
    private final int id1;
    private final int id2;

    public MyEqualRelationException(int id1, int id2) {
        this.id1 = Math.min(id1, id2);
        this.id2 = Math.max(id1, id2);
        COUNTER.merge(id1, 1, Integer::sum);
        if (id2 != id1) {
            COUNTER.merge(id2, 1, Integer::sum);
        }
        totalCount++;
    }

    @Override
    public void print() {
        System.out.println("er-" + totalCount +
                ", " + id1 + "-" + COUNTER.get(id1) +
                ", " + id2 + "-" + COUNTER.get(id2));
    }
}
