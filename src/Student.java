import java.util.ArrayList;
import java.util.List;

public class Student extends User {
    private List<Booking> activeBookings;
    private double pendingFine;
    private boolean isCertified;   // ✅ fixed: was wrongly typed as double

    public Student(String id, String name, String email) {
        super(id, name, email);
        this.activeBookings = new ArrayList<>();
        this.pendingFine = 0.0;
        this.isCertified = false;
    }

    @Override
    public String getRole() {
        return "STUDENT";
    }

    public List<Booking> getActiveBookings()            { return activeBookings; }
    public double getPendingFine()                      { return pendingFine; }
    public boolean isCertified()                        { return isCertified; }
    public void setPendingFine(double fine)             { this.pendingFine = fine; }
    public void setCertified(boolean c)                 { this.isCertified = c; }
}
