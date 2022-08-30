package test;

import com.oocourse.elevator1.PersonRequest;

import java.util.HashSet;

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
        notifyAll();
    }

    public synchronized void setEnd(boolean isEnd) {
        this.isEnd = isEnd;
        notifyAll();
    }

    public synchronized boolean isEnd() {
        notifyAll();
        return isEnd;
    }

    public synchronized boolean isEmpty() {
        notifyAll();
        return requests.isEmpty();
    }

    public synchronized HashSet<PersonRequest> getRequests() {
        notifyAll();
        return requests;
    }
}
