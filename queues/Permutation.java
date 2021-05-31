import edu.princeton.cs.algs4.StdIn;

/**
 * @author jtchen
 * @version 1.0
 * @date 2021/3/20 12:29
 */
public class Permutation {
    /*
    编写一个客户端程序Permutation.java，它使用整数k作为命令行参数。
    使用StdIn.readString（）从标准输入读取字符串序列；
    并精确地均匀地打印其中的k个。 最多打印一次序列中的每个项目。
     */

    // 命令行参数。 您可以假设0≤k≤n，其中n是标准输入上的字符串数。 请注意，您没有得到n。
    public static void main(String[] args) {
        int k = Integer.parseInt(args[0]);
        RandomizedQueue<String> queue = new RandomizedQueue<>();
        for (int i = 0; i < k; i++){
            String s = StdIn.readString();
            queue.enqueue(s);
        }
        queue.forEach(System.out::println);
    }
}
