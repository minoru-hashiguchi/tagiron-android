package com.icloud.hashiguchi.minoru.tagiron.constants;

public enum TotalValueType {
    LEFT("小さい方から3枚", "左3枚", 0, 1, 2),
    CENTER("中央の3枚", "中央3枚", 1, 2, 3),
    RIGHT("大きい方から3枚", "右3枚", 2, 3, 4),
    ALL("数字タイル全て", "全て", 0, 1, 2, 3, 4),
    ;

    private final int[] indexes;
    private final String text;
    private final String summaryText;

    TotalValueType(String text, String summaryText, int... indexes) {
        this.indexes = indexes;
        this.text = text;
        this.summaryText = summaryText;
    }

    public int[] getIndexes() {
        return this.indexes;
    }

    public String getText() {
        return text;
    }

    public String getSummaryText() {
        return summaryText;
    }
}
