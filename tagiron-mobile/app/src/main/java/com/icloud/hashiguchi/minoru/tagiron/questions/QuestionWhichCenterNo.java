package com.icloud.hashiguchi.minoru.tagiron.questions;

import java.util.Arrays;
import java.util.List;

import com.icloud.hashiguchi.minoru.tagiron.Tile;

public class QuestionWhichCenterNo extends QuestionBase implements ShareableQuestion {

	public QuestionWhichCenterNo() {
		this.text = this.getPrefixText("中央の数字タイルは5以上？4以下？");
	}

	@Override
	public List<Integer> answer(List<Tile> opponentTiles) {
		int answer = 0;
		if (opponentTiles.get(2).getNo() < 5) {
			answer = 4;
		} else {
			answer = 5;
		}
		return Arrays.asList(answer);
	}

	@Override
	public String getAnswerUnit() {
		if (answers.getFirst() == 4) {
			return "以下";
		} else {
			return "以上";
		}
	}

}
