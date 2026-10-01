package com.smartlibrary;

import com.smartlibrary.model.Book;
import com.smartlibrary.model.Copy;
import com.smartlibrary.model.Librarian;
import com.smartlibrary.model.Loan;
import com.smartlibrary.model.Notifiable;
import com.smartlibrary.model.Renewal;
import com.smartlibrary.model.Student;
import com.smartlibrary.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/** Minimal tests for the SmartLibrary design (no external libraries needed). */
public class Main {

    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) {
        System.out.println("=== SmartLibrary - relationships and contracts ===");

        testValidRenewal();
        testInvalidRenewalEarlierDate();
        testInvalidRenewalEqualDate();
        testInvalidRenewalNullDate();
        testRenewalChain();
        testBookCopyComposition();
        testInheritanceAndContract();
        testRenewalHistoryIsReadOnly();

        System.out.println();
        System.out.println("Passed: " + passed + " | Failed: " + failed);
        System.exit(failed == 0 ? 0 : 1);
    }

    // ---------- helpers ----------

    private static void check(String description, boolean condition) {
        if (condition) {
            passed++;
            System.out.println("  [PASS] " + description);
        } else {
            failed++;
            System.out.println("  [FAIL] " + description);
        }
    }

    private static Student newStudent() {
        return new Student("1085000001", "Laura Gomez", "laura.gomez@university.edu",
                "20251001", "Software Engineering");
    }

    private static Loan newLoan(Student student) {
        Book book = new Book("978-0134685991", "Effective Java");
        Copy copy = book.addCopy("EJ-001");
        // Loan on 2026-10-01, due on 2026-10-08
        return new Loan(student, copy, LocalDate.of(2026, 10, 1), LocalDate.of(2026, 10, 8));
    }

    // ---------- tests ----------

    private static void testValidRenewal() {
        System.out.println("\nTest 1: valid renewal (new date after the current due date)");
        Student student = newStudent();
        Loan loan = newLoan(student);

        loan.renew(LocalDate.of(2026, 10, 15));
        student.receiveNotification("Your loan was renewed.");

        System.out.println("  New due date: " + loan.getDueDate());
        System.out.println("  Number of renewals: " + loan.getRenewalCount());
        Renewal r = loan.getRenewals().get(0);
        check("due date is 2026-10-15", loan.getDueDate().equals(LocalDate.of(2026, 10, 15)));
        check("one renewal is registered", loan.getRenewalCount() == 1);
        check("renewal keeps the previous date 2026-10-08",
                r.getPreviousDate().equals(LocalDate.of(2026, 10, 8)));
        check("renewal keeps the new date 2026-10-15",
                r.getNewDate().equals(LocalDate.of(2026, 10, 15)));
        check("renewal has the date it was made", r.getRenewalDate() != null);
    }

    private static void testInvalidRenewalEarlierDate() {
        System.out.println("\nTest 2: invalid renewal (date BEFORE the current due date)");
        Loan loan = newLoan(newStudent());
        LocalDate dueBefore = loan.getDueDate();
        int countBefore = loan.getRenewalCount();
        boolean thrown = false;
        try {
            loan.renew(LocalDate.of(2026, 10, 5));
        } catch (IllegalArgumentException e) {
            thrown = true;
            System.out.println("  Expected error: " + e.getMessage());
        }
        check("IllegalArgumentException is thrown", thrown);
        check("due date did not change", loan.getDueDate().equals(dueBefore));
        check("no renewal was registered", loan.getRenewalCount() == countBefore);
    }

    private static void testInvalidRenewalEqualDate() {
        System.out.println("\nTest 3: invalid renewal (date EQUAL to the current due date)");
        Loan loan = newLoan(newStudent());
        LocalDate dueBefore = loan.getDueDate();
        int countBefore = loan.getRenewalCount();
        boolean thrown = false;
        try {
            loan.renew(LocalDate.of(2026, 10, 8));
        } catch (IllegalArgumentException e) {
            thrown = true;
            System.out.println("  Expected error: " + e.getMessage());
        }
        check("IllegalArgumentException is thrown", thrown);
        check("due date did not change", loan.getDueDate().equals(dueBefore));
        check("no renewal was registered", loan.getRenewalCount() == countBefore);
    }

    private static void testInvalidRenewalNullDate() {
        System.out.println("\nTest 4: invalid renewal (null date)");
        Loan loan = newLoan(newStudent());
        boolean thrown = false;
        try {
            loan.renew(null);
        } catch (IllegalArgumentException e) {
            thrown = true;
            System.out.println("  Expected error: " + e.getMessage());
        }
        check("IllegalArgumentException is thrown", thrown);
        check("state is unchanged", loan.getRenewalCount() == 0
                && loan.getDueDate().equals(LocalDate.of(2026, 10, 8)));
    }

    private static void testRenewalChain() {
        System.out.println("\nTest 5: two renewals keep a chained history, and a failure in between changes nothing");
        Loan loan = newLoan(newStudent());
        loan.renew(LocalDate.of(2026, 10, 15));
        try {
            loan.renew(LocalDate.of(2026, 10, 15)); // equal to the current date -> invalid
        } catch (IllegalArgumentException e) {
            System.out.println("  Expected error: " + e.getMessage());
        }
        loan.renew(LocalDate.of(2026, 10, 22));
        check("two renewals registered", loan.getRenewalCount() == 2);
        check("second renewal starts where the first one ended",
                loan.getRenewals().get(1).getPreviousDate()
                        .equals(loan.getRenewals().get(0).getNewDate()));
        check("final due date is 2026-10-22", loan.getDueDate().equals(LocalDate.of(2026, 10, 22)));
    }

    private static void testBookCopyComposition() {
        System.out.println("\nTest 6: a book has several copies and each copy belongs to one book (R10)");
        Book book = new Book("978-0134685991", "Effective Java");
        Copy c1 = book.addCopy("EJ-001");
        Copy c2 = book.addCopy("EJ-002");
        check("the book has 2 copies", book.getCopies().size() == 2);
        check("each copy points to its only book", c1.getBook() == book && c2.getBook() == book);
    }

    private static void testInheritanceAndContract() {
        System.out.println("\nTest 7: Student and Librarian are Users; only Student is Notifiable (R12, R13)");
        Student student = newStudent();
        Librarian librarian = new Librarian("1085000002", "Carlos Ruiz",
                "carlos.ruiz@university.edu", "EMP-07", "Morning");

        List<User> users = new ArrayList<>();
        users.add(student);
        users.add(librarian);
        for (User user : users) {
            System.out.println("  User: " + user.getName() + " (" + user.getId() + ")");
        }
        check("both can be used as User", users.size() == 2);
        check("Student is Notifiable", student instanceof Notifiable);
        check("Librarian is not Notifiable", !(((Object) librarian) instanceof Notifiable));

        boolean thrown = false;
        try {
            student.receiveNotification("   ");
        } catch (IllegalArgumentException e) {
            thrown = true;
        }
        check("a blank notification is rejected", thrown);
    }

    private static void testRenewalHistoryIsReadOnly() {
        System.out.println("\nTest 8: the renewal history cannot be modified from outside the loan");
        Loan loan = newLoan(newStudent());
        loan.renew(LocalDate.of(2026, 10, 15));
        boolean thrown = false;
        try {
            loan.getRenewals().clear();
        } catch (UnsupportedOperationException e) {
            thrown = true;
        }
        check("getRenewals() is read-only", thrown);
        check("the renewal is still there", loan.getRenewalCount() == 1);
    }
}
