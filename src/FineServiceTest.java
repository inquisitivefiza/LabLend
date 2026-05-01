

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

public class FineServiceTest {

    FineService fineService = new FineService();

    @Test
    void onTime_returnsZeroFine() {
        Student alice = new Student("S001","Alice","a@b.com");
        Equipment eq = new Equipment("E001","Scope",1);
        Booking b = new Booking(alice, eq);
        // Return exactly on due date — no fine
        b.setReturnDate(b.getDueDate());
        assertEquals(0.0, fineService.calculateFine(b));
    }

    @Test
    void tenDaysLate_correctFine() {
        Student alice = new Student("S001","Alice","a@b.com");
        Equipment eq = new Equipment("E001","Scope",1);
        Booking b = new Booking(alice, eq);
        b.setReturnDate(b.getDueDate().plusDays(10));
        // 10 days × Rs.10 = Rs.100
        assertEquals(100.0, fineService.calculateFine(b));
    }

    @Test
    void nullReturnDate_returnsZero() {
        Student alice = new Student("S001","Alice","a@b.com");
        Equipment eq = new Equipment("E001","Scope",1);
        Booking b = new Booking(alice, eq);
        // returnDate is null by default — not yet returned
        assertEquals(0.0, fineService.calculateFine(b));
    }

    @Test
    void returnBeforeDue_returnsZero() {
        Student alice = new Student("S001","Alice","a@b.com");
        Equipment eq = new Equipment("E001","Scope",1);
        Booking b = new Booking(alice, eq);
        b.setReturnDate(b.getDueDate().minusDays(2)); // early return
        assertEquals(0.0, fineService.calculateFine(b));
    }
}


