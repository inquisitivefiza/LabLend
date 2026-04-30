// DSA Concept: Queue (FIFO) using Linked List  —  manages equipment waitlist
public class WaitlistQueue<T> {
    private Node<T> front;
    private Node<T> rear;
    private int size;

    private static class Node<T> {
        T data;
        Node<T> next;
        Node(T data) {
            this.data = data;
            this.next = null;
        }
    }

    /** Add student/booking to end of waitlist — O(1) */
    public void enqueue(T item) {
        Node<T> newNode = new Node<>(item);
        if (rear != null)
            rear.next = newNode;
        rear = newNode;
        if (front == null)
            front = rear;
        size++;
    }

    /** Remove next student/booking from front — O(1) */
    public T dequeue() {
        if (isEmpty())
            throw new RuntimeException("Queue Empty");
        T data = front.data;
        front = front.next;
        if (front == null)
            rear = null;
        size--;
        return data;
    }

    /** Peek at front without removing — O(1) */
    public T peek() {
        if (isEmpty())
            throw new RuntimeException("Queue Empty");
        return front.data;
    }

    public boolean isEmpty() { return front == null; }
    public int size()        { return size; }
}
