package com.icloud.hashiguchi.minoru.tagiron.questions;

import java.util.Arrays;
import java.util.List;

import com.icloud.hashiguchi.minoru.tagiron.Tile;
import com.icloud.hashiguchi.minoru.tagiron.utils.TileUtil;

public class QuestionHowOdd extends QuestionHowBase {

	public QuestionHowOdd() {
		this.text = "奇数は何枚ある？";
	}

	@Override
	public List<Integer> answer(List<Tile> opponentTiles) {

		// 奇数のタイルを探す
		int count = TileUtil.selectOddTiles(opponentTiles).size();

		// 回答をセット
		return Arrays.asList(count);
	}
}
