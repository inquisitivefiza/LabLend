// DSA Concept: Min-Heap  —  always gives the highest-risk equipment first
// (Equipment.compareTo uses riskLevel, so lower compareTo = higher priority)
public class EquipmentHeap {
    private Equipment[] heap;
    private int size;

    public EquipmentHeap(int capacity) {   // ✅ constructor added
        heap = new Equipment[capacity];
        size = 0;
    }

    /** Insert equipment and restore heap property — O(log n) */
    public void insert(Equipment e) {
        if (size == heap.length) throw new RuntimeException("Heap is full");
        heap[size] = e;
        heapifyUp(size);
        size++;
    }

    /** Remove and return the highest-risk equipment — O(log n) */
    public Equipment extractMax() {
        if (size == 0) throw new RuntimeException("Heap is empty");
        Equipment top = heap[0];
        heap[0] = heap[--size];
        heapifyDown(0);
        return top;
    }

    /** Peek at the highest-risk equipment without removing — O(1) */
    public Equipment peek() {
        if (size == 0) throw new RuntimeException("Heap is empty");
        return heap[0];
    }

    // ── Internal helpers ──────────────────────────────────────────────────────

    private void heapifyUp(int i) {
        while (i > 0 && heap[parent(i)].compareTo(heap[i]) > 0) {
            swap(i, parent(i));
            i = parent(i);
        }
    }

    private void heapifyDown(int i) {   // ✅ added (required for extractMax)
        int largest = i;
        int left  = 2 * i + 1;
        int right = 2 * i + 2;
        if (left  < size && heap[left].compareTo(heap[largest])  < 0) largest = left;
        if (right < size && heap[right].compareTo(heap[largest]) < 0) largest = right;
        if (largest != i) {
            swap(i, largest);
            heapifyDown(largest);
        }
    }

    private void swap(int i, int j) {
        Equipment tmp = heap[i];
        heap[i] = heap[j];
        heap[j] = tmp;
    }

    private int parent(int i) { return (i - 1) / 2; }
    public  int size()        { return size; }
    public  boolean isEmpty() { return size == 0; }
}
