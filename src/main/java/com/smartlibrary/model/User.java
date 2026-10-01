package com.smartlibrary.model;

/**
 * Abstraction of a library user (requirement R12).
 * Students and librarians ARE library users; they share the identity data
 * (id, name, email) and each one adds its own particular information.
 */
public abstract class User {

    private final String id;
    private final String name;
    private final String email;

    protected User(String id, String name, String email) {
        this.id = requireText(id, "id");
        this.name = requireText(name, "name");
        this.email = requireText(email, "email");
    }

    /** Validates that a text value is neither null nor blank. */
    protected static String requireText(String value, String field) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(field + " must not be null or blank");
        }
        return value.trim();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
}
