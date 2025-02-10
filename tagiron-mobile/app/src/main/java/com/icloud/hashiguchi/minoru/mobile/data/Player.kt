package com.icloud.hashiguchi.minoru.mobile.data

import android.util.Log
import com.icloud.hashiguchi.minoru.tagiron.Tile
import com.icloud.hashiguchi.minoru.tagiron.constants.Color
import com.icloud.hashiguchi.minoru.tagiron.constants.Constant
import com.icloud.hashiguchi.minoru.tagiron.questions.QuestionBase
import com.icloud.hashiguchi.minoru.tagiron.questions.QuestionWhereNoBySelect
import java.util.Arrays
import java.util.function.Consumer
import java.util.stream.Collectors

abstract class Player {
    protected var name: String? = null
    protected var ownTiles: MutableList<Tile> = mutableListOf()
    protected var markingTiles: MutableList<Tile?> = mutableListOf()
    protected var questionsAndAnswers: MutableList<QuestionBase> = mutableListOf()
    protected var patterns: MutableList<Array<Tile>> = mutableListOf()

    /**
     * コンストラクタ
     * @param type
     * @param name
     */
    protected fun PlayerBase(name: String?) {
        this.name = name
        //手札候補の初期化（ディープコピー）
        for (tile in Constant.TILES) {
            markingTiles.add(tile.clone())
        }
    }

    /**
     * 質問か宣言か、行動を選択する
     *
     * @param questions
     * @return 行動が質問の場合は質問番号、宣言の場合はnull
     */
    abstract fun selectAction(questions: MutableList<QuestionBase?>?): Int?

    /**
     * 宣言する
     *
     * @param yourTiles
     * @return true: 正解、false: 不正解
     */
    abstract fun call(yourTiles: MutableList<Tile?>?): Boolean

    /**
     * 指定した質問の数字を選択する
     *
     * @param question 数字選択式の質問カード
     * @return 選択した数字
     */
    abstract fun selectNumber(question: QuestionWhereNoBySelect?): Int

    /**
     * ゲーム開始時に数字タイルを5枚、手札に追加し並べ替える
     *
     * @param fieldTiles ランダムに並んだ山札のタイル
     */
    fun pickTiles(fieldTiles: MutableList<Tile?>) {
        for (i in 0..4) {
            val picked: Tile = fieldTiles.get(0)!!
            ownTiles.add(picked)
            fieldTiles.removeAt(0)
        }
        // 手札の並べ替え
        ownTiles.sortWith(compareBy<Tile> { it.no.value }.thenBy { it.color.value })

        printMyTiles()
    }

    fun think(q: QuestionBase) {
        if (q.deletePatterns(patterns)) {
//            Log.println(Log.INFO, "LOG", "特定！！ -> {}", Arrays.toString(patterns.getFirst()))
        }
    }

    /**
     * 場の質問カードから、指定した場所の質問カードを取り出す
     *
     * @param pickedIndex 取り出す質問カードの場所(0~n)
     * @param questions 6枚以下の質問カード
     * @return 選択した質問カード
     */
    fun pickQuestion(pickedIndex: Int, questions: MutableList<QuestionBase?>): QuestionBase? {
        val picked = questions[pickedIndex]
        questions.removeAt(pickedIndex)
        return picked
    }

    /**
     * 質問する
     *
     * @param yourTiles 相手手札のタイル
     * @param picked 自身が選択した質問カード
     */
    fun askQuestion(yourTiles: MutableList<Tile?>?, picked: QuestionBase) {
        // 質問時に数値を選択する必要がある場合
        if (picked is QuestionWhereNoBySelect) {
            val q = picked
            val selected = selectNumber(q)
            q.selectedNo = selected
        }

        // 相手プレイヤーが質問に回答する
        val answers = picked.answer(yourTiles)
        picked.answers = answers
        picked.printAll()

        // 相手プレイヤーの回答を元に思考する
        think(picked)
        questionsAndAnswers.add(picked)
    }

    /**
     * 手札のタイルを出力する
     */
    fun printMyTiles() {
        Log.println(Log.INFO, "LOG", "-- " + name + "の手札 --")
        // カンマ区切りで出力
        val str: String = ownTiles.stream()
            .map<Any> { v: Tile -> v.toString() }
            .collect(Collectors.toList())
            .joinToString(", ")
        Log.println(Log.INFO, "LOG", str)
    }

    /**
     * これまでの質問カードとその回答を出力する<br></br>
     * 相手の共有情報カードも含む
     */
    fun printQuestionsAnswers() {
        Log.println(Log.INFO, "LOG", "-- 質問とその回答 --")
        questionsAndAnswers.forEach(Consumer { a: QuestionBase -> a.printAll() })
    }

    /**
     * 指定したパターン数以下の場合、手札の予想パターンを出力する
     *
     * @param size 予想手札パターン数の閾値
     */
    fun printPatterns(size: Int) {
        Log.println(Log.INFO, "LOG", "候補件数 -> " + patterns.size)
        if (patterns.size <= size) {
            patterns.forEach(Consumer<Array<Tile>> { p: Array<Tile>? ->
                Log.println(Log.INFO, "LOG", Arrays.toString(p))
            })
        }
    }

    /**
     * 初回思考
     * 相手手札候補から自分の手札と一致するものを除外する。
     */
    fun markOnFirst() {
        // 自身の手札と同じ(参照が同じ)タイルは除外
        val baseTiles: List<Tile> = Constant.TILES.toMutableList().filter {
            tile -> !ownTiles.any { it === tile }
        }

        for (t1 in baseTiles) {

            for (t2 in baseTiles) {
                if (t1 === t2 || t1.greaterThan(t2) ||
                    (t1.no == t2.no && t1.match(Color.BLUE) && t2.match(Color.RED))
                ) {
                    continue
                }

                for (t3 in baseTiles) {
                    if (t1 === t3 || t2 === t3 || t1.greaterThan(t3) || t2.greaterThan(t3) ||
                        (t2.no == t3.no && t2.match(Color.BLUE) && t3.match(Color.RED))
                    ) {
                        continue
                    }

                    for (t4 in baseTiles) {
                        if (t1 === t4 || t2 === t4 || t3 === t4 ||
                            t1.greaterThan(t4) || t2.greaterThan(t4) || t3.greaterThan(t4) ||
                            (t3.no == t4.no && t3.match(Color.BLUE) && t4.match(Color.RED))
                        ) {
                            continue
                        }

                        for (t5 in baseTiles) {
                        for (t5 in baseTiles) {
                            if (t1 === t5 || t2 === t5 || t3 === t5 || t4 === t5 ||
                                t1.greaterThan(t5) || t2.greaterThan(t5) || t3.greaterThan(t5) || t4.greaterThan(t5) ||
                                (t4.no === t5.no && t4.match(Color.BLUE) && t5.match(Color.RED))
                            ) {
                                continue
                            }
                            val tiles: Array<Tile> = arrayOf<Tile>(t1, t2, t3, t4, t5)
                            patterns.add(tiles)
                        }
                    }
                }
            }
        }

        /* ----------------------------------------------------------
		 *  パターンリストを多重ループで作成する都合上、
		 *  同色同数字で2枚存在するY5を含むパターンに重複が発生してしまう。
		 *  下記にてArrays.equalsを用いて自力で重複削除する必要がある。
		 * ---------------------------------------------------------- */
        Log.println(Log.DEBUG, "LOG", "候補件数[重複排除前] => " + patterns.size)
        val tmpList: MutableList<Array<Tile>> = ArrayList<Array<Tile>>()
        for (x in patterns) {
            val count = tmpList.stream().filter { y: Array<Tile>? -> Arrays.equals(x, y) }.count()
            if (count == 0L) {
                tmpList.add(x)
            }
        }
        patterns = tmpList
        Log.println(Log.DEBUG, "LOG", "候補件数[重複排除後] => " + patterns.size)
    }

//    /**
//     * 指定した数字と色に合致する手札候補に一つマークする
//     *
//     * @param mark マーク
//     * @param number タイル数字
//     * @param color タイル色
//     */
//    fun setMarkingTiles(mark: Mark, number: Int, color: Color, clz: Class<*>, step: Int) {
//        val searched: Tile? = markingTiles.stream()
//            .filter { t: Tile? -> t.getMark() === Mark.NONE && t.no === number && t.color === color }
//            .findFirst().orElse(null)
//        if (searched != null) {
//            if (D && mark != searched.getMark()) {
//                Log.println(
//                    Log.DEBUG, "LOG",
//                    java.lang.String.format(
//                        "marked [%s] %s%s ...%s #%s",
//                        mark.toString(), color, number, clz.simpleName, step
//                    )
//                )
//            }
//            // 検索結果を手札候補にマーク
//            searched.setMark(mark)
//        }
//    }

    /**
     * プレイヤー名を取得する
     * @return
     */
    fun getName(): String? {
        return name
    }

    /**
     * 自身の手札タイルを取得する
     * @return
     */
    fun getOwnTiles(): MutableList<Tile> {
        return ownTiles
    }
}