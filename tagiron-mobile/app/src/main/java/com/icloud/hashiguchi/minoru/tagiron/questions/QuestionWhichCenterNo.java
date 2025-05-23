package com.icloud.hashiguchi.minoru.tagiron.questions;

import com.icloud.hashiguchi.minoru.tagiron.TileViewModel;

import java.util.List;

public class QuestionWhichCenterNo extends QuestionBase implements ShareableQuestion {

    public QuestionWhichCenterNo() {
        this.text = this.getPrefixText("中央の数字タイルは\n5以上？4以下？");
    }

    @Override
    public String getSummaryText() {
        return getShareableMark() + "中央は5以上？4以下？";
    }

    @Override
    public List<Integer> answer(List<TileViewModel> opponentTiles) {
        int answer = 0;
        if (opponentTiles.get(2).getNo().getValue() < 5) {
            answer = 4;
        } else {
            answer = 5;
        }
        return List.of(answer);
    }

    @Override
    public String getAnswerUnit() {
        if (answers.get(0) == 4) {
            return "以下";
        } else {
            return "以上";
        }
    }

    @Override
    public boolean isShared() {
        return isShared;
    }
}
