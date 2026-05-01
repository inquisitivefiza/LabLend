import java.util.LinkedList;

<<<<<<< HEAD
public class WaitlistQueue<S extends User> {

    private final LinkedList<S> queue = new LinkedList<>();

    public void enqueue(S user) {
        queue.addLast(user);
    }

    public S dequeue() {
=======
// DSA Concept: Queue (FIFO) — used for waitlist management
public class WaitlistQueue<T> {
    private LinkedList<T> queue = new LinkedList<>();

    /** Add to the back of the queue — O(1) */
    public void enqueue(T item) {
        queue.addLast(item);
    }

    /** Remove from the front of the queue — O(1) */
    public T dequeue() {
>>>>>>> cbffb5969401e251ff0d4969af56aa990afe4abb
        if (isEmpty())
            throw new RuntimeException("Queue is empty");
        return queue.removeFirst();
    }

<<<<<<< HEAD
    public S peek() {
        if (isEmpty())
            throw new RuntimeException("Queue is empty");
        return queue.getFirst();
    }

    public int size() {
        return queue.size();
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }













}
=======
    /** Peek at the front without removing — O(1) */
    public T peek() {
        if (isEmpty())
            throw new RuntimeException("Queue is empty");
        return queue.peekFirst();
    }

    public boolean isEmpty() { return queue.isEmpty(); }
    public int size()        { return queue.size(); }
}
>>>>>>> cbffb5969401e251ff0d4969af56aa990afe4abb
