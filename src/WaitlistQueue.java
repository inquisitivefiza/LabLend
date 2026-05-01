import java.util.LinkedList;

// DSA Concept: Queue (FIFO) — used for waitlist management
public class WaitlistQueue<T> {
    private LinkedList<T> queue = new LinkedList<>();

    /** Add to the back of the queue — O(1) */
    public void enqueue(T item) {
        queue.addLast(item);
    }

    /** Remove from the front of the queue — O(1) */
    public T dequeue() {
        if (isEmpty())
            throw new RuntimeException("Queue is empty");
        return queue.removeFirst();
    }

    /** Peek at the front without removing — O(1) */
    public T peek() {
        if (isEmpty())
            throw new RuntimeException("Queue is empty");
        return queue.peekFirst();
    }

    public boolean isEmpty() { return queue.isEmpty(); }
    public int size()        { return queue.size(); }
}
