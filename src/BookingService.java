
public class BookingService {
    private InventoryMap inventory;
    private WaitlistQueue<Student> waitlist;  // ADD THIS

    public BookingService(InventoryMap inventory) {
        this.inventory = inventory;
        this.waitlist = new WaitlistQueue<>();   // ADD THIS
    }

    public Booking issueEquipment(Student student, String equipmentId)
            throws SuspendedUserException, UnpaidFineException,
            BorrowLimitExceededException, EquipmentNotFoundException {

        if (student.isSuspended())
            throw new SuspendedUserException("Student is suspended");
        if (student.getPendingFine() > 0)
            throw new UnpaidFineException("Clear fines first");
        if (student.getActiveBookings().size() >= Constants.MAX_BORROW_LIMIT)
            throw new BorrowLimitExceededException("Borrow limit reached");

        Equipment equipment = inventory.get(equipmentId);
        if (equipment == null)
            throw new EquipmentNotFoundException("Equipment not found: " + equipmentId);

        // ADD WAITLIST LOGIC HERE:
        if (!equipment.isAvailable()) {
            waitlist.enqueue(student);    // put on waitlist instead of crashing
            System.out.println(student.getName() + " added to waitlist for " + equipmentId);
            throw new EquipmentNotFoundException("Equipment unavailable. Added to waitlist.");
        }

        Booking booking = new Booking(student, equipment);
        student.getActiveBookings().add(booking);
        equipment.decrementAvailable();   // use new method
        return booking;
    }

    // ADD: get next person from waitlist
    public Student getNextFromWaitlist() {
        if (waitlist.isEmpty()) return null;
        return waitlist.dequeue();
    }

    public double returnEquipment(Student student, Booking booking) {
        booking.setReturnDate(java.time.LocalDate.now());
        booking.setStatus("RETURNED");
        FineService fineService = new FineService();
        double fine = fineService.calculateFine(booking);
        booking.setFine(fine);
        if (fine > 0) student.setPendingFine(student.getPendingFine() + fine);
        booking.getEquipment().incrementAvailable();  // use new method
        student.getActiveBookings().remove(booking);

        // Notify next in waitlist
        Student next = getNextFromWaitlist();
        if (next != null)
            System.out.println("Equipment free! Notifying: " + next.getName());
        return fine;
    }

    public void payFine(Student student, double amount) {
        student.setPendingFine(Math.max(0, student.getPendingFine() - amount));
    }
}

