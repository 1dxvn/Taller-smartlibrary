package com.smartlibrary.model;

/**
 * A student is a library user who can borrow copies and receive notifications
 * (requirements R12 and R13).
 */
public class Student extends User implements Notifiable {

    private final String studentCode;
    private final String academicProgram;

    public Student(String id, String name, String email,
                   String studentCode, String academicProgram) {
        super(id, name, email);
        this.studentCode = requireText(studentCode, "studentCode");
        this.academicProgram = requireText(academicProgram, "academicProgram");
    }

    public String getStudentCode() {
        return studentCode;
    }

    public String getAcademicProgram() {
        return academicProgram;
    }

    /** Simulates the delivery of the notification by printing it to the console. */
    @Override
    public void receiveNotification(String message) {
        requireText(message, "message");
        System.out.println("[NOTIFICATION] To " + getName() + " <" + getEmail() + ">: " + message);
    }
}
