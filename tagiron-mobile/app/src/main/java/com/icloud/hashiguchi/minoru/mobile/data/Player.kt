package com.icloud.hashiguchi.minoru.mobile.data

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.icloud.hashiguchi.minoru.mobile.utils.Logger
import com.icloud.hashiguchi.minoru.tagiron.TileViewModel
import com.icloud.hashiguchi.minoru.tagiron.constants.Color
import com.icloud.hashiguchi.minoru.tagiron.constants.Constant
import com.icloud.hashiguchi.minoru.tagiron.questions.QuestionBase
import com.icloud.hashiguchi.minoru.tagiron.questions.QuestionWhereNoBySelect
import java.util.Arrays
import java.util.Objects
import java.util.function.Consumer
import java.util.stream.Collectors

abstract class Player(open var name: String) {
    var ownTiles: MutableLiveData<MutableList<TileViewModel>> =
        MutableLiveData<MutableList<TileViewModel>>(mutableListOf())
        protected set
    var actionHistory: MutableLiveData<MutableList<ActionItem>> =
        MutableLiveData<MutableList<ActionItem>>(mutableListOf())
        protected set
    var patterns: MutableSet<Array<TileViewModel>> = mutableSetOf()
        protected set
    var isCreatedPatterns = MutableLiveData(false)

    companion object {
        const val D = Constant.D
    }

    /**
     * 質問か宣言か、行動を選択する
     *
     * @param questions
     * @return 行動が質問の場合は質問番号、宣言の場合はnull
     */
    abstract fun selectAction(questions: MutableList<QuestionBase>): Int?

    /**
     * 宣言する
     *
     * @return true: 正解、false: 不正解
     */
    abstract fun call(): Array<TileViewModel>

    /**
     * 指定した質問の数字を選択する
     *
     * @param question 数字選択式の質問カード
     * @return 選択した数字
     */
    abstract fun selectNumber(question: QuestionWhereNoBySelect): Int

    /**
     * ゲーム開始時に数字タイルを5枚、手札に追加し並べ替える
     *
     * @param fieldTiles ランダムに並んだ山札のタイル
     */
    fun pickTilesAfterThatSort(fieldTiles: MutableList<TileViewModel>) {
        for (i in 0..4) {
            val picked: TileViewModel = fieldTiles.get(0)
            ownTiles.value?.add(picked)
            fieldTiles.removeAt(0)
        }
        // 手札の並べ替え
        ownTiles.value?.sortWith(compareBy<TileViewModel> { it.no.value }.thenBy { it.color.value })

        printMyTiles()
    }

    fun deletePatterns(q: QuestionBase): Boolean {
        val oldSize = patterns.size
        val itr = patterns.iterator()
        while (itr.hasNext()) {
            val tiles = Arrays.stream(itr.next()).collect(Collectors.toList())
            val verify: List<Int> = q.answer(tiles)
            val notMatch = !Objects.deepEquals(q.answers, verify)
            if (notMatch) {
                itr.remove()
            }
        }

        Log.println(
            Log.DEBUG, Constant.LOG_TAG + "[${name}]",
            "patterns count: ${oldSize} -> ${patterns.size} "
        )
        if (patterns.size <= 10) {
            patterns.forEach { Logger.d(Constant.LOG_TAG, Arrays.toString(it)) }
        }
        return patterns.size == 1
    }

    /**
     * 場の質問カードから、指定した場所の質問カードを取り出す
     *
     * @param pickedIndex 取り出す質問カードの場所(0~n)
     * @param questions 6枚以下の質問カード
     * @return 選択した質問カード
     */
    fun pickQuestion(
        pickedIndex: Int,
        questions: MutableLiveData<MutableList<QuestionBase>>
    ): QuestionBase {
        val picked = questions.value?.get(pickedIndex)
        questions.value?.removeAt(pickedIndex)
        questions.postValue(questions.value)
        return picked!!
    }

    /**
     * 質問する
     *
     * @param yourTiles 相手手札のタイル
     * @param picked 自身が選択した質問カード
     */
    fun askQuestion(yourTiles: MutableList<TileViewModel>, picked: QuestionBase) {
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
        deletePatterns(picked)
        actionHistory.value?.add(ActionItem(picked, patterns.size))
        actionHistory.postValue(actionHistory.value)
    }

    /**
     * 手札のタイルを出力する
     */
    fun printMyTiles() {
        Log.println(Log.INFO, Constant.LOG_TAG + "[${name}]", "-- 手札 --")
        // カンマ区切りで出力
        val str: String = ownTiles.value!!.stream()
            .map<Any> { v: TileViewModel -> v.toString() }
            .collect(Collectors.toList())
            .joinToString(", ")
        Log.println(Log.INFO, Constant.LOG_TAG + "[${name}]", str)
    }

    /**
     * 指定したパターン数以下の場合、手札の予想パターンを出力する
     *
     * @param size 予想手札パターン数の閾値
     */
    fun printPatterns(size: Int) {
        Log.println(Log.INFO, Constant.LOG_TAG + "[${name}]", "候補件数 -> " + patterns.size)
        if (patterns.size <= size) {
            patterns.forEach(Consumer<Array<TileViewModel>> { p: Array<TileViewModel> ->
                Log.println(Log.INFO, Constant.LOG_TAG + "[${name}]", Arrays.toString(p))
            })
        }
    }

    /**
     * 初回思考
     * 相手手札候補から自分の手札と一致するものを除外する。
     */
    fun createTilePattern() {
        val startTime = System.currentTimeMillis() // 処理開始時間を取得

        var patternsMap: HashMap<String, Array<TileViewModel>> = hashMapOf()
        // 自身の手札と同じ(参照が同じ)タイルは除外
        val baseTiles: List<TileViewModel> = Constant.TILES.toMutableList()
            .filter { tile ->
                !ownTiles.value!!.any { it === tile }
            }

        Log.println(Log.DEBUG, Constant.LOG_TAG + "[${name}]", "baseTiles => " + baseTiles)

        for (t1 in baseTiles) {
            doNestedCreateTilePattern(patternsMap, baseTiles, t1)
        }
        patterns = patternsMap.values.toMutableSet()

        Log.println(
            Log.DEBUG,
            Constant.LOG_TAG + "[${name}]",
            "候補件数[重複排除後] => " + patterns.size
        )
        val endTime = System.currentTimeMillis() // 処理終了時間を取得
        val elapsedTime = endTime - startTime // 処理時間を計算
        Log.println(Log.DEBUG, Constant.LOG_TAG + "[${name}]", "処理時間: $elapsedTime ms")
        isCreatedPatterns.postValue(true)
    }

    private fun doNestedCreateTilePattern(
        patternsMap: HashMap<String, Array<TileViewModel>>,
        baseTiles: List<TileViewModel>,
        t1: TileViewModel
    ) {
        for (t2 in baseTiles) {
            if (t1 === t2 || t1.greaterThan(t2) ||
                (t1.no.value == t2.no.value && t1.match(Color.BLUE) && t2.match(Color.RED))
            ) {
                continue
            }
            doNestedCreateTilePattern(patternsMap, baseTiles, t1, t2)
        }
    }

    private fun doNestedCreateTilePattern(
        patternsMap: HashMap<String, Array<TileViewModel>>,
        baseTiles: List<TileViewModel>,
        t1: TileViewModel,
        t2: TileViewModel
    ) {
        for (t3 in baseTiles) {
            if (t1 === t3 || t2 === t3 || t1.greaterThan(t3) || t2.greaterThan(t3) ||
                (t2.no
                    .value == t3.no.value && t2.match(Color.BLUE) && t3.match(Color.RED))
            ) {
                continue
            }
            doNestedCreateTilePattern(patternsMap, baseTiles, t1, t2, t3)
        }
    }

    private fun doNestedCreateTilePattern(
        patternsMap: HashMap<String, Array<TileViewModel>>,
        baseTiles: List<TileViewModel>,
        t1: TileViewModel,
        t2: TileViewModel,
        t3: TileViewModel
    ) {
        for (t4 in baseTiles) {
            if (t1 === t4 || t2 === t4 || t3 === t4 ||
                t1.greaterThan(t4) || t2.greaterThan(t4) || t3.greaterThan(t4) ||
                (t3.no.value == t4.no.value && t3.match(Color.BLUE) && t4.match(Color.RED))
            ) {
                continue
            }
            doNestedCreateTilePattern(patternsMap, baseTiles, t1, t2, t3, t4)
        }
    }

    private fun doNestedCreateTilePattern(
        patternsMap: HashMap<String, Array<TileViewModel>>,
        baseTiles: List<TileViewModel>,
        t1: TileViewModel,
        t2: TileViewModel,
        t3: TileViewModel,
        t4: TileViewModel
    ) {
        for (t5 in baseTiles) {
            if (t1 === t5 || t2 === t5 || t3 === t5 || t4 === t5 ||
                t1.greaterThan(t5) || t2.greaterThan(t5) || t3.greaterThan(t5) || t4.greaterThan(
                    t5
                ) || (t4.no.value == t5.no.value && t4.match(Color.BLUE) && t5.match(
                    Color.RED
                ))
            ) {
                continue
            }


            val tiles: Array<TileViewModel> = arrayOf(t1, t2, t3, t4, t5)
            val key = tiles.joinToString { it.no.value.toString() + it.color.value.toString() }
//            Logger.d(Constant.LOG_TAG + "[${name}]", "key -> ${key}")
            patternsMap.put(key, tiles)

        }
    }
}