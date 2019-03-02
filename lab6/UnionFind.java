public class UnionFind {

    // instance variables
    private int[] parent;

    /* Creates a UnionFind data structure holding n vertices. Initially, all
       vertices are in disjoint sets. */
    public UnionFind(int n) {
        parent = new int[n];
        // all element are disconnected at the beginning
        for (int i = 0; i < n; i++) {
            parent[i] = -1;
        }
    }

    /* Throws an exception if v1 is not a valid index. */
    private void validate(int vertex) {
        if (vertex < 0 || vertex >= parent.length){
            throw new IllegalArgumentException("Invalid index");
        }
    }

    /* Returns the size of the set v1 belongs to. */
    public int sizeOf(int v1) {
        validate(v1);
        return -1 * parent[find(v1)]; // find returns root index, which stores size
    }

    /* Returns the parent of v1. If v1 is the root of a tree, returns the
       negative size of the tree for which v1 is the root. */
    public int parent(int v1) {
        validate(v1);
        return parent[v1]; // returns the vertex's parent
    }

    /* Returns true if nodes v1 and v2 are connected. */
    public boolean connected(int v1, int v2) {
        validate(v1);
        validate(v2);
        return find(v1) == find(v2); // check if the share root
    }

    /* Connects two elements v1 and v2 together. v1 and v2 can be any valid 
       elements, and a union-by-size heuristic is used. If the sizes of the sets
       are equal, tie break by connecting v1's root to v2's root. Unioning a 
       vertex with itself or vertices that are already connected should not 
       change the sets but may alter the internal structure of the data. */
    public void union(int v1, int v2) {
        validate(v1);
        validate(v2);
        // find corresponding root of each set
        int r1 = find(v1);
        int r2 = find(v2);
        int size1 = sizeOf(r1);
        int size2 = sizeOf(r2);
        // if vertices not in same set, then join smaller tree to larger
        if (r1 != r2) {
            int new_size = - (size1 + size2);
            if (size1 <= size2) {
                parent[r1] = r2; // join r1's set to r2's set
                parent[r2] = new_size; // increase size of new larger tree
            } else {
                parent[r2] = r1; // join r2's set to r1's set
                parent[r1] = new_size; // increase size of new larger tree
            }
        }
    }

    /* Returns the root of the set V belongs to. Path-compression is employed
       allowing for fast search-time. */
    public int find(int vertex) {
        validate(vertex);
        int r = vertex;
        while (parent[r] >= 0) {
            r = parent[r];
        }
        return r;
    }

}
