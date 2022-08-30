public class Edge implements Comparable<Edge> {
    private int begin;
    private int end;
    private int distance;

    public Edge(int begin, int end, int distance) {
        this.begin = begin;
        this.end = end;
        this.distance = distance;
    }

    public int getBegin() {
        return begin;
    }

    public int getEnd() {
        return end;
    }

    public int getDistance() {
        return distance;
    }

    public int compareTo(Edge edge) {
        return distance - edge.distance;
    }
}
