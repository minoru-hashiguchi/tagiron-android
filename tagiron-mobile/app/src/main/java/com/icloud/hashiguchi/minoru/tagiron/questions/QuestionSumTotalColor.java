package com.icloud.hashiguchi.minoru.tagiron.questions;

import com.icloud.hashiguchi.minoru.tagiron.Tile;
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
    public List<Integer> answer(List<Tile> opponentTiles) {
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
