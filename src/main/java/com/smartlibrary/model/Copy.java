package com.smartlibrary.model;

/**
 * A physical copy of exactly one book (requirement R10).
 * The constructor is package-private so a copy can only be created by its Book.
 */
public class Copy {

    private final Book book;
    private final String inventoryCode;

    Copy(Book book, String inventoryCode) {
        if (book == null) {
            throw new IllegalArgumentException("a copy must belong to a book");
        }
        if (inventoryCode == null || inventoryCode.trim().isEmpty()) {
            throw new IllegalArgumentException("inventoryCode must not be null or blank");
        }
        this.book = book;
        this.inventoryCode = inventoryCode.trim();
    }

    public Book getBook() {
        return book;
    }

    public String getInventoryCode() {
        return inventoryCode;
    }
}
