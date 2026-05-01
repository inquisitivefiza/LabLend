

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BookingServiceTest {

    InventoryMap inventory;
    BookingService service;
    Student alice;

    @BeforeEach           // runs before EACH test — fresh state every time
    void setup() {
        inventory = new InventoryMap();
        inventory.addEquipment(new Equipment("E001","Oscilloscope",3));
        inventory.addEquipment(new Equipment("E002","Multimeter",1));
        inventory.addEquipment(new Equipment("E003","Soldering Iron",2));
        service = new BookingService(inventory);
        alice = new Student("S001","Alice","alice@lab.com");
    }

    @Test
    void happyPath_bookingSucceeds() throws Exception {
        Booking b = service.issueEquipment(alice, "E001");
        assertNotNull(b);
        assertEquals("ACTIVE", b.getStatus());
        assertFalse(inventory.get("E001").isAvailable());
        assertEquals(1, alice.getActiveBookings().size());
    }

    @Test
    void borrowLimit_throwsWhenExceeded() throws Exception {
        service.issueEquipment(alice, "E001");
        service.issueEquipment(alice, "E002");
        service.issueEquipment(alice, "E003");
        // 4th booking should throw
        assertThrows(BorrowLimitExceededException.class,
                () -> service.issueEquipment(alice, "E004"));
    }

    @Test
    void unpaidFine_blocksNewBooking() {
        alice.setPendingFine(50.0);
        assertThrows(UnpaidFineException.class,
                () -> service.issueEquipment(alice, "E001"));
    }

    @Test
    void suspendedUser_blocksBooking() {
        alice.setSuspended(true);
        assertThrows(SuspendedUserException.class,
                () -> service.issueEquipment(alice, "E001"));
    }
}


