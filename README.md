# Lab Equipment Booking System

## Introduction

This Lab Equipment Booking System is designed to facilitate the management of lab equipment and students in a university laboratory. It provides functionalities for both lab administrators and students, allowing them to perform various operations such as adding equipment, registering students, issuing/returning equipment, managing waitlists, and calculating fines for late returns.

## Demo

[![Watch the Demo](https://img.shields.io/badge/Watch-Demo%20Video-red?style=for-the-badge&logo=youtube)](https://youtu.be/dEdI96eqtjo?si=ywvORK7uBOVbtBWY)

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
| **Min-Heap** | `EquipmentHeap` | Prioritises equipment by risk level for inspection workflows |

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

To run this application, you need to have **Maven** and **Java 17+** installed.

### Clone the Repository

```bash
git clone https://github.com/<your-username>/LabEquipmentBookingSystem.git
```

### Navigate to the Project Directory

```bash
cd LabEquipmentBookingSystem
```

### Build the Project

```bash
mvn clean
mvn compile
mvn package
```

### Run the Application

```bash
java -jar target/LabEquipmentBookingSystem-1.0-SNAPSHOT.jar
```

> If you encounter an error, the JAR filename may differ. Check the `target/` folder and replace `LabEquipmentBookingSystem-1.0-SNAPSHOT` with the actual filename.

### Run Tests

```bash
mvn test
```

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
