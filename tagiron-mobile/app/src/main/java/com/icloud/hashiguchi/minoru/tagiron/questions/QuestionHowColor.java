package com.icloud.hashiguchi.minoru.tagiron.questions;

import java.util.Arrays;
import java.util.List;

import com.icloud.hashiguchi.minoru.tagiron.Tile;
import com.icloud.hashiguchi.minoru.tagiron.constants.Color;
import com.icloud.hashiguchi.minoru.tagiron.utils.TileUtil;

public class QuestionHowColor extends QuestionHowBase {

	protected Color specifidColor = null;

	public QuestionHowColor(Color color) {
		this.specifidColor = color;
		this.text = String.format("%sの数字タイルは何枚ある？", color.getKanji());
	}

	@Override
	public List<Integer> answer(List<Tile> opponentTiles) {

		// 同色のタイルを探す
		int count = TileUtil.selectOneColorTiles(opponentTiles, this.specifidColor).size();
		return Arrays.asList(count);
	}
}
