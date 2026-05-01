

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        InventoryMap inventory = new InventoryMap();
        BookingService bookingService = new BookingService(inventory);

        // Pre-load some equipment
        inventory.addEquipment(new Equipment("E001","Oscilloscope",3));
        inventory.addEquipment(new Equipment("E002","Multimeter",1));
        inventory.addEquipment(new Equipment("E003","Soldering Iron",2));

        // Store students in a simple map for demo
        java.util.HashMap<String,Student> students = new java.util.HashMap<>();

        while (true) {
            System.out.println("\n1.Add Student  2.Book  3.Return  4.Pay Fine  5.Inventory  0.Exit");
            System.out.print("Choice: ");
            int choice = sc.nextInt(); sc.nextLine();

            switch (choice) {
                case 0 -> { System.out.println("Bye!"); return; }

                case 1 -> {
                    System.out.print("Student ID: "); String sid = sc.nextLine();
                    System.out.print("Name: ");       String sname = sc.nextLine();
                    System.out.print("Email: ");      String semail = sc.nextLine();
                    students.put(sid, new Student(sid, sname, semail));
                    System.out.println("Student added.");
                }

                case 2 -> {
                    System.out.print("Student ID: "); String sid = sc.nextLine();
                    System.out.print("Equipment ID: "); String eid = sc.nextLine();
                    Student st = students.get(sid);
                    if (st == null) { System.out.println("Student not found."); break; }
                    try {
                        Booking b = bookingService.issueEquipment(st, eid);
                        System.out.println("Booked! Due: " + b.getDueDate());
                    } catch (Exception e) { System.out.println("Error: " + e.getMessage()); }
                }

                case 3 -> {
                    System.out.print("Student ID: "); String sid = sc.nextLine();
                    Student st = students.get(sid);
                    if (st == null || st.getActiveBookings().isEmpty()) {
                        System.out.println("No active bookings."); break;
                    }
                    Booking b = st.getActiveBookings().get(0); // return first booking
                    double fine = bookingService.returnEquipment(st, b);
                    System.out.println("Returned. Fine: Rs." + fine);
                }

                case 4 -> {
                    System.out.print("Student ID: "); String sid = sc.nextLine();
                    Student st = students.get(sid);
                    if (st == null) { System.out.println("Not found."); break; }
                    bookingService.payFine(st, st.getPendingFine());
                    System.out.println("Fine cleared.");
                }

                case 5 -> inventory.printInventory();
            }
        }
    }
}
