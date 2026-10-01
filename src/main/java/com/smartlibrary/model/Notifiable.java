package com.smartlibrary.model;

/**
 * Behavior contract (requirement R13): an object that implements this interface
 * promises it can receive a text notification from the system.
 * The contract does NOT say how, when or through which channel it is delivered.
 */
public interface Notifiable {

    /**
     * Receives a notification message.
     *
     * @param message the text to deliver; must not be null or blank
     * @throws IllegalArgumentException if the message is null or blank
     */
    void receiveNotification(String message);
}
