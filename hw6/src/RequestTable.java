import java.util.HashSet;

import com.oocourse.elevator2.PersonRequest;

public class RequestTable {
    private final HashSet<PersonRequest> requests;
    private boolean isEnd;

    public RequestTable() {
        requests = new HashSet<>(100);
        this.isEnd = false;
    }

    public synchronized void add(PersonRequest request) {
        requests.add(request);
        notifyAll();
    }

    public synchronized void remove(PersonRequest request) {
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

    public synchronized HashSet<PersonRequest> getRequests() {
        return requests;
    }
}
