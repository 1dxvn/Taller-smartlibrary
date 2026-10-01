package com.smartlibrary.model;

/**
 * A librarian is a library user who works at the library (requirement R12).
 * It does NOT implement Notifiable: R13 only says that SOME users receive
 * notifications, and no requirement asks the system to notify librarians.
 * If that changes, only "implements Notifiable" has to be added here.
 */
public class Librarian extends User {

    private final String employeeCode;
    private final String shift;

    public Librarian(String id, String name, String email,
                     String employeeCode, String shift) {
        super(id, name, email);
        this.employeeCode = requireText(employeeCode, "employeeCode");
        this.shift = requireText(shift, "shift");
    }

    public String getEmployeeCode() {
        return employeeCode;
    }

    public String getShift() {
        return shift;
    }
}
