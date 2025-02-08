package com.icloud.hashiguchi.minoru.tagiron.utils;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import com.icloud.hashiguchi.minoru.tagiron.Tile;
import com.icloud.hashiguchi.minoru.tagiron.constants.Color;

public class TileUtil {

	private TileUtil() {
		// Do nothing.
	}

	/**
	 * 奇数のタイルを抽出する
	 * @param collection 抽出元タイルのリスト
	 * @return 結果
	 */
	public static List<Tile> selectOddTiles(Collection<Tile> collection) {
		List<Tile> results = collection.stream()
				.filter(t -> {
					return t.getNo() != null && t.getNo() % 2 == 1;
				})
				.collect(Collectors.toList());
		return results;
	}

	/**
	 * 0を含む偶数のタイルを抽出する
	 * @param collection 抽出元タイルのリスト
	 * @return 結果
	 */
	public static List<Tile> selectEvenTiles(Collection<Tile> collection) {
		List<Tile> results = collection.stream()
				.filter(t -> {
					return t.getNo() != null && t.getNo() % 2 == 0;
				})
				.collect(Collectors.toList());
		return results;
	}

	/**
	 * 指定色のタイルを抽出する
	 * @param tiles 抽出元タイルのリスト
	 * @return 結果
	 */
	public static List<Tile> selectOneColorTiles(List<Tile> tiles, Color color) {
		List<Tile> results = tiles.stream()
				.filter(t -> {
					return t.getColor() != null && t.getColor() == color;
				})
				.collect(Collectors.toList());
		return results;
	}

	/**
	 * 指定場所のタイルの数字を合算する
	 * @param tiles
	 * @param indexes
	 * @return
	 */
	public static int sumTileNumber(List<Tile> tiles, int... indexes) {
		int sum = 0;
		for (int index : indexes) {
			Integer val = tiles.get(index).getNo();
			if (val != null) {
				sum += val;
			}
		}
		return sum;
	}
}
