import java.util.LinkedList;

public class WaitlistQueue<S extends User> {

    private final LinkedList<S> queue = new LinkedList<>();

    public void enqueue(S user) {
        queue.addLast(user);
    }

    public S dequeue() {
        if (isEmpty())
            throw new RuntimeException("Queue is empty");
        return queue.removeFirst();
    }

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