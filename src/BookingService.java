public class BookingService {
    private InventoryMap inventory;

    public BookingService(InventoryMap inventory) {
        this.inventory = inventory;
    }

    /**
     * Issue equipment to a student.
     * Performs all validation checks before creating the booking.
     */
    public Booking issueEquipment(Student student, String equipmentId)
            throws SuspendedUserException,
            UnpaidFineException,
            BorrowLimitExceededException,
            EquipmentNotFoundException {

        // Check 1: student must not be suspended
        if (student.isSuspended())
            throw new SuspendedUserException("Student is suspended");

        // Check 2: student must have no pending fines
        if (student.getPendingFine() > 0)
            throw new UnpaidFineException("Clear fines first");

        // Check 3: student must not exceed borrow limit
        if (student.getActiveBookings().size() >= Constants.MAX_BORROW_LIMIT)
            throw new BorrowLimitExceededException("Borrow limit reached");

        // Check 4: equipment must exist and be available
        Equipment equipment = inventory.get(equipmentId);  // ✅ fixed variable name
        if (equipment == null || !equipment.isAvailable())
            throw new EquipmentNotFoundException("Equipment unavailable");

        // Create booking
        Booking booking = new Booking(student, equipment);
        student.getActiveBookings().add(booking);
        equipment.setAvailable(false);    // ✅ fixed typo from original 'eqipment'
        return booking;
    }

    /**
     * Return equipment and calculate fine.
     */
    public double returnEquipment(Student student, Booking booking) {
        booking.setReturnDate(java.time.LocalDate.now());
        booking.setStatus("RETURNED");

        FineService fineService = new FineService();
        double fine = fineService.calculateFine(booking);
        booking.setFine(fine);

        if (fine > 0) {
            student.setPendingFine(student.getPendingFine() + fine);
        }

        booking.getEquipment().setAvailable(true);
        student.getActiveBookings().remove(booking);
        return fine;
    }

    /**
     * Pay off a student's pending fine.
     */
    public void payFine(Student student, double amount) {
        double remaining = student.getPendingFine() - amount;
        student.setPendingFine(Math.max(0, remaining));
    }
}
