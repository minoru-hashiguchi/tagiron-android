package com.icloud.hashiguchi.minoru.tagiron.questions;

import com.icloud.hashiguchi.minoru.tagiron.TileViewModel;
import com.icloud.hashiguchi.minoru.tagiron.constants.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class QuestionWhereNoSameColor extends QuestionWhereBase {
    public QuestionWhereNoSameColor() {
        this.text = "同じ色がとなり合っている\n数字タイルはどこ？";
    }

    @Override
    public String getSummaryText() {
        return "同色が隣り合う場所は？";
    }

    @Override
    public List<Integer> answer(List<TileViewModel> opponentTiles) {
        List<Integer> list = new ArrayList<>();
        Color old = null;
        for (int i = 0; i < opponentTiles.size(); i++) {
            try {
                if (i == 0) {
                    continue;
                }
                if (old == opponentTiles.get(i).getColor().getValue()) {
                    // 一つ前の色と同じであれば両方のインデックスをリストに追加
                    list.add(i - 1);
                    list.add(i);
                }
            } finally {
                // 次の周期に行く前に今回のタイルカラーを退避
                old = opponentTiles.get(i).getColor().getValue();
            }
        }
        // 重複するインデックス番号を削除して返却
        return list.stream().distinct().collect(Collectors.toList());
    }
}
