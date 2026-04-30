// ─── Constants ───────────────────────────────────────────────────────────────
public class Constants {
    public static final int    MAX_BORROW_LIMIT = 3;
    public static final double FINE_PER_DAY     = 10.0;
    public static final int    MAX_BORROW_DAYS  = 7;
}

// ─── Custom Exceptions ────────────────────────────────────────────────────────
class SuspendedUserException extends Exception {
    public SuspendedUserException(String message) { super(message); }
}

class UnpaidFineException extends Exception {
    public UnpaidFineException(String message) { super(message); }
}

class BorrowLimitExceededException extends Exception {
    public BorrowLimitExceededException(String message) { super(message); }
}

class EquipmentNotFoundException extends Exception {
    public EquipmentNotFoundException(String message) { super(message); }
}

