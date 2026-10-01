package com.smartlibrary.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A book is a title of the catalog (requirement R10).
 * Composition with Copy: copies are created only through the book
 * (addCopy) and never exist without it. Assumption: a copy cannot be moved to
 * another book, and retiring a title from the catalog retires its copies.
 */
public class Book {

    private final String isbn;
    private final String title;
    private final List<Copy> copies = new ArrayList<>();

    public Book(String isbn, String title) {
        if (isbn == null || isbn.trim().isEmpty() || title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("isbn and title must not be null or blank");
        }
        this.isbn = isbn.trim();
        this.title = title.trim();
    }

    /** Registers a new physical copy of this book and returns it. */
    public Copy addCopy(String inventoryCode) {
        Copy copy = new Copy(this, inventoryCode);
        copies.add(copy);
        return copy;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getTitle() {
        return title;
    }

    /** Read-only view: copies can only be added through addCopy. */
    public List<Copy> getCopies() {
        return Collections.unmodifiableList(copies);
    }
}
