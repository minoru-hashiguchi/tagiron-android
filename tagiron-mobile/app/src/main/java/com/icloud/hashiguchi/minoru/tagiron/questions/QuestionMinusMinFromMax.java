package com.icloud.hashiguchi.minoru.tagiron.questions;

import com.icloud.hashiguchi.minoru.tagiron.Tile;

import java.util.List;

public class QuestionMinusMinFromMax extends QuestionBase implements ShareableQuestion {

    public QuestionMinusMinFromMax() {
        this.text = this.getPrefixText("数字タイルの最大の数から、\n最小の数を引いた数は？");
    }

    @Override
    public String getSummaryText() {
        return "[共]最大ひく最小は？";
    }

    @Override
    public List<Integer> answer(List<Tile> opponentTiles) {
        int answer = opponentTiles.get(opponentTiles.size() - 1).getNo().getValue()
                - opponentTiles.get(0).getNo().getValue();
        return List.of(answer);
    }

    @Override
    public String getAnswerUnit() {
        return "";
    }

}
