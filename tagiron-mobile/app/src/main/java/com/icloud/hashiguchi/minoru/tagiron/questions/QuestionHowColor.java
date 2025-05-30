package com.icloud.hashiguchi.minoru.tagiron.questions;

import com.icloud.hashiguchi.minoru.tagiron.TileViewModel;
import com.icloud.hashiguchi.minoru.tagiron.constants.Color;
import com.icloud.hashiguchi.minoru.tagiron.utils.TileUtil;

import java.util.List;

public class QuestionHowColor extends QuestionHowBase {

    protected Color specifidColor = null;

    public QuestionHowColor(Color color) {
        this.specifidColor = color;
        this.text = String.format("%sの数字タイルは\n何枚ある？", color.getKanji());
    }

    @Override
    public String getSummaryText() {
        return String.format("%sは何枚？", this.specifidColor.getKanji());
    }

    @Override
    public List<Integer> answer(List<TileViewModel> opponentTiles) {

        // 同色のタイルを探す
        int count = TileUtil.selectOneColorTiles(opponentTiles, this.specifidColor).size();
        return List.of(count);
    }
}
