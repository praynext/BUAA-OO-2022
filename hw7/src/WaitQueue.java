import java.util.HashSet;

public class WaitQueue {
    private static final WaitQueue WAIT_QUEUE = new WaitQueue();
    private final HashSet<MyPersonRequest> requests = new HashSet<>();
    private boolean isEnd = false;
    private int count = 0;

    public static WaitQueue getInstance() {
        return WAIT_QUEUE;
    }

    public synchronized void add(MyPersonRequest request) {
        requests.add(request);
        notifyAll();
    }

    public synchronized void increase() {
        count += 1;
    }

    public synchronized void decrease() {
        count -= 1;
        if (count == 0) {
            notifyAll();
        }
    }

    public synchronized HashSet<MyPersonRequest> getRequests() {
        return requests;
    }

    public synchronized void setEnd(boolean isEnd) {
        this.isEnd = isEnd;
        notifyAll();
    }

    public synchronized boolean isEnd() {
        return isEnd && (count == 0);
    }

    public synchronized boolean isEmpty() {
        return requests.isEmpty();
    }
}
