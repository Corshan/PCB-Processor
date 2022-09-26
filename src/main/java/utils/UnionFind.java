package utils;

public class UnionFind {

    public static int find(int[] a, int id) {
        while(a[id]!=id) {
            a[id]=a[a[id]]; //Compress path
            id=a[id];
        }
        return id;
    }

    public static void union(int[] a, int p, int q) {
        a[find(a, q)] = find(a, p);
    }
}
