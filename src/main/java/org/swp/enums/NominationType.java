package org.swp.enums;

public enum NominationType {
    BAD(0),
    NORMAL(1),
    QUITE_GOOD(2),
    REALLY_GOOD(3);

    private final int value;

    NominationType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static NominationType fromValue(int value) {
        for (NominationType type : NominationType.values()) {
            if (type.getValue() == value) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid nomination type value: " + value);
    }
}
