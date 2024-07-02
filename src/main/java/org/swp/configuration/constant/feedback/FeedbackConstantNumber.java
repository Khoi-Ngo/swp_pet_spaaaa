package org.swp.configuration.constant.feedback;

public enum FeedbackConstantNumber {
    NUMBER_OF_LATEST_FEEDBACK(4),
    NUMBER_OF_LATEST_FEEDBACK_REPLY(4);
    private final int value;

    FeedbackConstantNumber(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

}
