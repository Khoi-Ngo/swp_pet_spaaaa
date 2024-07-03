package org.swp.enums;

public enum UserRole {
    ADMIN(0),
    CUSTOMER(1),
    SHOP_OWNER(2);
    private int value;

    UserRole(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
