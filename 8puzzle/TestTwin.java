import edu.princeton.cs.algs4.In;

/**
 * @author jtchen
 * @version 1.0
 * @date 2021/5/26 13:01
 */
public class TestTwin {

    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);
        System.out.println(initial);
        System.out.println(initial.twin());
    }

}
