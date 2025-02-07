package com.icloud.hashiguchi.tagironmobile.constants;

/**
 * [ ] PLAIN		... 存在する可能性がある。ほぼ未検討</br>
 * [x] IGNORE		... 存在しないため除外</br>
 * [o] CONTAINS		... 存在するで確定</br>
 */
public enum Mark {
    UNKNOWN(" "),
    IGNORE("x"),
    CONTAINS("o"),
    ;

    private final String signal;

    Mark(String signal) {
        this.signal = signal;
    }

    @Override
    public String toString() {
        return signal;
    }

    public Mark toggle() {
        if (this == IGNORE) {
            return CONTAINS;
        }
        if (this == CONTAINS) {
            return IGNORE;
        }
        throw new IllegalArgumentException("Unexpected value: " + this);
    }

    public boolean isConfirmed() {
        return this == IGNORE || this == CONTAINS;
    }

    public String getSignal() {
        return signal;
    }
}
