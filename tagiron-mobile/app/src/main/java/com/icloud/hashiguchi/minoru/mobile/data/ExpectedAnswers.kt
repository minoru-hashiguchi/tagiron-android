package com.icloud.hashiguchi.minoru.mobile.data

import com.icloud.hashiguchi.minoru.tagiron.TileViewModel
import com.icloud.hashiguchi.minoru.tagiron.questions.QuestionBase
import com.icloud.hashiguchi.minoru.tagiron.questions.ShareableQuestion
import kotlin.math.roundToInt

/**
 * 質問と残パターンから、質問の優先順位を特定するために必要なプロパティを定義する
 *
 * 自手のリターンを優先する質問を選択するものとし、質問選択の優先度は以下の通り
 * 残パターンが「１」のみで絞り切れているもの → 自ずと平均値が１に近づくため不要
 * 質問に対する残パターン数の平均値の昇順
 * 残パターンの「１」が多い順
 * さらに同列の場合は「共有情報カード」でないものを優先
 */
class ExpectedAnswers(
    val q: QuestionBase,
    private val patterns: MutableSet<Array<TileViewModel>>,
    val questionIndex: Int,
    val numberIndex: Int?
) {
    var average: Int = 0
    var oneCount: Int = 0
    var isShareable: Boolean = false
    var ignore: Boolean = false

    constructor(
        q: QuestionBase,
        patterns: MutableSet<Array<TileViewModel>>,
        questionIndex: Int
    ) : this(q, patterns, questionIndex, null)

    /**
     * 主に平均値を算出する
     */
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
        average = map.values.average().roundToInt()
        oneCount = map.values.count { it == 1 }
        isShareable = q is ShareableQuestion
        ignore = average == patterns.size
        return this
    }

    override fun toString(): String {
        return "平均=${average}, 1の数=${oneCount}, 共有可能=${isShareable} #${q.summaryText}"
    }
}