package com.smartlibrary.model;

import java.time.LocalDate;

/**
 * Record of one renewal of a loan (requirement R11): it keeps the previous due
 * date, the new due date and the date on which the renewal was made.
 * It is immutable and only a Loan can create it (package-private constructor).
 */
public class Renewal {

    private final LocalDate previousDate;
    private final LocalDate newDate;
    private final LocalDate renewalDate;

    Renewal(LocalDate previousDate, LocalDate newDate, LocalDate renewalDate) {
        this.previousDate = previousDate;
        this.newDate = newDate;
        this.renewalDate = renewalDate;
    }

    public LocalDate getPreviousDate() {
        return previousDate;
    }

    public LocalDate getNewDate() {
        return newDate;
    }

    public LocalDate getRenewalDate() {
        return renewalDate;
    }

    @Override
    public String toString() {
        return "Renewal{previous=" + previousDate + ", new=" + newDate
                + ", madeOn=" + renewalDate + "}";
    }
}
