package com.icloud.hashiguchi.minoru.tagiron.questions;

import com.icloud.hashiguchi.minoru.tagiron.TileViewModel;
import com.icloud.hashiguchi.minoru.tagiron.constants.Color;

import java.util.List;

public class QuestionSumTotalColor extends QuestionBase {

    private final Color color;

    public QuestionSumTotalColor(Color type) {
        this.color = type;
        this.text = String.format("%sの数の合計は？", type.getKanji());
    }

    public Color getColor() {
        return color;
    }

    @Override
    public String getSummaryText() {
        return String.format("%sの数の合計は？", this.color.getKanji());
    }

    @Override
    public List<Integer> answer(List<TileViewModel> opponentTiles) {
        int sum = opponentTiles.stream()
                .filter(t -> t.getColor().getValue() == color)
                .mapToInt(t -> t.getNo().getValue()).sum();
        return List.of(sum);
    }

    @Override
    public String getAnswerUnit() {
        return "";
    }
}
