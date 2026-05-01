# Lab Equipment Booking System

## Introduction

This Lab Equipment Booking System is designed to facilitate the management of lab equipment and students in a university laboratory. It provides functionalities for both lab administrators and students, allowing them to perform various operations such as adding equipment, registering students, issuing/returning equipment, managing waitlists, and calculating fines for late returns.

## 🎥 Demo Video

[▶ Watch Demo Video](https://youtu.be/dEdI96eqtjo?si=ywvORK7uBOVbtBWY)

## Features

### For Administrators:

1. **Add Student**: Register a new student into the system with their ID, name, and email.
2. **Book Equipment**: Issue a piece of lab equipment to a student.
3. **Return Equipment**: Process the return of equipment and automatically calculate any applicable late fines.
4. **Pay Fine**: Clear pending fines for a student.
5. **View Inventory**: Display all equipment currently in the lab with availability status and risk levels.

### For Students:

1. **Borrow Equipment**: Reserve available equipment (subject to borrow limit and fine status).
2. **Return Equipment**: Return borrowed equipment; fines are auto-calculated for late returns.
3. **Waitlist**: Automatically join a waitlist if the requested equipment is currently unavailable; notified when it becomes free.
4. **Pay Fine**: Clear any pending late-return fines to regain borrowing privileges.

## DSA Concepts Used

| Data Structure | Class | Purpose |
|---|---|---|
| **HashMap** | `InventoryMap` | O(1) average-case equipment lookup, insert, and delete |
| **Queue (FIFO)** | `WaitlistQueue` | Manages student waitlists in order of arrival |
| **Stack (LIFO)** | `BookingStack` | Tracks booking history; supports undo operations |
| **Max-Heap** | `EquipmentHeap` | Prioritises equipment by risk level for inspection workflows |

## Core Heart Algorithm

The central algorithm of this system lives in `BookingService.java` — specifically the `issueEquipment` method. It is the decision engine that every booking request passes through.

### Equipment Issue Flow

```
issueEquipment(student, equipmentId)
        │
        ▼
 Is student suspended?  ──YES──▶  throw SuspendedUserException
        │ NO
        ▼
 Has unpaid fine > 0?   ──YES──▶  throw UnpaidFineException
        │ NO
        ▼
 Active bookings >= 3?  ──YES──▶  throw BorrowLimitExceededException
        │ NO
        ▼
 Equipment exists in    ──NO───▶  throw EquipmentNotFoundException
    InventoryMap?
        │ YES
        ▼
 Equipment available    ──NO───▶  waitlist.enqueue(student)
  (count > 0)?                    throw EquipmentNotFoundException
        │ YES                     ("Added to waitlist")
        ▼
 Create Booking object
 student.activeBookings.add(booking)
 equipment.decrementAvailable()
        │
        ▼
   Return booking ✅
```

### Equipment Return Flow

```
returnEquipment(student, booking)
        │
        ▼
 Set returnDate = today
 Set status = "RETURNED"
        │
        ▼
 FineService.calculateFine(booking)
   daysLate = returnDate - dueDate
   fine = daysLate > 0 ? daysLate × Rs.10 : 0
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
        │                       notify next student
        ▼
   Return fine amount ✅
```

### Why These Data Structures?

**HashMap (`InventoryMap`)** — Equipment lookup by ID must be instant. A HashMap gives O(1) average get/put, which is critical when the system scales to hundreds of equipment items.

**Queue (`WaitlistQueue`)** — Fairness demands FIFO. The student who waited longest must be served first when equipment becomes available. A LinkedList-backed queue gives O(1) enqueue and dequeue.

**Stack (`BookingStack`)** — Booking history is naturally last-in-first-out. The most recent booking is the one most likely to be undone or reviewed, making a stack the ideal structure.

**Max-Heap (`EquipmentHeap`)** — For maintenance and inspection workflows, the highest-risk equipment must always be inspected first. A heap gives O(log n) insert and O(log n) extract-max, always surfacing the most critical item at the top.

## Project Structure

```
├── Main.java                  # Entry point — interactive console menu
├── BookingService.java        # Core business logic (issue, return, pay fine)
├── FineService.java           # Calculates late-return fines
├── InventoryMap.java          # HashMap-based equipment inventory
├── WaitlistQueue.java         # Generic FIFO queue for waitlist management
├── BookingStack.java          # Generic LIFO stack for booking history
├── EquipmentHeap.java         # Max-heap ordered by equipment risk level
├── Booking.java               # Booking entity (dates, status, fine)
├── Equipment.java             # Equipment entity (id, name, risk, availability)
├── Student.java               # Student entity (extends User)
├── User.java                  # Abstract base class for all users
├── Constants.java             # App-wide constants and custom exceptions
├── BookingServiceTest.java    # JUnit tests for BookingService
├── BookingStackTest.java      # JUnit tests for BookingStack
├── WaitlistQueueTest.java     # JUnit tests for WaitlistQueue
└── FineServiceTest.java       # JUnit tests for FineService
```

## Test Cases

Tests are written using **JUnit 5** and run directly inside **IntelliJ IDEA** (right-click the test file → Run).

### BookingServiceTest

| Test | What it verifies |
|---|---|
| `happyPath_bookingSucceeds()` | A valid student can book available equipment; status is `ACTIVE`, inventory decrements, and booking is added to student's list |
| `borrowLimit_throwsWhenExceeded()` | After 3 active bookings, a 4th attempt throws `BorrowLimitExceededException` |
| `unpaidFine_blocksNewBooking()` | A student with `pendingFine > 0` cannot book; throws `UnpaidFineException` |
| `suspendedUser_blocksBooking()` | A suspended student cannot book; throws `SuspendedUserException` |

### BookingStackTest

| Test | What it verifies |
|---|---|
| `lifoOrder_isCorrect()` | Push A, B, C → pop returns C, B, A in that order (LIFO) |
| `peek_doesNotRemove()` | `peek()` returns the top element but size stays the same |
| `overflow_throwsException()` | Pushing beyond capacity throws `RuntimeException` (Stack Overflow) |
| `underflow_throwsException()` | Popping from an empty stack throws `RuntimeException` (Stack Underflow) |

### WaitlistQueueTest

| Test | What it verifies |
|---|---|
| `fifoOrder_isCorrect()` | Enqueue Alice, Bob, Charlie → dequeue returns Alice, Bob, Charlie in order (FIFO) |
| `peek_doesNotRemove()` | `peek()` returns the front element but does not remove it; size stays 1 |
| `dequeueEmpty_throwsException()` | Dequeuing from an empty queue throws `RuntimeException` |
| `isEmpty_correctlyDetected()` | `isEmpty()` returns true on a new queue and false after an enqueue |

### FineServiceTest

| Test | What it verifies |
|---|---|
| `onTime_returnsZeroFine()` | Returning exactly on the due date results in a fine of Rs. 0 |
| `tenDaysLate_correctFine()` | Returning 10 days late results in Rs. 100 (10 × Rs. 10) |
| `nullReturnDate_returnsZero()` | If equipment has not been returned yet (`returnDate = null`), fine is Rs. 0 |
| `returnBeforeDue_returnsZero()` | Returning 2 days early results in a fine of Rs. 0 |

## Assumptions

1. Equipment is pre-loaded at startup with IDs `E001`, `E002`, and `E003`.
2. Each student can borrow a maximum of **3 items** at a time (`MAX_BORROW_LIMIT = 3`).
3. The default loan period is **7 days** (`MAX_BORROW_DAYS = 7`).
4. Late fine is charged at **Rs. 10 per day** beyond the due date (`FINE_PER_DAY = 10.0`).
5. A student with an **unpaid fine** cannot borrow any new equipment until it is cleared.
6. A **suspended student** is blocked from all booking operations.
7. If equipment is unavailable, the student is automatically added to a **FIFO waitlist** and notified when stock is replenished.
8. Equipment with multiple units (e.g., `Oscilloscope` with qty=3) tracks availability via an internal counter rather than separate objects.
9. Returning equipment to the inventory automatically pops the next student off the waitlist and notifies them.

## Installation

To run this application, you need **Java 17+** and **IntelliJ IDEA** installed.

### Clone the Repository

```bash
git clone https://github.com/<your-username>/LabEquipmentBookingSystem.git
```

### Open in IntelliJ IDEA

1. Open IntelliJ IDEA → **File → Open** → select the project folder.
2. IntelliJ will auto-detect the project structure.
3. Make sure **JUnit 5** is added as a dependency (IntelliJ will prompt you to add it if missing when you open any test file).

### Run the Application

1. Open `Main.java`.
2. Click the green **Run** button next to the `main` method, or press `Shift + F10`.

### Run Tests

1. Open any test file (e.g., `BookingServiceTest.java`).
2. Right-click inside the file → **Run 'BookingServiceTest'**.
3. Or right-click the project root → **Run All Tests** to execute the full test suite.

## Usage Example

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

Choice: 3
Student ID: S001
Returned. Fine: Rs.0.0
```
