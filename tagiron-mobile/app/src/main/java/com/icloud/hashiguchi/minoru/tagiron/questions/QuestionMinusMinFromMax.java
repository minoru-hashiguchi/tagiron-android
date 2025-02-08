package com.icloud.hashiguchi.minoru.tagiron.questions;

import java.util.Arrays;
import java.util.List;

import com.icloud.hashiguchi.minoru.tagiron.Tile;

public class QuestionMinusMinFromMax extends QuestionBase implements ShareableQuestion {

	public QuestionMinusMinFromMax() {
		this.text = this.getPrefixText("数字タイルの最大の数から、最小の数を引いた数は？");
	}

	@Override
	public List<Integer> answer(List<Tile> opponentTiles) {
		int answer = opponentTiles.getLast().getNo() - opponentTiles.getFirst().getNo();
		return Arrays.asList(answer);
	}

	@Override
	public String getAnswerUnit() {
		return "";
	}

}
