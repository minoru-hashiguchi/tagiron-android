package com.icloud.hashiguchi.minoru.tagiron.questions;

import com.icloud.hashiguchi.minoru.tagiron.Tile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QuestionWhereNo extends QuestionBase {
    protected int[] selectNumbers = new int[]{};

    public QuestionWhereNo(int... selectNumbers) {
        this.selectNumbers = selectNumbers;
        if (selectNumbers.length < 1) {
            throw new RuntimeException("パラメータ設定エラー：" + Arrays.toString(selectNumbers));
        } else {
            this.selectedNo = selectNumbers[0];
            this.text = String.format("%sはどこ？", selectNumbers[0]);
        }
    }

    public int[] getSelectNumbers() {
        return selectNumbers;
    }

    @Override
    public String getSummaryText() {
        return String.format("%sはどこ？", selectedNo);
    }

    @Override
    public List<Integer> answer(List<Tile> opponentTiles) {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < opponentTiles.size(); i++) {
            if (opponentTiles.get(i).getNo().getValue() == this.selectedNo) {
                // 見つかったら回答(指定Noの場所)を設定
                list.add(i);
            }
        }
        return list;
    }

    @Override
    public String getAnswerUnit() {
        return "番目";
    }
}
