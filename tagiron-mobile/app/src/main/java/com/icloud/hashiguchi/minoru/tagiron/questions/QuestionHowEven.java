package com.icloud.hashiguchi.minoru.tagiron.questions;

import com.icloud.hashiguchi.minoru.tagiron.Tile;
import com.icloud.hashiguchi.minoru.tagiron.utils.TileUtil;

import java.util.List;

public class QuestionHowEven extends QuestionHowBase {

    public QuestionHowEven() {
        this.text = "偶数は何枚ある？\n（0も含む）";
    }

    @Override
    public List<Integer> answer(List<Tile> opponentTiles) {

        // 0含む偶数のタイルを探す
        int count = TileUtil.selectEvenTiles(opponentTiles).size();

        // 回答をセット
        return List.of(count);
    }
}
