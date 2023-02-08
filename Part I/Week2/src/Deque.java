import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
    private class Node
    {
        private Item item;
        private Node pre;
        private Node next;
    }

    private Node head;
    private Node tail;
    private int size;
    // construct an empty deque
    public Deque()
    {
        head = null;
        tail = null;
        size = 0;
    }
    // is the deque empty?
    public boolean isEmpty()
    {
        return (head == null);
    }

    // return the number of items on the deque
    public int size()
    {
        return size;
    }

    // add the item to the front
    public void addFirst(Item item)
    {
        if (item == null)
            throw new IllegalArgumentException();
        Node temp = new Node();
        temp.item = item;
        temp.next = head;
        temp.pre = null;
        if (head == null)
        {
            head = tail = temp;
        }
        else
        {
            head.pre = temp;
            head = temp;
        }
        size++;
    }

    // add the item to the back
    public void addLast(Item item)
    {
        if (item == null)
            throw new IllegalArgumentException();
        Node temp = new Node();
        temp.item = item;
        temp.next = null;
        temp.pre = tail;
        if (tail == null)
        {
            head = tail = temp;
        }
        else
        {
            tail.next = temp;
            tail = temp;
        }
        size++;
    }
    // remove and return the item from the front
    public Item removeFirst()
    {
        if (head == null)
            throw new NoSuchElementException();
        Item res = head.item;
        head = head.next;
        size--;
        if (head == null)
        {
            head = tail = null;
        }
        else
        {
            head.pre = null;
        }
        return res;
    }

    // remove and return the item from the back
    public Item removeLast()
    {
        if (tail == null)
            throw new NoSuchElementException();
        Item res = tail.item;
        tail = tail.pre;
        size--;
        if (tail == null)
        {
            head = tail = null;
        }
        else
        {
            tail.next = null;
        }
        return res;
    }

    // return an iterator over items in order from front to back
    private class DequeIterator implements Iterator<Item> {
        private Node current = head;

        public boolean hasNext()
        {
            return current != null;
        }

        public void remove()
        {
            throw new UnsupportedOperationException();
        }

        public Item next()
        {
            if (!hasNext())
                throw new NoSuchElementException();

            Item item = current.item;
            current = current.next;
            return item;
        }
    }

    public Iterator<Item> iterator()
    {
        return new DequeIterator();
    }
    // unit testing (required)
    public static void main(String[] args)
    {
        Deque<String> deque = new Deque<>();
        StdOut.println(deque.isEmpty());
        deque.addFirst("is");
        deque.addFirst("this");
        deque.addLast("awesome!");
        StdOut.println(deque.size());

        for (String str : deque) {
            StdOut.println(str);
        }
        StdOut.println(deque.removeFirst());
        StdOut.println(deque.removeFirst());
        StdOut.println(deque.removeLast());
        deque.addLast("cool!");
        StdOut.println(deque.removeFirst());
    }
}
