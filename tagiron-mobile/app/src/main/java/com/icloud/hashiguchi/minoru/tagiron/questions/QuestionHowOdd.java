package com.icloud.hashiguchi.minoru.tagiron.questions;

import com.icloud.hashiguchi.minoru.tagiron.TileViewModel;
import com.icloud.hashiguchi.minoru.tagiron.utils.TileUtil;

import java.util.List;

public class QuestionHowOdd extends QuestionHowBase {

    public QuestionHowOdd() {
        this.text = "奇数は何枚ある？";
    }

    @Override
    public String getSummaryText() {
        return "奇数は何枚？";
    }

    @Override
    public List<Integer> answer(List<TileViewModel> opponentTiles) {

        // 奇数のタイルを探す
        int count = TileUtil.selectOddTiles(opponentTiles).size();

        // 回答をセット
        return List.of(count);
    }
}
