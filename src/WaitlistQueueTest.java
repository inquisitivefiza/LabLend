import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class WaitlistQueueTest {

    @Test
    void fifoOrder_isCorrect() {
        WaitlistQueue<Student> q = new WaitlistQueue<>();
        q.enqueue(new Student("S001", "Alice", "alice@lab.com"));
        q.enqueue(new Student("S002", "Bob", "bob@lab.com"));
        q.enqueue(new Student("S003", "Charlie", "charlie@lab.com"));
        assertEquals("Alice",   q.dequeue().getName());
        assertEquals("Bob",     q.dequeue().getName());
        assertEquals("Charlie", q.dequeue().getName());
    }

    @Test
    void peek_doesNotRemove() {
        WaitlistQueue<Student> q = new WaitlistQueue<>();
        q.enqueue(new Student("S001", "Alice", "alice@lab.com"));
        q.peek();
        assertEquals(1, q.size());
        assertEquals("Alice", q.dequeue().getName());
    }

    @Test
    void dequeueEmpty_throwsException() {
        WaitlistQueue<Student> q = new WaitlistQueue<>();
        assertThrows(RuntimeException.class, q::dequeue);
    }

    @Test
    void isEmpty_correctlyDetected() {
        WaitlistQueue<Student> q = new WaitlistQueue<>();
        assertTrue(q.isEmpty());
        q.enqueue(new Student("S001", "X", "x@lab.com"));
        assertFalse(q.isEmpty());
    }
}
