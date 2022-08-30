package exceptions;

import com.oocourse.spec2.exceptions.EqualMessageIdException;

import java.util.HashMap;

public class MyEqualMessageIdException extends EqualMessageIdException {
    private static HashMap<Integer, Integer> counter = new HashMap<>();
    private static int totalCount = 0;
    private int id;

    public MyEqualMessageIdException(int id) {
        this.id = id;
        counter.merge(id, 1, Integer::sum);
        totalCount++;
    }

    @Override
    public void print() {
        System.out.println("emi-" + totalCount + ", " + id + "-" + counter.get(id));
    }
}