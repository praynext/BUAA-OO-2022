import java.util.HashMap;

public class UnionFind {
    private final HashMap<Integer, Integer> father = new HashMap<>();
    private final HashMap<Integer, Integer> mst = new HashMap<>();
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
        mst.put(id, 0);
        blockSum++;
    }

    public void merge(int id1, int id2, int value) {
        int i = find(id1);
        int j = find(id2);
        if (i == j) {
            mst.put(i, -1);
            return;
        }
        father.put(i, j);
        if (mst.get(i) != -1 && mst.get(j) != -1) {
            mst.merge(j, mst.get(i) + value, Integer::sum);
        } else {
            mst.put(j, -1);
        }
        blockSum--;
    }

    public int queryBlockSum() {
        return blockSum;
    }

    public int getSize() {
        return father.size();
    }

    public boolean contains(int id) {
        return father.containsKey(id);
    }

    public int queryLeastConnection(int id) {
        return mst.get(find(id));
    }

    public void setLeastConnection(int id, int value) {
        mst.put(find(id), value);
    }
}
