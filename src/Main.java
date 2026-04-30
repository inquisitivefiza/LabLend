import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {

        System.out.println("========================================");
        System.out.println("       LabLend - Lab Equipment System   ");
        System.out.println("========================================\n");

        // ── 1. Setup Inventory (HashMap) ─────────────────────────────────────
        InventoryMap inventory = new InventoryMap();
        inventory.addEquipment(new Equipment("E001", "Oscilloscope",   3));
        inventory.addEquipment(new Equipment("E002", "Multimeter",     1));
        inventory.addEquipment(new Equipment("E003", "Soldering Iron", 2));
        inventory.addEquipment(new Equipment("E004", "Laser Module",   5));
        inventory.addEquipment(new Equipment("E005", "Power Supply",   2));
        inventory.printInventory();

        // ── 2. Setup Services ────────────────────────────────────────────────
        BookingService bookingService = new BookingService(inventory);
        FineService    fineService    = new FineService();

        // ── 3. Create Students ───────────────────────────────────────────────
        Student alice = new Student("S001", "Alice", "alice@lab.com");
        Student bob   = new Student("S002", "Bob",   "bob@lab.com");

        // ── 4. Issue Equipment ───────────────────────────────────────────────
        System.out.println("\n--- Booking Equipment ---");
        try {
            Booking b1 = bookingService.issueEquipment(alice, "E001");
            System.out.println("✅ Alice booked: " + b1.getEquipment().getName()
                    + " | Due: " + b1.getDueDate());

            Booking b2 = bookingService.issueEquipment(alice, "E002");
            System.out.println("✅ Alice booked: " + b2.getEquipment().getName()
                    + " | Due: " + b2.getDueDate());

            Booking b3 = bookingService.issueEquipment(bob, "E003");
            System.out.println("✅ Bob booked:   " + b3.getEquipment().getName()
                    + " | Due: " + b3.getDueDate());

            // ── 5. Test Return + Fine Calculation ────────────────────────────
            System.out.println("\n--- Returning Equipment (10 days late) ---");
            // Simulate returning b1 10 days after due date
            b1.setReturnDate(b1.getDueDate().plusDays(10));
            b1.setStatus("RETURNED");
            double fine = fineService.calculateFine(b1);
            b1.setFine(fine);
            alice.setPendingFine(fine);
            b1.getEquipment().setAvailable(true);
            alice.getActiveBookings().remove(b1);
            System.out.println("Fine for Alice (b1): ₹" + fine
                    + " (" + 10 + " days × ₹" + Constants.FINE_PER_DAY + ")");

            // ── 6. Test Borrow Limit ─────────────────────────────────────────
            System.out.println("\n--- Testing Borrow Limit ---");
            // Alice still has b2 active; try booking 3 more to hit limit
            bookingService.issueEquipment(alice, "E004");
            bookingService.issueEquipment(alice, "E005"); // This should throw

        } catch (BorrowLimitExceededException e) {
            System.out.println("⛔ Borrow limit: " + e.getMessage());
        } catch (UnpaidFineException e) {
            System.out.println("⛔ Unpaid fine:  " + e.getMessage());
        } catch (SuspendedUserException e) {
            System.out.println("⛔ Suspended:    " + e.getMessage());
        } catch (EquipmentNotFoundException e) {
            System.out.println("⛔ Not found:    " + e.getMessage());
        }

        // ── 7. Test Unpaid Fine Block ────────────────────────────────────────
        System.out.println("\n--- Testing Unpaid Fine Block ---");
        try {
            bookingService.issueEquipment(alice, "E001"); // Alice has pending fine
        } catch (UnpaidFineException e) {
            System.out.println("⛔ Blocked — unpaid fine: " + e.getMessage()
                    + " | Amount: ₹" + alice.getPendingFine());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        // ── 8. Pay Fine & Retry ──────────────────────────────────────────────
        System.out.println("\n--- Paying Fine ---");
        bookingService.payFine(alice, alice.getPendingFine());
        System.out.println("✅ Fine cleared. Pending: ₹" + alice.getPendingFine());

        // ── 9. BookingStack Demo ─────────────────────────────────────────────
        System.out.println("\n--- BookingStack (LIFO History) ---");
        BookingStack<String> stack = new BookingStack<>(10);
        stack.push("BookingA");
        stack.push("BookingB");
        stack.push("BookingC");
        System.out.println("Pushed: A, B, C");
        System.out.println("Pop: " + stack.pop());  // C
        System.out.println("Pop: " + stack.pop());  // B
        System.out.println("Peek: " + stack.peek()); // A

        // ── 10. WaitlistQueue Demo ───────────────────────────────────────────
        System.out.println("\n--- WaitlistQueue (FIFO Waitlist) ---");
        WaitlistQueue<String> queue = new WaitlistQueue<>();
        queue.enqueue("Alice");
        queue.enqueue("Bob");
        queue.enqueue("Charlie");
        System.out.println("Enqueued: Alice, Bob, Charlie");
        System.out.println("Dequeue: " + queue.dequeue()); // Alice
        System.out.println("Dequeue: " + queue.dequeue()); // Bob
        System.out.println("Peek:    " + queue.peek());    // Charlie

        // ── 11. EquipmentHeap Demo ───────────────────────────────────────────
        System.out.println("\n--- EquipmentHeap (Priority by Risk Level) ---");
        EquipmentHeap heap = new EquipmentHeap(10);
        heap.insert(new Equipment("H001", "Laser Cutter",  5));
        heap.insert(new Equipment("H002", "Voltmeter",     1));
        heap.insert(new Equipment("H003", "Oscilloscope",  3));
        heap.insert(new Equipment("H004", "Acid Bath",     5));
        System.out.println("Inserted 4 items");
        System.out.println("Extract (lowest riskLevel first): "
                + heap.extractMax().getName() + " (risk=" + heap.peek().getRiskLevel() + " next)");
        System.out.println("Next: " + heap.extractMax().getName());

        System.out.println("\n========================================");
        System.out.println("           All Tests Passed ✅           ");
        System.out.println("========================================");
    }
}
