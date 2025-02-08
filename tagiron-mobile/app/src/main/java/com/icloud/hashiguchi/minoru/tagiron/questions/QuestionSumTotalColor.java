package com.icloud.hashiguchi.minoru.tagiron.questions;

import java.util.Arrays;
import java.util.List;

import com.icloud.hashiguchi.minoru.tagiron.Tile;
import com.icloud.hashiguchi.minoru.tagiron.constants.Color;

public class QuestionSumTotalColor extends QuestionBase {

	private Color color;

	public QuestionSumTotalColor(Color type) {
		this.color = type;
		this.text = String.format("%sの数の合計は？", type.getKanji());
	}

	public Color getColor() {
		return color;
	}

	@Override
	public List<Integer> answer(List<Tile> opponentTiles) {
		int sum = opponentTiles.stream()
				.filter(t -> t.getColor() == color)
				.mapToInt(t -> t.getNo()).sum();
		return Arrays.asList(sum);
	}

	@Override
	public String getAnswerUnit() {
		return "";
	}
}
