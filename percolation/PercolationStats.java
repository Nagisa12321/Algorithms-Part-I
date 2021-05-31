import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

/************************************************
 *
 * @author jtchen
 * @date 2020/12/23 22:04
 * @version 1.0
 ************************************************/
public class PercolationStats {
    private final int[] times;
    private final int n;
    private final int trials;

    // perform independent trials on an n-by-n grid
    // 在n到n的网格上执行独立测试
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) throw new IllegalArgumentException();
        this.n = n;
        this.trials = trials;
        this.times = new int[trials];
        for (int i = 0; i < trials; i++) {
            Percolation p = new Percolation(n);
            while (!p.percolates()) {
                // StdRandom.uniform --> [a, b)
                int row = StdRandom.uniform(1, n + 1);
                int col = StdRandom.uniform(1, n + 1);
                if (!p.isOpen(row, col)) {
                    p.open(row, col);
                    times[i]++;
                }
            }
        }
    }

    // sample mean of percolation threshold
    // 渗滤阈值的样本均值
    public double mean() {
        double sum = 0;
        for (int i = 0; i < trials; i++) {
            double tmp = (double) times[i] / (n * n);
            sum += tmp;
        }
        sum /= trials;
        return sum;
    }

    // sample standard deviation of percolation threshold
    // 渗滤阈值的标准偏差
    public double stddev() {
        double[] samples = new double[trials];
        for (int i = 0; i < trials; i++)
            samples[i] = (double) times[i] / (n * n);
        return /*Math.sqrt(StdStats.stddev(samples) / (double) (times.length - 1));*/
            StdStats.stddev(samples);
    }

    // low endpoint of 95% confidence interval
    // 95％置信区间的低端点
    public double confidenceLo() {
        return mean() - (1.96 * stddev() / Math.sqrt(trials));
    }

    // high endpoint of 95% confidence interval
    // 95％置信区间的高终点
    public double confidenceHi() {
        return mean() + (1.96 * stddev() / Math.sqrt(trials));
    }

    // test client (see below)
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);
        PercolationStats p = new PercolationStats(n, trials);
        System.out.println("mean = " + p.mean());
        System.out.println("stddev = " + p.stddev());
        System.out.println("95% confidence interval = [" + p.confidenceLo() + ", " + p
                .confidenceHi() + "]");
    }
}