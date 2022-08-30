package exceptions;

import com.oocourse.spec3.exceptions.EmojiIdNotFoundException;

import java.util.HashMap;

public class MyEmojiIdNotFoundException extends EmojiIdNotFoundException {
    private static final HashMap<Integer, Integer> COUNTER = new HashMap<>();
    private static int totalCount = 0;
    private final int id;

    public MyEmojiIdNotFoundException(int id) {
        this.id = id;
        COUNTER.merge(id, 1, Integer::sum);
        totalCount++;
    }

    @Override
    public void print() {
        System.out.println("einf-" + totalCount + ", " + id + "-" + COUNTER.get(id));
    }

}