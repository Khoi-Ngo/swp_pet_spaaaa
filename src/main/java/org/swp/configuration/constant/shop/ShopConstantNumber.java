package org.swp.configuration.constant.shop;

public enum ShopConstantNumber {

    NUMBER_OF_MOST_RCMD_SHOP(6);
    private final int value;

    ShopConstantNumber(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
