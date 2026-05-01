

public class Equipment {
    private String id;
    private String name;
    private int riskLevel;
    private int availableCount;   // CHANGED: was boolean isAvailable
    private boolean isDamaged;

    public Equipment(String id, String name, int riskLevel) {
        this.id = id;
        this.name = name;
        this.riskLevel = riskLevel;
        this.availableCount = 1;    // default: 1 copy available
        this.isDamaged = false;
    }

    // New constructor with quantity
    public Equipment(String id, String name, int riskLevel, int qty) {
        this(id, name, riskLevel);
        this.availableCount = qty;
    }

    // isAvailable = true only when at least 1 copy is free
    public boolean isAvailable() { return availableCount > 0; }

    // Call this when a booking is made
    public void decrementAvailable() {
        if (availableCount > 0) availableCount--;
    }

    // Call this when equipment is returned
    public void incrementAvailable() { availableCount++; }

    public int getAvailableCount()  { return availableCount; }
    public String getId()           { return id; }
    public String getName()         { return name; }
    public int getRiskLevel()       { return riskLevel; }
    public boolean isDamaged()      { return isDamaged; }
    public void setDamaged(boolean d) { this.isDamaged = d; }

    @Override
    public String toString() {
        return id + " | " + name + " | risk=" + riskLevel
                + " | available=" + availableCount;
    }
}

