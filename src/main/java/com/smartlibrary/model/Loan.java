package com.smartlibrary.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A loan links one student with one copy (association) and owns its renewal
 * history (composition, requirement R11). It protects the renewal rule:
 * the new due date must be strictly after the current due date.
 */
public class Loan {

    private final Student student;
    private final Copy copy;
    private final LocalDate loanDate;
    private LocalDate dueDate;
    private final List<Renewal> renewals = new ArrayList<>();

    public Loan(Student student, Copy copy, LocalDate loanDate, LocalDate dueDate) {
        if (student == null || copy == null || loanDate == null || dueDate == null) {
            throw new IllegalArgumentException("student, copy, loanDate and dueDate are required");
        }
        if (!dueDate.isAfter(loanDate)) {
            throw new IllegalArgumentException("due date must be after the loan date");
        }
        this.student = student;
        this.copy = copy;
        this.loanDate = loanDate;
        this.dueDate = dueDate;
    }

    /** Renews the loan; the renewal is registered with today's date. */
    public void renew(LocalDate newDueDate) {
        renew(newDueDate, LocalDate.now());
    }

    /**
     * Renews the loan. Atomic: every validation happens before any change,
     * so if an exception is thrown the loan state remains untouched.
     *
     * @throws IllegalArgumentException if a date is null or newDueDate is not
     *                                  strictly after the current due date
     */
    public void renew(LocalDate newDueDate, LocalDate renewalDate) {
        if (newDueDate == null || renewalDate == null) {
            throw new IllegalArgumentException("dates must not be null");
        }
        if (!newDueDate.isAfter(dueDate)) {
            throw new IllegalArgumentException("new due date " + newDueDate
                    + " must be after the current due date " + dueDate);
        }
        Renewal renewal = new Renewal(dueDate, newDueDate, renewalDate);
        renewals.add(renewal);
        dueDate = newDueDate;
    }

    public Student getStudent() {
        return student;
    }

    public Copy getCopy() {
        return copy;
    }

    public LocalDate getLoanDate() {
        return loanDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public int getRenewalCount() {
        return renewals.size();
    }

    /** Read-only view of the renewal history. */
    public List<Renewal> getRenewals() {
        return Collections.unmodifiableList(renewals);
    }
}
