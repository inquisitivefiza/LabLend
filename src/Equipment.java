public class Equipment {
    private String id;
    private String name;
    private int riskLevel;
    private boolean isAvailable;
    private boolean isDamaged;

    public Equipment(String id, String name, int riskLevel) {
        this.id = id;
        this.name = name;
        this.riskLevel = riskLevel;
        this.isAvailable = true;
        this.isDamaged = false;
    }

    // Used by EquipmentHeap to compare by riskLevel (higher risk = higher priority)
    public int compareTo(Equipment other) {
        return Integer.compare(this.riskLevel, other.riskLevel);
    }

    public String getId()                           { return id; }
    public String getName()                         { return name; }
    public int getRiskLevel()                       { return riskLevel; }
    public boolean isAvailable()                    { return isAvailable; }
    public boolean isDamaged()                      { return isDamaged; }
    public void setAvailable(boolean a)             { this.isAvailable = a; }
    public void setDamaged(boolean d)               { this.isDamaged = d; }

    @Override
    public String toString() {
        return "Equipment{id='" + id + "', name='" + name +
                "', riskLevel=" + riskLevel +
                ", available=" + isAvailable + "}";
    }
}
