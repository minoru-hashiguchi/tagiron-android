package com.icloud.hashiguchi.minoru.tagiron.questions;

import java.util.Arrays;
import java.util.List;

import com.icloud.hashiguchi.minoru.tagiron.Tile;
import com.icloud.hashiguchi.minoru.tagiron.constants.TotalValueType;
import com.icloud.hashiguchi.minoru.tagiron.utils.TileUtil;

public class QuestionSumTotalRange extends QuestionBase {

	private TotalValueType totalValueType;

	public QuestionSumTotalRange(TotalValueType type) {
		this.totalValueType = type;
		this.text = String.format("%sの数の合計は？", type.getText());
	}

	public TotalValueType getTotalValueType() {
		return totalValueType;
	}

	@Override
	public List<Integer> answer(List<Tile> opponentTiles) {
		int sum = TileUtil.sumTileNumber(opponentTiles, this.totalValueType.getIndexes());
		return Arrays.asList(sum);
	}

	@Override
	public String getAnswerUnit() {
		return "";
	}
}
