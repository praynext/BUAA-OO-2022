package exceptions;

import com.oocourse.spec3.exceptions.MessageIdNotFoundException;

import java.util.HashMap;

public class MyMessageIdNotFoundException extends MessageIdNotFoundException {
    private static final HashMap<Integer, Integer> COUNTER = new HashMap<>();
    private static int totalCount = 0;
    private final int id;

    public MyMessageIdNotFoundException(int id) {
        this.id = id;
        COUNTER.merge(id, 1, Integer::sum);
        totalCount++;
    }

    @Override
    public void print() {
        System.out.println("minf-" + totalCount + ", " + id + "-" + COUNTER.get(id));
    }
}
