package org.swp.enums;

public enum BookingStatus {
    WAIT_FOR_SHOP_OWNER_ACCEPT("Waiting for shop owner to accept"),
    WAIT_FOR_CUSTOMER_ACCEPT("Waiting for customer to accept"),
    ACCEPTED("Booking accepted"),
    CANCELLED("Booking cancelled"),
    DONE("Booking done"),
    REJECTED("Booking rejected");

    private final String description;

    BookingStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}

