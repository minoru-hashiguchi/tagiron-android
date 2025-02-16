package com.icloud.hashiguchi.minoru.tagiron.questions;

import com.icloud.hashiguchi.minoru.tagiron.TileViewModel;
import com.icloud.hashiguchi.minoru.tagiron.constants.TotalValueType;
import com.icloud.hashiguchi.minoru.tagiron.utils.TileUtil;

import java.util.List;

public class QuestionSumTotalRange extends QuestionBase {

    private final TotalValueType totalValueType;

    public QuestionSumTotalRange(TotalValueType type) {
        this.totalValueType = type;
        this.text = String.format("%sの\n数の合計は？", type.getText());
    }

    @Override
    public String getSummaryText() {
        return String.format("%sの合計は？", this.totalValueType.getSummaryText());
    }

    @Override
    public List<Integer> answer(List<TileViewModel> opponentTiles) {
        int sum = TileUtil.sumTileNumber(opponentTiles, this.totalValueType.getIndexes());
        return List.of(sum);
    }

    @Override
    public String getAnswerUnit() {
        return "";
    }
}
