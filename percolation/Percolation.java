import edu.princeton.cs.algs4.WeightedQuickUnionUF;

/************************************************
 *
 * @author jtchen
 * @date 2020/12/23 22:01
 * @version 1.0
 ************************************************/
public class Percolation {
    private final WeightedQuickUnionUF uf;
    private final WeightedQuickUnionUF ufCheck;
    private final int n;
    private final boolean[] isOpen;
    private int nums;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) throw new IllegalArgumentException();
        this.nums = 0;
        this.n = n;
        //初始化: 创建n*n+2个节点
        this.uf = new WeightedQuickUnionUF(n * n + 2);
        this.ufCheck = new WeightedQuickUnionUF(n * n + 1);
        this.isOpen = new boolean[n * n];
        /*
        for (int i = 0; i < n; i++) {
            uf.union(0, getIndex(1, i + 1));
            uf.union(n * n + 1, getIndex(n, i + 1));
        }

         */
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        if (row < 1 || row > n || col < 1 || col > n)
            throw new IllegalArgumentException();
        else if (isOpen(row, col)) return;
            //若是最后一行或者第一行, 则要相应和虚节点链接
        if (row == 1) {
            uf.union(0, getIndex(row, col));
            ufCheck.union(0, getIndex(row, col));
        }
        if (row == n)
            uf.union(n * n + 1, getIndex(row, col));
        //加入上下左右相应集合, 如果上下左右相应存在的话
        if (col != 1 && isOpen(row, col - 1)) {
            uf.union(getIndex(row, col), getIndex(row, col - 1));
            ufCheck.union(getIndex(row, col), getIndex(row, col - 1));
        }
        if (col != n && isOpen(row, col + 1)) {
            uf.union(getIndex(row, col), getIndex(row, col + 1));
            ufCheck.union(getIndex(row, col), getIndex(row, col + 1));
        }
        if (row != 1 && isOpen(row - 1, col)) {
            uf.union(getIndex(row, col), getIndex(row - 1, col));
            ufCheck.union(getIndex(row, col), getIndex(row - 1, col));
        }
        if (row != n && isOpen(row + 1, col)) {
            uf.union(getIndex(row, col), getIndex(row + 1, col));
            ufCheck.union(getIndex(row, col), getIndex(row + 1, col));
        }
        //打开位
        isOpen[getIndex(row, col) - 1] = true;
        nums++;
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (row < 1 || row > n || col < 1 || col > n)
            throw new IllegalArgumentException();
        //因为是n * n数组, 因此减一
        return isOpen[getIndex(row, col) - 1];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        if (row < 1 || row > n || col < 1 || col > n)
            throw new IllegalArgumentException();
        if (!isOpen(row, col)) return false;
        else return ufCheck.find(0) == ufCheck.find(getIndex(row, col));
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return nums;
    }

    // does the system percolate?
    public boolean percolates() {
        return uf.find(0) == uf.find(n * n + 1);
    }

    //通过行、列返回在数组中的下标
    private int getIndex(int row, int col) {
        return (row - 1) * n + col;
    }

    // test client (optional)
    public static void main(String[] args) {
        Percolation percolation = new Percolation(1);
        System.out.println(percolation.percolates());
        percolation.open(1, 1);
        System.out.println(percolation.percolates());

    }
}