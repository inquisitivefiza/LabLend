import java.time.temporal.ChronoUnit;

public class FineService {

    /**
     * Calculates fine for a booking.
     * Returns 0 if not yet returned or returned on time.
     */
    public double calculateFine(Booking booking) {
        if (booking.getReturnDate() == null) return 0;

        long daysLate = ChronoUnit.DAYS.between(
                booking.getDueDate(),
                booking.getReturnDate()
        );

        return daysLate > 0 ? daysLate * Constants.FINE_PER_DAY : 0;
    }
}
