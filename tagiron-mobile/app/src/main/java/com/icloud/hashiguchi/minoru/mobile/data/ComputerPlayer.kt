package com.icloud.hashiguchi.minoru.mobile.data

import android.util.Log
import com.icloud.hashiguchi.minoru.mobile.utils.Logger
import com.icloud.hashiguchi.minoru.tagiron.TileViewModel
import com.icloud.hashiguchi.minoru.tagiron.constants.Constant
import com.icloud.hashiguchi.minoru.tagiron.questions.QuestionBase
import com.icloud.hashiguchi.minoru.tagiron.questions.QuestionWhereNoBySelect
import java.util.Random


class ComputerPlayer(name: String) : Player(name) {
    var level = Level.STRONG
        set(value) {
            field = value
            Logger.i(
                Constant.LOG_TAG,
                "コンピュータのレベルは${field}(${field.displayName})で設定されました"
            )
        }
    var pickedQuestionIndex = 0
    var pickedNumberIndex = 0
    override var name: String = "${name}(${level.displayName})"

    override fun selectAction(questions: MutableList<QuestionBase>): Int? {
        // 場の質問カードがないときは宣言のみ
        if (questions.size == 0 || patterns.size == 1) {
            Log.println(Log.INFO, Constant.LOG_TAG + "[${name}]", "宣言する")
            return null
        }

        when (level) {
            Level.NORMAL -> {
                pickedQuestionIndex = Random().nextInt(questions.size)
            }

            Level.STRONG -> {
                var list = createExpectedAnswersList(questions)
                list.sortWith(
                    compareByDescending<ExpectedAnswers> { it.onlyOne }
                        .thenBy { it.average }
                        .thenByDescending { it.oneCount }
                        .thenBy { it.isShareable }
                )
                list.forEach({ Logger.d(Constant.LOG_TAG, "${it}") })
                if (list.isEmpty()) {
                    // パターンが減らせる質問がなければ宣言する
                    return null
                }
                pickedQuestionIndex = list.get(0).questionIndex
                pickedNumberIndex = list.get(0).numberIndex ?: 0
            }
        }

        return pickedQuestionIndex
    }

    override fun call(): Array<TileViewModel> {
        val index = Random().nextInt(patterns.size)
        return patterns.elementAt(index)
    }

    override fun selectNumber(question: QuestionWhereNoBySelect): Int {
        val values = question.selectNumbers
        val index = when (level) {
            Level.NORMAL -> Random().nextInt(values.size)
            Level.STRONG -> pickedNumberIndex
        }
        return values[index]
    }

    private fun createExpectedAnswersList(questions: MutableList<QuestionBase>): MutableList<ExpectedAnswers> {
        var results: MutableList<ExpectedAnswers> = mutableListOf()
        for (questionIndex in 0..questions.size - 1) {
            var q = questions.get(questionIndex)
            if (q is QuestionWhereNoBySelect) {
                for (numberIndex in 0..q.selectNumbers.size - 1) {
                    var clone = q.clone()
                    clone.selectedNo = q.selectNumbers.get(numberIndex)
                    var expected =
                        ExpectedAnswers(clone, patterns, questionIndex, numberIndex).execute()
                    if (expected.ignore) {
                        Logger.d(Constant.LOG_TAG, "除外：#${clone.summaryText}")
                    } else {
                        results.add(expected)
                    }
                }
            } else {
                var expected =
                    ExpectedAnswers(q, patterns, questionIndex).execute()
                if (expected.ignore) {
                    Logger.d(Constant.LOG_TAG, "除外：#${q.summaryText}")
                } else {
                    results.add(expected)
                }
            }
        }
        return results
    }
}