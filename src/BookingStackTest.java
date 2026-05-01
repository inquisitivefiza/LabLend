

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BookingStackTest {

    @Test
    void lifoOrder_isCorrect() {
        BookingStack<String> stack = new BookingStack<>(5);
        stack.push("A");
        stack.push("B");
        stack.push("C");
        // Last in = first out
        assertEquals("C", stack.pop());
        assertEquals("B", stack.pop());
        assertEquals("A", stack.pop());
    }

    @Test
    void peek_doesNotRemove() {
        BookingStack<String> stack = new BookingStack<>(3);
        stack.push("A");
        assertEquals("A", stack.peek());
        assertEquals(1, stack.size()); // still 1, peek didn't remove
    }

    @Test
    void overflow_throwsException() {
        BookingStack<String> stack = new BookingStack<>(2);
        stack.push("A");
        stack.push("B");
        // 3rd push exceeds capacity=2
        assertThrows(RuntimeException.class, () -> stack.push("C"));
    }

    @Test
    void underflow_throwsException() {
        BookingStack<String> stack = new BookingStack<>(3);
        assertThrows(RuntimeException.class, stack::pop);
    }
}

