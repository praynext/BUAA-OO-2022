import java.util.HashMap;

public class UnionFind {
    private HashMap<Integer, Integer> father = new HashMap<>();
    private int blockSum = 0;

    public UnionFind() {
    }

    public int find(int id) {
        return father.get(id) == id ? id : father.merge(id, find(father.get(id)), (a, b) -> b);
    }

    public boolean isSameSet(int id1, int id2) {
        return find(id1) == find(id2);
    }

    public void add(int id) {
        father.put(id, id);
        blockSum++;
    }

    public void merge(int id1, int id2) {
        int i = find(id1);
        int j = find(id2);
        if (i == j) {
            return;
        }
        father.put(i, j);
        blockSum--;
    }

    public int queryBlockSum() {
        return blockSum;
    }
}
