package com.icloud.hashiguchi.minoru.tagiron.questions;

import com.icloud.hashiguchi.minoru.tagiron.TileViewModel;

import java.util.List;

public class QuestionHowPairNo extends QuestionBase {

    public QuestionHowPairNo() {
        this.text = "同じ数字タイルのペアは\n何組ある？";
    }

    @Override
    public String getSummaryText() {
        return "同じ数字ペアは何組？";
    }

    @Override
    public List<Integer> answer(List<TileViewModel> opponentTiles) {
        int answer = 0;

        // 5枚の数字タイルの重複排除した結果を基にペアの組数を判定する
        int count = (int) opponentTiles.stream()
                .map(t -> t.getNo())
                .distinct()
                .count();

        if (count == 3) {
            // 重複排除後が3枚の場合はペア2組
            answer = 2;
        } else if (count == 4) {
            // 重複排除後が4枚の場合はペア1組
            answer = 1;
        } else {
            // 上記以外はペアなし
            answer = 0;
        }
        return List.of(answer);
    }

    @Override
    public String getAnswerUnit() {
        return "組";
    }
}
