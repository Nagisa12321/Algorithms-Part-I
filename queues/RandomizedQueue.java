import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * @author jtchen
 * @version 1.0
 * @date 2021/3/19 19:22
 */

/*
随机队列类似于堆栈或队列，不同的是，除去的项目是在数据结构中的项目之间
随机随机选择的。 创建实现以下API的通用数据类型
 */
public class RandomizedQueue<Item> implements Iterable<Item> {

    private int tail = 0;
    private int head = 0;
    private Item[] items;

    /*
    性能要求。 您的随机队列实现必须在固定的摊销时间内支
    持每个随机队列操作（除了创建迭代器之外）。 也就是说，
    对于某些常数c，在最坏的情况下，m个随机队列操作的任
    何混合序列（从空队列开始）必须最多执行cm步。 包含n个
    项的随机队列最多必须使用48n + 192字节的内存。 另外
    ，您的迭代器实现必须在最坏的情况下始终支持next（）
    和hasNext（）操作。 并在线性时间内进行构造； 您
    可能（并且将需要）为每个迭代器使用线性数量的额外内存
     */
    // construct an empty randomized queue
    public RandomizedQueue() {
        items = (Item[]) new Object[1];
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return tail == head;
    }

    // return the number of items on the randomized queue
    public int size() {
        return head - tail;
    }

    // add the item
    public void enqueue(Item item) {
        // Throw an IllegalArgumentException if the client
        // calls enqueue() with a null argument.
        if (item == null) throw new IllegalArgumentException();
        if (isFull())
            reset();
        items[head] = item;
        head++;

    }

    // remove and return a random item
    public Item dequeue() {
        // Throw a java.util.NoSuchElementException if the
        // client calls either sample() or dequeue() when
        // the randomized queue is empty.
        if (isEmpty()) throw new NoSuchElementException();
        if (isSmall()) reset();

        random(tail, size());
        Item result = items[tail];
        tail++;

        return result;
    }

    private void random(int tail, int len) {
        int random = (int) (StdRandom.uniform() * len);
        swap(random, tail);
    }

    private void swap(int random, int tail) {
        Item tmp = items[tail];
        items[tail] = items[tail + random];
        items[tail + random] = tmp;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (isEmpty()) throw new NoSuchElementException();
        random(tail, size());
        return items[tail];
    }

    private boolean isFull() {
        return head == items.length;
    }

    private boolean isSmall() {
        return size() < items.length / 4;
    }

    private void reset() {
        int size = size();
        // 根据需要增长或者缩小数组
        Item[] tmp = (Item[]) new Object[size == 0 ? 1 : size() * 2];
        System.arraycopy(items, tail, tmp, 0, size());

        tail = 0;
        head = size;
        items = tmp;
    }

    /*
    每个迭代器都必须以统一的随机顺序返回项目。 同一随机队列中两个或多个迭代器的
    顺序必须相互独立。 每个迭代器必须保持自己的随机顺序。
     */
    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator();
    }


    private class RandomizedQueueIterator implements Iterator<Item> {
        private int cur;

        public RandomizedQueueIterator() {
            cur = tail;
        }

        public boolean hasNext() {
            return cur != head;
        }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            random(cur, head - cur);
            Item result = items[cur];

            cur++;
            return result;
        }


        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        RandomizedQueue<Integer> queue = new RandomizedQueue<>();
        queue.enqueue(823);
        queue.enqueue(730);
        queue.enqueue(12312);
        queue.enqueue(123);
        queue.enqueue(732130);

        Iterator<Integer> iterator = queue.iterator();
        iterator.forEachRemaining(System.out::println);
    }
}
