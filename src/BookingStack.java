// DSA Concept: Stack (LIFO)  —  tracks booking history, supports undo
public class BookingStack<T> {
    private Object[] stack;
    private int top;
    private int capacity;

    public BookingStack(int capacity) {
        this.capacity = capacity;
        stack = new Object[capacity];
        top = -1;
    }

    /** Push item onto stack — O(1) */
    public void push(T item) {
        if (top == capacity - 1)
            throw new RuntimeException("Stack Overflow");
        stack[++top] = item;
    }

    /** Pop item from stack — O(1) */
    @SuppressWarnings("unchecked")
    public T pop() {
        if (isEmpty())
            throw new RuntimeException("Stack Underflow");
        return (T) stack[top--];
    }

    /** Peek at top item without removing — O(1) */
    @SuppressWarnings("unchecked")
    public T peek() {
        if (isEmpty())
            throw new RuntimeException("Stack is empty");
        return (T) stack[top];
    }

    public boolean isEmpty() { return top == -1; }   // ✅ properly inside class
    public int size()        { return top + 1; }
}
