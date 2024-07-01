package org.swp.enums;

public enum NominationType {
    BAD(0),
    NORMAL(1),
    QUITE_GOOD(2),
    REALLY_GOOD(3);
    private int value;

    NominationType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
