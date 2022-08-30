package exceptions;

import com.oocourse.spec2.exceptions.PersonIdNotFoundException;

import java.util.HashMap;

public class MyPersonIdNotFoundException extends PersonIdNotFoundException {
    private static HashMap<Integer, Integer> counter = new HashMap<>();
    private static int totalCount = 0;
    private int id;

    public MyPersonIdNotFoundException(int id) {
        this.id = id;
        counter.merge(id, 1, Integer::sum);
        totalCount++;
    }

    @Override
    public void print() {
        System.out.println("pinf-" + totalCount + ", " + id + "-" + counter.get(id));
    }
}
