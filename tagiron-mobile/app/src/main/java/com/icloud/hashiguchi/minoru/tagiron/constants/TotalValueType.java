package com.icloud.hashiguchi.minoru.tagiron.constants;

public enum TotalValueType {
	LEFT("小さい方から3枚", 0, 1, 2),
	CENTER("中央の3枚", 1, 2, 3),
	RIGHT("大きい方から3枚", 2, 3, 4),
	ALL("数字タイル全て", 0, 1, 2, 3, 4),
	;

	TotalValueType(String text, int... indexes) {
		this.indexes = indexes;
		this.text = text;
	}

	private int[] indexes;
	private String text;

	public int[] getIndexes() {
		return this.indexes;
	}

	public String getText() {
		return text;
	}
}
