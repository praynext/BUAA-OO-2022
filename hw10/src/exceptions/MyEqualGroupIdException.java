package exceptions;

import com.oocourse.spec2.exceptions.EqualGroupIdException;

import java.util.HashMap;

public class MyEqualGroupIdException extends EqualGroupIdException {
    private static HashMap<Integer, Integer> counter = new HashMap<>();
    private static int totalCount = 0;
    private int id;

    public MyEqualGroupIdException(int id) {
        this.id = id;
        counter.merge(id, 1, Integer::sum);
        totalCount++;
    }

    @Override
    public void print() {
        System.out.println("egi-" + totalCount + ", " + id + "-" + counter.get(id));
    }
}
