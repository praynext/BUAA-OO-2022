package exceptions;

import com.oocourse.spec1.exceptions.EqualPersonIdException;

import java.util.HashMap;

public class MyEqualPersonIdException extends EqualPersonIdException {
    private static HashMap<Integer, Integer> counter = new HashMap<>();
    private static int totalCount = 0;
    private int id;

    public MyEqualPersonIdException(int id) {
        this.id = id;
        counter.merge(id, 1, Integer::sum);
        totalCount++;
    }

    @Override
    public void print() {
        System.out.println("epi-" + totalCount + ", " + id + "-" + counter.get(id));
    }
}
