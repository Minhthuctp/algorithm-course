import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import java.util.Iterator;
import java.util.NoSuchElementException;
public class RandomizedQueue<Item> implements Iterable<Item> {

    private Item[] arr;
    private int size;

    // construct an empty randomized queue
    public RandomizedQueue() {
        arr = (Item[]) new Object[1];
        size = 0;
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return (size == 0);
    }

    // return the number of items on the randomized queue
    public int size()
    {
        return size;
    }

    private void reSize(int capacity)
    {
        Item[] temp = (Item[]) new Object[capacity];
        for (int i = 0; i < size; ++i) {
            temp[i] = arr[i];
        }
        arr =temp;
    }

    // add the item
    public void enqueue(Item item)
    {
        if (item == null)
            throw new IllegalArgumentException();

        if (size == arr.length) {
            reSize(2 * arr.length);
        }
        arr[size] = item;
        size++;

    }

    // remove and return a random item
    public Item dequeue()
    {
        if (isEmpty()) throw new NoSuchElementException();


        int id = StdRandom.uniform(size);

        Item item = arr[id];
        size--;
        arr[id] = arr[size];
        arr[size] = null;

        if (size > 0 && size == arr.length / 4) reSize(arr.length / 2);
        return item;
    }

    // return a random item (but do not remove it)
    public Item sample()
    {
        if (isEmpty())
            throw new NoSuchElementException();
        int id = StdRandom.uniform(size);
        return arr[id];
    }

    private class RandomizedQueueIterator implements Iterator<Item> {
        private Item[] arr_copy;
        private int size_copy;

        public RandomizedQueueIterator() {
            size_copy = size;
            arr_copy = (Item[]) new Object[size];
            for (int i = 0; i < size_copy; ++i) {
                arr_copy[i] = arr[i];
            }
        }

        public boolean hasNext()
        {
            return size_copy>0;
        }

        public void remove()
        {
            throw new UnsupportedOperationException();
        }

        public Item next()
        {
            if (!hasNext())
                throw new NoSuchElementException();

            int id = StdRandom.uniform(size_copy);

            Item item = arr_copy[id];
            size_copy--;
            arr_copy[id] = arr_copy[size_copy];
            arr_copy[size_copy] = null;

            return item;
        }
    }
    // return an independent iterator over items in random order
    public Iterator<Item> iterator()
    {
        return new RandomizedQueueIterator();
    }

    // unit testing (required)
    public static void main(String[] args)
    {
        RandomizedQueue<Integer> queue = new RandomizedQueue<>();

        StdOut.println(queue.isEmpty());
        queue.enqueue(1);
        queue.enqueue(2);
        queue.enqueue(3);
        queue.enqueue(4);
        queue.enqueue(5);
        queue.enqueue(6);
        StdOut.println("===== Test size() =====");
        StdOut.println(queue.size());
        StdOut.println("===== Test sample() =====");
        StdOut.println(queue.sample());
        StdOut.println("===== Test iterator() =====");
        for (int num : queue) {
            StdOut.println(num);
        }
        StdOut.println("===== Test dequeue() =====");
        StdOut.println(queue.dequeue());
        StdOut.println(queue.dequeue());

        for (int num : queue) {
            StdOut.println(num);
        }

        StdOut.println(queue.dequeue());
        StdOut.println(queue.dequeue());
        StdOut.println(queue.dequeue());
        StdOut.println(queue.dequeue());
        // TODO: remove till the end
    }

}