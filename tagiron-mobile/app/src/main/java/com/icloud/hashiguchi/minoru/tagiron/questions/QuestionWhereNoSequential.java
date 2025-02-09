package com.icloud.hashiguchi.minoru.tagiron.questions;

import com.icloud.hashiguchi.minoru.tagiron.Tile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class QuestionWhereNoSequential extends QuestionBase {

    public QuestionWhereNoSequential() {
        this.text = "数が連続している\n数字タイルはどこ？";
    }

    @Override
    public String getSummaryText() {
        return "数が連続する場所は？";
    }

    @Override
    public List<Integer> answer(List<Tile> opponentTiles) {
        List<Integer> list = new ArrayList<>();
        int old = 0;
        for (int i = 0; i < opponentTiles.size(); i++) {
            try {
                if (i == 0) {
                    continue;
                }
                if (old + 1 == opponentTiles.get(i).getNo().getValue()) {
                    // 一つ前の数字と一つ差あれば両方のインデックスをリストに追加
                    list.add(i - 1);
                    list.add(i);
                }
            } finally {
                // 次の周期に行く前に今回の数字を退避
                old = opponentTiles.get(i).getNo().getValue();
            }
        }
        // 重複するインデックス番号を削除して返却
        return list.stream().distinct().collect(Collectors.toList());
    }

    @Override
    public String getAnswerUnit() {
        return "番目";
    }
}
