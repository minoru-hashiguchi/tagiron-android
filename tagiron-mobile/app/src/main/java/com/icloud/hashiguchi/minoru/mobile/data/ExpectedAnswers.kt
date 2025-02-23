package com.icloud.hashiguchi.minoru.mobile.data

import com.icloud.hashiguchi.minoru.tagiron.TileViewModel
import com.icloud.hashiguchi.minoru.tagiron.questions.QuestionBase
import com.icloud.hashiguchi.minoru.tagiron.questions.ShareableQuestion
import kotlin.math.roundToInt

//自手のリターンを優先する質問を選択するものとし、質問選択の優先度は以下の通り
//残パターンが「１」のみで絞り切れているもの
//質問に対する残パターン数の平均値の昇順
//残パターンの「１」が多い順
//さらに同列の場合は「共有情報カード」でないものを優先
class ExpectedAnswers(
    val q: QuestionBase,
    val patterns: MutableSet<Array<TileViewModel>>,
    val questionIndex: Int,
    val numberIndex: Int?
) {
    constructor(
        q: QuestionBase,
        patterns: MutableSet<Array<TileViewModel>>,
        questionIndex: Int
    ) : this(q, patterns, questionIndex, null)

    var onlyOne: Boolean = false
    var average: Double = 0.0
    var oneCount: Int = 0
    var isShareable: Boolean = false
    var ignore: Boolean = false
    fun execute(): ExpectedAnswers {

        val map = HashMap<List<Int>, Int>(mapOf())
        patterns.forEach {
            val answers = q.answer(it.toMutableList())
            if (map.containsKey(answers)) {
                val count: Int? = map.get(answers)
                if (count != null) {
                    map.set(answers, count + 1)
                }
            } else {
                map.put(answers, 1)
            }
        }
        average = (map.values.average() * 100.0).roundToInt() / 100.0
        oneCount = map.values.filter { it == 1 }.count()
        onlyOne = oneCount == map.size
        isShareable = q is ShareableQuestion
        ignore = average.roundToInt() == patterns.size
        return this
    }

    override fun toString(): String {
        return "1のみ=${onlyOne}, 平均=${average}, 1の数=${oneCount}, 共有可能=${isShareable} #${q.summaryText}"
    }
}