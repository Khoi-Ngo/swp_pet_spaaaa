package org.swp.enums;

public enum TypePet {
    // This enum refers to both service and pet/customer in BOOKING
    DOG("Dog"),
    CAT("Cat"),
    RAT("Rat");

    private final String value;

    // Constructor to assign the string value to the enum constant
    TypePet(String value) {
        this.value = value;
    }

    // Method to get the string value of the enum constant
    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }
}
