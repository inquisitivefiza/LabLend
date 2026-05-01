import java.util.LinkedList;

public class WaitlistQueue<S extends User> {

    private final LinkedList<S> queue = new LinkedList<>();

    public void enqueue(S item) {
        queue.addLast(item);
    }

    public S dequeue() {
        if (isEmpty())
            throw new RuntimeException("Queue is empty");
        return queue.removeFirst();
    }

    public S peek() {
        if (isEmpty())
            throw new RuntimeException("Queue is empty");
        return queue.peekFirst();
    }

    public boolean isEmpty() { return queue.isEmpty(); }
    public int size()        { return queue.size(); }
}