<<<<<<< HEAD
=======
// implements Comparable so EquipmentHeap can compare by riskLevel
>>>>>>> cbffb5969401e251ff0d4969af56aa990afe4abb
public class Equipment implements Comparable<Equipment> {
    private String id;
    private String name;
    private int riskLevel;
    private int availableCount;
    private boolean isDamaged;

    public Equipment(String id, String name, int riskLevel) {
        this.id = id;
        this.name = name;
        this.riskLevel = riskLevel;
        this.availableCount = 1;
        this.isDamaged = false;
    }

<<<<<<< HEAD
=======
    // Constructor with quantity
>>>>>>> cbffb5969401e251ff0d4969af56aa990afe4abb
    public Equipment(String id, String name, int riskLevel, int qty) {
        this(id, name, riskLevel);
        this.availableCount = qty;
    }

    public boolean isAvailable()        { return availableCount > 0; }
    public void decrementAvailable()    { if (availableCount > 0) availableCount--; }
    public void incrementAvailable()    { availableCount++; }
    public int getAvailableCount()      { return availableCount; }
    public String getId()               { return id; }
    public String getName()             { return name; }
    public int getRiskLevel()           { return riskLevel; }
    public boolean isDamaged()          { return isDamaged; }
    public void setDamaged(boolean d)   { this.isDamaged = d; }

    @Override
    public int compareTo(Equipment other) {
        return Integer.compare(other.riskLevel, this.riskLevel); // higher risk = higher priority
    }

<<<<<<< HEAD
=======
    // Call this when equipment is returned
    public void incrementAvailable() { availableCount++; }

    public int getAvailableCount()    { return availableCount; }
    public String getId()             { return id; }
    public String getName()           { return name; }
    public int getRiskLevel()         { return riskLevel; }
    public boolean isDamaged()        { return isDamaged; }
    public void setDamaged(boolean d) { this.isDamaged = d; }

    // Higher riskLevel = higher priority in the heap
    @Override
    public int compareTo(Equipment other) {
        return Integer.compare(other.riskLevel, this.riskLevel);
    }

>>>>>>> cbffb5969401e251ff0d4969af56aa990afe4abb
    @Override
    public String toString() {
        return id + " | " + name + " | risk=" + riskLevel
                + " | available=" + availableCount;
    }
}