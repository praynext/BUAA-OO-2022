package exceptions;

import com.oocourse.spec1.exceptions.RelationNotFoundException;

import java.util.HashMap;

public class MyRelationNotFoundException extends RelationNotFoundException {
    private static HashMap<Integer, Integer> counter = new HashMap<>();
    private static int totalCount = 0;
    private int id1;
    private int id2;

    public MyRelationNotFoundException(int id1, int id2) {
        this.id1 = Math.min(id1, id2);
        this.id2 = Math.max(id1, id2);
        counter.merge(id1, 1, Integer::sum);
        if (id2 != id1) {
            counter.merge(id2, 1, Integer::sum);
        }
        totalCount++;
    }

    @Override
    public void print() {
        System.out.println("rnf-" + totalCount +
                ", " + id1 + "-" + counter.get(id1) +
                ", " + id2 + "-" + counter.get(id2));
    }
}
