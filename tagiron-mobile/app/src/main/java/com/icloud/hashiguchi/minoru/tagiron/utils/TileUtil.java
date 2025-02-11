package com.icloud.hashiguchi.minoru.tagiron.utils;

import com.icloud.hashiguchi.minoru.tagiron.TileViewModel;
import com.icloud.hashiguchi.minoru.tagiron.constants.Color;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class TileUtil {

    private TileUtil() {
        // Do nothing.
    }

    /**
     * 奇数のタイルを抽出する
     *
     * @param collection 抽出元タイルのリスト
     * @return 結果
     */
    public static List<TileViewModel> selectOddTiles(Collection<TileViewModel> collection) {
        List<TileViewModel> results = collection.stream()
                .filter(t -> {
                    return t.getNo() != null && t.getNo().getValue() % 2 == 1;
                })
                .collect(Collectors.toList());
        return results;
    }

    /**
     * 0を含む偶数のタイルを抽出する
     *
     * @param collection 抽出元タイルのリスト
     * @return 結果
     */
    public static List<TileViewModel> selectEvenTiles(Collection<TileViewModel> collection) {
        List<TileViewModel> results = collection.stream()
                .filter(t -> {
                    return t.getNo() != null && t.getNo().getValue() % 2 == 0;
                })
                .collect(Collectors.toList());
        return results;
    }

    /**
     * 指定色のタイルを抽出する
     *
     * @param tiles 抽出元タイルのリスト
     * @return 結果
     */
    public static List<TileViewModel> selectOneColorTiles(List<TileViewModel> tiles, Color color) {
        List<TileViewModel> results = tiles.stream()
                .filter(t -> {
                    return t.getColor() != null && t.getColor().getValue() == color;
                })
                .collect(Collectors.toList());
        return results;
    }

    /**
     * 指定場所のタイルの数字を合算する
     *
     * @param tiles
     * @param indexes
     * @return
     */
    public static int sumTileNumber(List<TileViewModel> tiles, int... indexes) {
        int sum = 0;
        for (int index : indexes) {
            Integer val = tiles.get(index).getNo().getValue();
            if (val != null) {
                sum += val;
            }
        }
        return sum;
    }
}
