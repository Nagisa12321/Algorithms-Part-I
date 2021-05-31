import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author jtchen
 * @version 1.0
 * @date 2021/5/25 11:14
 */
public class Board {
    /**
     * 用来存储接收到的正方形
     */
    private final int[][] tiles;
    /**
     * 正方形的宽度
     */
    private final int boardSize;

    /**
     * 0 板块的i和j
     */
    private int zeroI;
    private int zeroJ;

    /**
     * 汉明权重
     */
    private int hamming;

    /**
     * 曼哈顿权重
     */
    private int manhattan;


    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    //
    // Constructor.  You may assume that the constructor receives an n-by-n array containing
    // the n2 integers between 0 and n2 − 1, where 0 represents the blank square.
    // You may also assume that 2 ≤ n < 128.
    //
    // 您可以假定构造函数接收一个n×n数组，其中包含0到n2-1之间的n2个整数，其中0表示空白正方形。您还可以假设2≤n <128。
    public Board(int[][] tiles) {
        this.boardSize = tiles.length;
        this.tiles = new int[boardSize][boardSize];
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                int num = tiles[i][j];
                this.tiles[i][j] = num;
                if (num == 0) {
                    zeroI = i;
                    zeroJ = j;
                }
            }
        }

        // hamming
        int num = 1;
        this.hamming = 0;
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                // 不是最后一个方块
                if (!(i == boardSize - 1 && j == boardSize - 1)) {
                    if (tiles[i][j] != num++) {
                        hamming++;
                    }
                }
            }
        }

        // manhattan
        this.manhattan = 0;
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                int goal = tiles[i][j];
                if (goal != 0) {
                    int finalI = (goal - 1) / boardSize;
                    int finalJ = (goal - 1) % boardSize;
                    manhattan += Math.abs(finalI - i) + Math.abs(finalJ - j);
                }
            }
        }
    }

    // string representation of this board
    // The toString() method returns a string composed of n + 1 lines.
    // The first line contains the board size n; the remaining n lines contains the n-by-n grid of tiles in row-major order,
    // using 0 to designate the blank square.
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(boardSize).append('\n');
        for (int i = 0; i < boardSize; i++) {
            builder.append(' ');
            for (int j = 0; j < boardSize; j++) {
                builder.append(tiles[i][j]);
                if (j != boardSize - 1) {
                    builder.append("  ");
                }
            }
            builder.append('\n');
        }
        return builder.toString();
    }

    // board dimension n
    public int dimension() {
        return boardSize;
    }
    /*-----------------------------------------------------------------------------------------------------------*/
    // Hamming and Manhattan distances.                                                                          //
    // To measure how close a board is to the goal board, we define two notions of distance.                     //
    // The Hamming distance betweeen a board and the goal board is the number of tiles in the wrong position.    //
    // The Manhattan distance between a board and the goal board is the sum of the                               //
    // Manhattan distances (sum of the vertical and horizontal distance) from the tiles to their goal positions. //
    //  为了衡量一块板与目标板之间的距离，我们定义了两个距离概念。木板之间的汉明距离与目标板之间的位置错误。                         //
    //  板与目标板之间的曼哈顿距离是从图块到其目标位置的曼哈顿距离之和（垂直和水平距离之和）。                                    //
    /*-----------------------------------------------------------------------------------------------------------*/

    // number of tiles out of place
    public int hamming() {
        return this.hamming;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        return this.manhattan;
    }

    // is this board the goal board?
    public boolean isGoal() {
        int num = 1;
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (i == boardSize - 1 && j == boardSize - 1) break;
                else if (tiles[i][j] != num++) return false;
            }
        }
        return true;
    }

    // Comparing two boards for equality.
    // Two boards are equal if they are have the same size and their corresponding tiles are in the same positions.
    // The equals() method is inherited from java.lang.Object, so it must obey all of Java’s requirements.
    // does this board equal y?
    public boolean equals(Object y) {
        if (y == null) return false;
        else if (this == y) return true;
        // 如果y的类不是Board直接返回false
        if (!(y.getClass() == this.getClass())) return false;
        Board board = (Board) y;
        // deepEquals: 二维数组
        return boardSize == board.boardSize && Arrays.deepEquals(tiles, board.tiles);
    }

    // all neighboring boards
    // Neighboring boards.
    // The neighbors() method returns an iterable containing the neighbors of the board.
    // Depending on the location of the blank square, a board can have 2, 3, or 4 neighbors.
    //
    // 我想我应该记录0的位置, 防止每次都遍历 :)
    public Iterable<Board> neighbors() {
        List<Board> list = new ArrayList<>(4);
        if (zeroI != 0) {
            // i != 0 说明可以和上面的交换
            swap(tiles, zeroI, zeroJ, zeroI - 1, zeroJ);
            list.add(new Board(this.tiles));
            swap(tiles, zeroI, zeroJ, zeroI - 1, zeroJ);
        }
        if (zeroI != boardSize - 1) {
            swap(tiles, zeroI, zeroJ, zeroI + 1, zeroJ);
            list.add(new Board(this.tiles));
            swap(tiles, zeroI, zeroJ, zeroI + 1, zeroJ);
        }
        if (zeroJ != 0) {
            swap(tiles, zeroI, zeroJ, zeroI, zeroJ - 1);
            list.add(new Board(this.tiles));
            swap(tiles, zeroI, zeroJ, zeroI, zeroJ - 1);
        }
        if (zeroJ != boardSize - 1) {
            swap(tiles, zeroI, zeroJ, zeroI, zeroJ + 1);
            list.add(new Board(this.tiles));
            swap(tiles, zeroI, zeroJ, zeroI, zeroJ + 1);
        }
        return list;
    }

    private void swap(int[][] arr, int i1, int j1, int i2, int j2) {
        int tmp = arr[i1][j1];
        arr[i1][j1] = arr[i2][j2];
        arr[i2][j2] = tmp;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (tiles[i][j] != 0) {
                    if (i != 0 && tiles[i - 1][j] != 0) {
                        // i != 0 说明可以和上面的交换
                        swap(tiles, i, j, i - 1, j);
                        Board result = new Board(this.tiles);
                        swap(tiles, i, j, i - 1, j);
                        return result;
                    }
                    else if (i != boardSize - 1 && tiles[i + 1][j] != 0) {
                        swap(tiles, i, j, i + 1, j);
                        Board result = new Board(this.tiles);
                        swap(tiles, i, j, i + 1, j);
                        return result;
                    }
                }
            }
        }
        return null;
    }

    // unit testing (not graded)
    public static void main(String[] args) {
        /*----------test init / toString()---------------*/
        int[][] arr0 = { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 0 } };
        Board board0 = new Board(arr0);
        System.out.println("test init / toString(): \n" + board0);

        /*----------test hamming()---------------*/
        int[][] arr1 = { { 8, 1, 3 }, { 4, 0, 2 }, { 7, 6, 5 } };
        Board board1 = new Board(arr1);
        System.out.println("\ntest hamming(): \n" + board1.hamming());
        int[][] arr2 = { { 1, 4, 2 }, { 8, 5, 7 }, { 3, 0, 6 } };
        Board board2 = new Board(arr2);
        System.out.println(board2.hamming());

        /*----------test manhattan()---------------*/
        int[][] arr3 = { { 8, 1, 3 }, { 4, 0, 2 }, { 7, 6, 5 } };
        Board board3 = new Board(arr3);
        System.out.println("\ntest manhattan(): \n" + board3.manhattan());

        /*-----------test isGoal()--------------*/
        int[][] arr4 = { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 0 } };
        Board board4 = new Board(arr4);
        System.out.println(
                "\ntest isGoal(): \narr[]: " + Arrays.deepToString(arr4) + ", isGoal: " + board4
                        .isGoal());
        int[][] arr5 = { { 1, 4, 2 }, { 8, 5, 7 }, { 3, 0, 6 } };
        Board board5 = new Board(arr5);
        System.out.println("arr[]: " + Arrays.deepToString(arr5) + ", isGoal: " + board5.isGoal());

        /*------------test equals()------------------*/
        int[][] arr6 = { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 0 } };
        int[][] arr7 = { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 0 } };
        Board board6 = new Board(arr6);
        Board board7 = new Board(arr7);
        System.out.println("\ntest equals: \n" + board6.equals(board7));

        /*---------------test neighbors()------------------------*/
        int[][] arr8 = { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 0 } };
        Board board8 = new Board(arr8);
        System.out.println("\ntest neighbors(): \n" + board8.neighbors());
        System.out.println(board8);
    }
}
