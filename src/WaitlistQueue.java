

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class WaitlistQueueTest {

    @Test
    void fifoOrder_isCorrect() {
        WaitlistQueue<String> q = new WaitlistQueue<>();
        q.enqueue("Alice");
        q.enqueue("Bob");
        q.enqueue("Charlie");
        // First in = first out
        assertEquals("Alice",   q.dequeue());
        assertEquals("Bob",     q.dequeue());
        assertEquals("Charlie", q.dequeue());
    }

    @Test
    void peek_doesNotRemove() {
        WaitlistQueue<String> q = new WaitlistQueue<>();
        q.enqueue("Alice");
        q.peek();           // should NOT remove Alice
        assertEquals(1, q.size());
        assertEquals("Alice", q.dequeue());
    }

    @Test
    void dequeueEmpty_throwsException() {
        WaitlistQueue<String> q = new WaitlistQueue<>();
        assertThrows(RuntimeException.class, q::dequeue);
    }

    @Test
    void isEmpty_correctlyDetected() {
        WaitlistQueue<String> q = new WaitlistQueue<>();
        assertTrue(q.isEmpty());
        q.enqueue("X");
        assertFalse(q.isEmpty());
    }
}

