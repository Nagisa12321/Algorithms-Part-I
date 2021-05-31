import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation requirement.  To implement the A* algorithm, you must use the MinPQ data type for
 * the priority queue.
 * <p>
 * Corner cases.
 * <p>
 * - Throw an IllegalArgumentException in the constructor if the argument is null.
 * - Return -1 in moves() if the board is unsolvable.
 * - Return null in solution() if the board is unsolvable.
 *
 * @author jtchen
 * @version 1.0
 * @date 2021/5/25 13:56
 */
public class Solver {

    /**
     * 直接在构造函数中进行
     */
    private Stack<Board> stack;

    /**
     * 步骤
     */
    private int moves;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) throw new IllegalArgumentException();
        Board twinBoard = initial.twin();
        /**
         * the open set for A*.
         */
        // f(n)=g(n)+h(n): move 就是 g(n), Priority就是h(n)
        MinPQ<GameTreeNode> minPQ = new MinPQ<>(
                (node1, node2) -> node1.getPriority() - node2.getPriority()
        );
        MinPQ<GameTreeNode> minPQHelper = new MinPQ<>(
                (node1, node2) -> node1.getPriority() - node2.getPriority()
        );

        minPQ.insert(new GameTreeNode(null, initial, 0));
        minPQHelper.insert(new GameTreeNode(null, twinBoard, 0));

        int boardSize = initial.dimension();
        int[][] titles = new int[boardSize][boardSize];
        int num = 1;
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                titles[i][j] = num++;
            }
        }
        titles[boardSize - 1][boardSize - 1] = 0;
        /**
         * 目标
         */
        Board goal = new Board(titles);

        while (true) {
            GameTreeNode cur = minPQ.delMin();
            GameTreeNode curHelper = minPQHelper.delMin();

            if (cur.getBoard().equals(goal)) {
                stack = new Stack<>();
                while (cur != null) {
                    stack.push(cur.getBoard());
                    cur = cur.getParent();
                }
                moves = stack.size() - 1;
                break;
            }
            else if (curHelper.getBoard().equals(goal)) {
                moves = -1;
                stack = null;
                break;
            }

            for (Board neighbor : cur.neighbors()) {
                GameTreeNode tmpNode = new GameTreeNode(cur, neighbor, cur.getMove() + 1);
                minPQ.insert(tmpNode);
            }

            for (Board neighbor : curHelper.neighbors()) {
                GameTreeNode tmpNode = new GameTreeNode(curHelper, neighbor, curHelper.getMove() + 1);
                minPQHelper.insert(tmpNode);
            }
        }
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return solution() != null;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        return moves;
    }

    // The critical optimization.
    //
    // 1. A* search has one annoying feature:
    // search nodes corresponding to the same board are enqueued on the priority queue many times
    // (e.g., the bottom-left search node in the game-tree diagram above). To reduce unnecessary
    // exploration of useless search nodes, when considering the neighbors of a search node,
    // don’t enqueue a neighbor if its board is the same as the board of the previous search node
    // in the game tree.
    // A *搜索具有一个烦人的功能：与同一块板相对应的搜索节点会多次排入优先级队列（例如，上面游戏树图中的左下搜索节点）。
    // 为减少对无用搜索节点的不必要探索，在考虑搜索节点的邻居时，如果邻居的棋盘与游戏树中前一个搜索节点的棋盘相同，
    // 则不要将其排入队列。
    //
    // 2. Caching the Hamming and Manhattan priorities.
    // To avoid recomputing the Manhattan priority of a search node
    // from scratch each time during various priority queue operations,
    // pre-compute its value when you construct the search node; save it
    // in an instance variable; and return the saved value as needed. This
    // caching technique is broadly applicable: consider using it in any s
    // ituation where you are recomputing the same quantity many times and
    // for which computing that quantity is a bottleneck operation.
    // 为了避免在各种优先级队列操作期间每次从头开始重新计算搜索节点的曼哈顿优先级，在构造搜索节点时应预先计算其值；
    // 将其保存在实例变量中；
    // 并根据需要返回保存的值。
    // 此缓存技术广泛适用：在多次重新计算相同数量且计算该数量成为瓶颈操作的任何情况下，请考虑使用该缓存技术。
    //
    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        return stack;
    }

    // test client (see below)
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }


    private static class GameTreeNode {

        private GameTreeNode parent;
        private Board board;
        private int priority;
        private int move;

        public GameTreeNode(GameTreeNode parent, Board board, int move) {
            this.parent = parent;
            this.board = board;
            this.priority = board.manhattan() + move;
            this.move = move;
        }

        public GameTreeNode getParent() {
            return parent;
        }


        public Board getBoard() {
            return board;
        }


        public int getPriority() {
            return priority;
        }

        public int getMove() {
            return move;
        }

        public Iterable<Board> neighbors() {
            List<Board> res = new ArrayList<>();
            for (Board b : this.board.neighbors()) {
                if (move == 0 || !parent.board.equals(b)) {
                    res.add(b);
                }
            }
            return res;
        }
    }
}