package com.icloud.hashiguchi.minoru.tagiron.questions;

import com.icloud.hashiguchi.minoru.tagiron.Tile;
import com.icloud.hashiguchi.minoru.tagiron.constants.TotalValueType;
import com.icloud.hashiguchi.minoru.tagiron.utils.TileUtil;

import java.util.List;

public class QuestionSumTotalRange extends QuestionBase {

    private final TotalValueType totalValueType;

    public QuestionSumTotalRange(TotalValueType type) {
        this.totalValueType = type;
        this.text = String.format("%sの\n数の合計は？", type.getText());
    }

    public TotalValueType getTotalValueType() {
        return totalValueType;
    }

    @Override
    public List<Integer> answer(List<Tile> opponentTiles) {
        int sum = TileUtil.sumTileNumber(opponentTiles, this.totalValueType.getIndexes());
        return List.of(sum);
    }

    @Override
    public String getAnswerUnit() {
        return "";
    }
}
