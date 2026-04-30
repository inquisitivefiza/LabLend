import java.util.HashMap;
import java.util.Collection;

// DSA Concept: HashMap  —  O(1) average lookup, insert, delete
public class InventoryMap {
    private HashMap<String, Equipment> map = new HashMap<>();

    /** Add a new equipment item to the inventory */
    public void addEquipment(Equipment e) {
        map.put(e.getId(), e);
    }

    /** Retrieve equipment by ID; returns null if not found */
    public Equipment get(String id) {
        return map.get(id);
    }

    /** Check if an equipment ID exists in inventory */
    public boolean contains(String id) {
        return map.containsKey(id);
    }

    /** Remove equipment from inventory */
    public void remove(String id) {
        map.remove(id);
    }

    /** Return all equipment items */
    public Collection<Equipment> getAllEquipment() {
        return map.values();
    }

    /** Total number of items in inventory */
    public int size() {
        return map.size();
    }

    /** Display the full inventory */
    public void printInventory() {
        System.out.println("\n--- Inventory (" + map.size() + " items) ---");
        for (Equipment e : map.values()) {
            System.out.println("  " + e);
        }
    }
}