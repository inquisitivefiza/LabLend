import java.time.LocalDate;

public class Booking {
    private String bookingId;
    private Student student;
    private Equipment equipment;
    private LocalDate issueDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private double fine;
    private String status;

    public Booking(Student student, Equipment equipment) {
        this.bookingId   = "B" + System.currentTimeMillis();
        this.student     = student;
        this.equipment   = equipment;
        this.issueDate   = LocalDate.now();
        this.dueDate     = LocalDate.now().plusDays(Constants.MAX_BORROW_DAYS);
        this.returnDate  = null;
        this.fine        = 0.0;
        this.status      = "ACTIVE";
    }

    public String getBookingId()                        { return bookingId; }
    public Student getStudent()                         { return student; }
    public Equipment getEquipment()                     { return equipment; }
    public LocalDate getIssueDate()                     { return issueDate; }
    public LocalDate getDueDate()                       { return dueDate; }
    public LocalDate getReturnDate()                    { return returnDate; }
    public double getFine()                             { return fine; }
    public String getStatus()                           { return status; }

    public void setReturnDate(LocalDate d)              { this.returnDate = d; }
    public void setStatus(String s)                     { this.status = s; }
    public void setFine(double f)                       { this.fine = f; }

    @Override
    public String toString() {
        return "Booking{id='" + bookingId +
                "', equipment='" + equipment.getName() +
                "', status='" + status + "'}";
    }
}
