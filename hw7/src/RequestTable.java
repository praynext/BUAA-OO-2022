import java.util.HashSet;

public class RequestTable {
    private final HashSet<MyPersonRequest> requests;
    private boolean isEnd;

    public RequestTable() {
        requests = new HashSet<>();
        this.isEnd = false;
    }

    public synchronized void add(MyPersonRequest request) {
        requests.add(request);
        notifyAll();
    }

    public synchronized void remove(MyPersonRequest request) {
        requests.remove(request);
    }

    public synchronized void setEnd(boolean isEnd) {
        this.isEnd = isEnd;
        notifyAll();
    }

    public synchronized boolean isEnd() {
        return isEnd;
    }

    public synchronized boolean isEmpty() {
        return requests.isEmpty();
    }

    public synchronized HashSet<MyPersonRequest> getRequests() {
        return requests;
    }
}
