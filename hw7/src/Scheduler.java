public class Scheduler extends Thread {
    private static final Scheduler SCHEDULER = new Scheduler();

    public static Scheduler getInstance() {
        return SCHEDULER;
    }

    @Override
    public void run() {
        //currentThread().setName("Scheduler");
        while (true) {
            synchronized (WaitQueue.getInstance()) {
                if (WaitQueue.getInstance().isEmpty()) {
                    if (WaitQueue.getInstance().isEnd()) {
                        TotalBuildings.getInstance().setEnd(true);
                        TotalFloors.getInstance().setEnd(true);
                        return;
                    }
                    try {
                        WaitQueue.getInstance().wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                for (MyPersonRequest request : WaitQueue.getInstance().getRequests()) {
                    if (request.isTransferred()) {
                        if (request.getPresentFloor() != request.getToFloor()) {
                            request.setNextFloor(request.getToFloor());
                            TotalBuildings.getInstance().getBuildings().
                                    get(request.getToBuilding() - 'A').
                                    getRequestTable().add(request);
                        } else {
                            WaitQueue.getInstance().decrease();
                        }
                    } else {
                        if (request.getFromBuilding() == request.getToBuilding()) {
                            request.setTransferred(true);
                            request.setNextFloor(request.getToFloor());
                            request.setNextBuilding(request.getToBuilding());
                            TotalBuildings.getInstance().getBuildings().
                                    get(request.getToBuilding() - 'A').
                                    getRequestTable().add(request);
                        } else {
                            int transferFloor = TotalFloors.getInstance().search(request);
                            request.setNextFloor(transferFloor);
                            if (transferFloor == request.getPresentFloor()) {
                                request.setNextBuilding(request.getToBuilding());
                                TotalFloors.getInstance().getFloors().
                                        get(transferFloor - 1).getRequestTable().add(request);
                            } else {
                                request.setNextBuilding(request.getFromBuilding());
                                TotalBuildings.getInstance().getBuildings().
                                        get(request.getFromBuilding() - 'A').
                                        getRequestTable().add(request);
                            }
                        }
                    }
                }
                WaitQueue.getInstance().getRequests().clear();
            }
        }
    }
}
