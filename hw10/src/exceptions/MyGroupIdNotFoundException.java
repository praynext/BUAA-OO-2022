package exceptions;

import com.oocourse.spec2.exceptions.GroupIdNotFoundException;

import java.util.HashMap;

public class MyGroupIdNotFoundException extends GroupIdNotFoundException {
    private static HashMap<Integer, Integer> counter = new HashMap<>();
    private static int totalCount = 0;
    private int id;

    public MyGroupIdNotFoundException(int id) {
        this.id = id;
        counter.merge(id, 1, Integer::sum);
        totalCount++;
    }

    @Override
    public void print() {
        System.out.println("ginf-" + totalCount + ", " + id + "-" + counter.get(id));
    }
}
