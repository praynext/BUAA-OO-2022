package exceptions;

import com.oocourse.spec2.exceptions.MessageIdNotFoundException;

import java.util.HashMap;

public class MyMessageIdNotFoundException extends MessageIdNotFoundException {
    private static HashMap<Integer, Integer> counter = new HashMap<>();
    private static int totalCount = 0;
    private int id;

    public MyMessageIdNotFoundException(int id) {
        this.id = id;
        counter.merge(id, 1, Integer::sum);
        totalCount++;
    }

    @Override
    public void print() {
        System.out.println("minf-" + totalCount + ", " + id + "-" + counter.get(id));
    }
}
