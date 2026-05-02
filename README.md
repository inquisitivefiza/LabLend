# 🔬 Lab Equipment Booking System

> A console-based Java application that simulates a university laboratory's **equipment management workflow** using a multi-layer DSA engine — HashMap inventory, FIFO waitlist, LIFO booking history, and a Max-Heap inspection scheduler — built entirely on core DSA and OOP principles.

---

## 📌 Problem Statement

University labs face a recurring challenge managing shared equipment without a structured system:

- Students request the same equipment simultaneously — no mechanism to handle **unavailability fairly**.
- **High-risk equipment** (oscilloscopes, soldering irons) gets inspected in arbitrary order, not by criticality.
- There is **no borrow-limit enforcement**, allowing one student to monopolise the entire inventory.
- **Fines for late returns** are calculated manually and inconsistently — or not at all.
- Students with **unpaid fines or suspensions** can still borrow freely without any gate check.

This system solves each of these by implementing a **validation-first booking engine**, a FIFO waitlist for unavailable equipment, a fine calculator tied to the booking's due date, and a max-heap that always surfaces the highest-risk item for inspection first.

---

## 🎥 Demo Video

[▶ Watch Demo Video](https://youtu.be/dEdI96eqtjo?si=ywvORK7uBOVbtBWY)

## ✨ Features

### For Administrators
| Feature | Description |
|---|---|
| Add Student | Register a new student with ID, name, and email |
| Book Equipment | Issue a piece of lab equipment to a student — runs all validation checks before proceeding |
| Return Equipment | Process return, auto-calculate late fine, increment availability, and notify next waitlisted student |
| Pay Fine | Clear a student's pending fine so they can borrow again |
| View Inventory | Display all equipment with availability count and risk level |

### For Students
| Feature | Description |
|---|---|
| Borrow Equipment | Reserve available equipment — blocked by borrow limit, unpaid fine, or suspension |
| Return Equipment | Return borrowed equipment; fine is computed automatically at `Rs.10 × days late` |
| Waitlist | Automatically join a FIFO waitlist if equipment is unavailable; notified when stock is freed |
| Pay Fine | Clear pending fine to regain borrowing privileges |

---

## 🧠 OOP Design

### Class Architecture

```
BookingService                    ← core booking engine
 ├── InventoryMap                 ← HashMap-backed equipment store
 └── WaitlistQueue<Student>       ← LinkedList-backed FIFO waitlist

FineService                       ← fine calculation (standalone, injected per call)

BookingStack<T>                   ← generic LIFO stack for booking history
EquipmentHeap                     ← array-backed Max-Heap for inspection order

Entities
 ├── User (abstract)
 │    └── Student    (extends User — activeBookings, pendingFine, isCertified)
 ├── Equipment       (id, name, riskLevel, availableCount — implements Comparable)
 ├── Booking         (student, equipment, issueDate, dueDate, returnDate, fine, status)
 └── Constants       + custom exceptions (SuspendedUserException, UnpaidFineException,
                       BorrowLimitExceededException, EquipmentNotFoundException)
```

### OOP Principles Applied

| Principle | Where Applied |
|---|---|
| **Abstraction** | `User.java` is an abstract class declaring `getRole()` as abstract — every user type must implement it. `BookingStack`, `WaitlistQueue`, and `EquipmentHeap` each expose a clean public API and hide all internal array/linked-list mechanics |
| **Inheritance** | `Student` extends `User`, inheriting `id`, `name`, `email`, `isSuspended`, and the suspension gate — adding `activeBookings`, `pendingFine`, and `isCertified` on top |
| **Encapsulation** | All entity fields are `private` with controlled getter/setter access. Fine accumulation (`student.setPendingFine(student.getPendingFine() + fine)`) and availability tracking (`equipment.decrementAvailable()` / `incrementAvailable()`) stay inside the owning class |
| **Polymorphism** | `WaitlistQueue<S extends User>` and `BookingStack<T>` are generic — they work for any type that satisfies the bound, and the dequeue/pop logic is identical regardless of the concrete type passed in |
| **Single Responsibility** | `BookingService` owns booking and return flow. `FineService` owns fine calculation only. `InventoryMap` owns equipment CRUD. `WaitlistQueue` owns fairness ordering. Each class has exactly one reason to change |

---

## ❤️ Core Heart Algorithm

The central algorithm lives in **`BookingService.java`**. Two methods — `issueEquipment` and `returnEquipment` — form the complete lifecycle of every booking in the system.

### Flow 1 — Issue Equipment (`BookingService.issueEquipment`)

```
issueEquipment(student, equipmentId)
        │
        ▼
  student.isSuspended()?        ──YES──▶  throw SuspendedUserException
        │ NO
        ▼
  student.getPendingFine() > 0? ──YES──▶  throw UnpaidFineException
        │ NO
        ▼
  activeBookings.size() >= 3?   ──YES──▶  throw BorrowLimitExceededException
        │ NO                               (MAX_BORROW_LIMIT = 3)
        ▼
  inventory.get(equipmentId)    ──NULL──▶  throw EquipmentNotFoundException
        │ found
        ▼
  equipment.isAvailable()?      ──NO───▶  waitlist.enqueue(student)
        │                                  throw EquipmentNotFoundException
        │ YES                              ("Added to waitlist")
        ▼
  new Booking(student, equipment)
  student.activeBookings.add(booking)
  equipment.decrementAvailable()
        │
        ▼
  Return booking ✅
```

**The guard order matters.** Suspension is checked before fines, fines before borrow limit, borrow limit before inventory lookup. This ensures the most user-actionable error is thrown first — a suspended student never sees a fine message, and a student at the borrow limit never triggers an unnecessary inventory lookup.

### Flow 2 — Return Equipment (`BookingService.returnEquipment`)

```
returnEquipment(student, booking)
        │
        ▼
  booking.setReturnDate(LocalDate.now())
  booking.setStatus("RETURNED")
        │
        ▼
  FineService.calculateFine(booking)
    returnDate == null?           ──YES──▶  return 0.0
        │ NO
    daysLate = returnDate - dueDate
    fine = daysLate > 0 ? daysLate × Rs.10 : 0.0
        │
        ▼
  fine > 0?  ──YES──▶  student.pendingFine += fine
        │ NO
        ▼
  equipment.incrementAvailable()
  student.activeBookings.remove(booking)
        │
        ▼
  waitlist.isEmpty()?  ──NO──▶  next = waitlist.dequeue()
        │                        System.out → "Notifying: " + next.getName()
        ▼
  Return fine amount ✅
```

### Flow 3 — Inspection Priority (`EquipmentHeap`)

```
EquipmentHeap.insert(equipment)        →  heapifyUp()   O(log n)
EquipmentHeap.extractMax()             →  heapifyDown()  O(log n)

Comparator: Equipment.compareTo(other)
  → Integer.compare(other.riskLevel, this.riskLevel)
  → higher riskLevel = higher priority = extracted first
```

**Soldering Iron (risk=2) is always inspected before Multimeter (risk=1).
Oscilloscope (risk=3) is always inspected first of all.**

### Why These Data Structures?

**`HashMap` (`InventoryMap`)** — Equipment lookup by ID must be O(1). A HashMap gives O(1) average `get`/`put`/`remove`, which is critical when inventory scales to hundreds of items. Linear scan would make every booking slower proportional to inventory size.

**`LinkedList`-backed `WaitlistQueue`** — Fairness demands FIFO: the student who waited longest is served first when equipment is freed. A `LinkedList` gives O(1) `addLast` (enqueue) and O(1) `removeFirst` (dequeue) — no shifting like an array would require.

**Array-backed `BookingStack`** — Booking history is naturally last-in-first-out: the most recent booking is the one most likely to be reviewed or undone. A fixed-capacity array with a `top` pointer gives O(1) `push` and `pop`, with overflow protection built in.

**Array-backed `EquipmentHeap`** — For maintenance and inspection workflows, the highest-risk equipment must always be inspected first. A heap gives O(log n) insert and O(log n) `extractMax` via `heapifyUp`/`heapifyDown`, always surfacing the most critical item at index 0.

---

## 🗂️ DSA Concepts Used

| Data Structure | Class | Complexity | Purpose |
|---|---|---|---|
| **HashMap** | `InventoryMap` | O(1) avg get / put / remove | Instant equipment lookup, insert, and delete by ID |
| **Queue (FIFO)** | `WaitlistQueue<S extends User>` | O(1) enqueue / O(1) dequeue | FIFO waitlist — first student to wait is first to be notified when equipment frees |
| **Stack (LIFO)** | `BookingStack<T>` | O(1) push / O(1) pop | Booking history — most recent booking at top; supports undo-style operations |
| **Max-Heap** | `EquipmentHeap` | O(log n) insert / O(log n) extractMax | Inspection scheduler — always surfaces the highest-risk equipment first |

---

## 📁 Project Structure

```
├── Main.java                  # Entry point — interactive console menu (Admin + Student flows)
├── BookingService.java        # Core engine: issueEquipment, returnEquipment, payFine, waitlist
├── FineService.java           # Fine calculation: daysLate × Rs.10, null-safe, early-return safe
├── InventoryMap.java          # HashMap wrapper: addEquipment, get, contains, remove, printInventory
├── WaitlistQueue.java         # Generic FIFO queue (LinkedList-backed): enqueue, dequeue, peek
├── BookingStack.java          # Generic LIFO stack (array-backed): push, pop, peek, overflow guard
├── EquipmentHeap.java         # Max-Heap (array-backed): insert, extractMax, heapifyUp, heapifyDown
├── Booking.java               # Entity: bookingId, issueDate, dueDate, returnDate, fine, status
├── Equipment.java             # Entity: id, name, riskLevel, availableCount — implements Comparable
├── Student.java               # Entity: extends User — activeBookings, pendingFine, isCertified
├── User.java                  # Abstract base: id, name, email, isSuspended — abstract getRole()
├── Constants.java             # MAX_BORROW_LIMIT=3, MAX_BORROW_DAYS=7, FINE_PER_DAY=10.0
│                              # + SuspendedUserException, UnpaidFineException,
│                              #   BorrowLimitExceededException, EquipmentNotFoundException
├── BookingServiceTest.java    # JUnit 5 tests for BookingService (4 tests)
├── BookingStackTest.java      # JUnit 5 tests for BookingStack (4 tests)
├── WaitlistQueueTest.java     # JUnit 5 tests for WaitlistQueue (4 tests)
└── FineServiceTest.java       # JUnit 5 tests for FineService (4 tests)
```

---

## 🧪 Test Cases

Tests are written using **JUnit 5** and run directly inside **IntelliJ IDEA** (right-click the test file → Run).  
A `@BeforeEach` setup in `BookingServiceTest` rebuilds a fresh `InventoryMap`, `BookingService`, and `Student` before every test — no shared state.

### `BookingServiceTest`

| Test | What it verifies |
|---|---|
| `happyPath_bookingSucceeds()` | A valid student books `E001`; returned booking is non-null with status `ACTIVE`, inventory shows unavailable, and `alice.activeBookings.size() == 1` |
| `borrowLimit_throwsWhenExceeded()` | After booking `E001`, `E002`, `E003` (limit=3), a 4th attempt on `E001` throws `BorrowLimitExceededException` — not `EquipmentNotFoundException` |
| `unpaidFine_blocksNewBooking()` | Setting `alice.pendingFine = 50.0` before any booking throws `UnpaidFineException` on the first attempt |
| `suspendedUser_blocksBooking()` | Setting `alice.suspended = true` throws `SuspendedUserException` before any other check runs |

### `BookingStackTest`

| Test | What it verifies |
|---|---|
| `lifoOrder_isCorrect()` | Push `"A"`, `"B"`, `"C"` into a capacity-5 stack → pop returns `"C"`, `"B"`, `"A"` in that order (LIFO) |
| `peek_doesNotRemove()` | After pushing `"A"`, `peek()` returns `"A"` but `size()` stays `1` — peek does not consume the element |
| `overflow_throwsException()` | Pushing a 3rd element into a capacity-2 stack throws `RuntimeException` ("Stack Overflow") |
| `underflow_throwsException()` | Calling `pop()` on a freshly created empty stack throws `RuntimeException` ("Stack Underflow") |

### `WaitlistQueueTest`

| Test | What it verifies |
|---|---|
| `fifoOrder_isCorrect()` | Enqueue Alice, Bob, Charlie → dequeue returns `"Alice"`, `"Bob"`, `"Charlie"` in arrival order (FIFO) |
| `peek_doesNotRemove()` | After enqueuing Alice, `peek()` returns her name but `size()` stays `1` — front element is not removed |
| `dequeueEmpty_throwsException()` | Calling `dequeue()` on an empty `WaitlistQueue` throws `RuntimeException` ("Queue is empty") |
| `isEmpty_correctlyDetected()` | `isEmpty()` returns `true` on a new queue and `false` immediately after one `enqueue` |

### `FineServiceTest`

| Test | What it verifies |
|---|---|
| `onTime_returnsZeroFine()` | Setting `returnDate = dueDate` (exactly on time) returns a fine of `0.0` |
| `tenDaysLate_correctFine()` | Setting `returnDate = dueDate.plusDays(10)` returns `100.0` — formula: `10 × Rs.10` |
| `nullReturnDate_returnsZero()` | `returnDate` defaults to `null` on a new booking (not yet returned) — `calculateFine` returns `0.0` |
| `returnBeforeDue_returnsZero()` | Setting `returnDate = dueDate.minusDays(2)` (2 days early) returns `0.0` — no negative fines |

---

## 📐 Assumptions

1. Equipment is pre-loaded at startup with IDs `E001` (Oscilloscope, qty=3), `E002` (Multimeter, qty=1), `E003` (Soldering Iron, qty=2).
2. Each student can borrow a maximum of **3 items** at a time (`MAX_BORROW_LIMIT = 3`).
3. The default loan period is **7 days** from issue date (`MAX_BORROW_DAYS = 7`).
4. Late fine is charged at **Rs. 10 per day** beyond the due date (`FINE_PER_DAY = 10.0`).
5. A student with an **unpaid fine** cannot borrow any new equipment until it is cleared via `payFine`.
6. A **suspended student** is blocked from all booking operations — checked before any other validation.
7. If equipment is unavailable, the student is automatically added to a **FIFO waitlist** and notified when the next unit is returned.
8. Equipment with multiple units tracks availability via an internal `availableCount` counter — not separate objects per unit.
9. `EquipmentHeap` is used for the inspection/maintenance workflow only; the main booking flow uses `InventoryMap` for O(1) access.
10. All four custom exceptions (`SuspendedUserException`, `UnpaidFineException`, `BorrowLimitExceededException`, `EquipmentNotFoundException`) are defined in `Constants.java`.

---

## ⚙️ Installation

Requires **Java 17+** and **IntelliJ IDEA**.

```bash
git clone https://github.com/<your-username>/LabEquipmentBookingSystem.git
```

1. Open IntelliJ IDEA → **File → Open** → select the project folder.
2. IntelliJ will auto-detect the project structure.
3. When you open any test file, IntelliJ will prompt you to add **JUnit 5** — accept it.

**Run the app:** Open `Main.java` → click the green Run button or press `Shift + F10`.  
**Run a single test class:** Right-click `BookingServiceTest.java` → **Run 'BookingServiceTest'**.  
**Run all tests:** Right-click the project root → **Run All Tests**.

---

## 💻 Usage Example

```
1.Add Student  2.Book  3.Return  4.Pay Fine  5.Inventory  0.Exit
Choice: 1
Student ID: S001
Name: Alice
Email: alice@lab.com
Student added.

Choice: 2
Student ID: S001
Equipment ID: E001
Booked! Due: 2025-05-08

Choice: 5
--- Inventory (3 items) ---
  E001 | Oscilloscope   | risk=3 | available=2
  E002 | Multimeter     | risk=1 | available=1
  E003 | Soldering Iron | risk=2 | available=2

Choice: 3
Student ID: S001
Returned. Fine: Rs.0.0

Choice: 0
Bye!
```

---

## 🤝 Contributing

Contributions are welcome! Please fork the repository and submit a pull request with your changes.

1. Fork the repository
2. Create your feature branch: `git checkout -b feature/your-feature-name`
3. Commit your changes: `git commit -m "Add your feature"`
4. Push to the branch: `git push origin feature/your-feature-name`
5. Open a Pull Request

---

## 📁 Repository

You can find the GitHub repository for this project **[here](https://github.com/inquisitivefiza/LabLend)**.

Feel free to reach out if you have any questions or need any help!
