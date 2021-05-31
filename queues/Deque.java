import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * @author jtchen
 * @version 1.0
 * @date 2021/3/19 13:33
 */
public class Deque<Item> implements Iterable<Item> {

    private Node head = null;
    private Node tail = null;
    private int size = 0;

    // construct an empty deque
    public Deque() {

    }

    // unit testing (required)
    public static void main(String[] args) {
        Deque<Integer> deque = new Deque<>();
        deque.addLast(1);
        // ==> [1]
        for (Integer value : deque) System.out.println(value);

        deque.addFirst(2);
        // ==> [1, 2]
        for (Integer integer : deque) System.out.println(integer);
    }

    // return the number of items on the deque
    public int size() {
        return size;
    }

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null) throw new IllegalArgumentException();
        if (head == null) {
            head = new Node(item);
            tail = head;
        }
        else {
            Node node = new Node(item);
            head.next = node;
            node.prev = head;
            head = head.next;
        }
        size++;
    }

    // add the item to the back
    public void addLast(Item item) {
        if (item == null) throw new IllegalArgumentException();
        if (tail == null) {
            head = new Node(item);
            tail = head;
        }
        else {
            Node node = new Node(item);
            tail.prev = node;
            node.next = tail;
            tail = tail.prev;
        }
        size++;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (isEmpty()) throw new NoSuchElementException();

        Item item = head.val;
        if (size == 1) {
            head = null;
            tail = null;
        }
        else {
            head = head.prev;
            head.next = null;
        }
        size--;
        return item;
    }

    // is the deque empty?
    public boolean isEmpty() {
        return head == null;
    }

    // remove and return the item from the back
    public Item removeLast() {
        if (isEmpty()) throw new NoSuchElementException();

        Item item = tail.val;
        if (size == 1) {
            head = null;
            tail = null;
        }
        else {
            tail = tail.next;
            tail.prev = null;
        }
        size--;
        return item;
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    private class DequeIterator implements Iterator<Item> {

        private Node cur;

        public DequeIterator() {
            cur = head;
        }

        @Override
        public boolean hasNext() {
            return cur != null;
        }

        @Override
        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            Item item = cur.val;
            cur = cur.prev;
            return item;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    private class Node {
        private Node next;
        private Node prev;
        private Item val;

        public Node(Item val) {
            this.val = val;
        }
    }

}
