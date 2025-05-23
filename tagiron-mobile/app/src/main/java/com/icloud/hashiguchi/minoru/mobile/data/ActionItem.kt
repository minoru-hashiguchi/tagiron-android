package com.icloud.hashiguchi.minoru.mobile.data

import com.icloud.hashiguchi.minoru.mobile.utils.Logger
import com.icloud.hashiguchi.minoru.tagiron.TileViewModel
import com.icloud.hashiguchi.minoru.tagiron.questions.QuestionBase

class ActionItem(var question: QuestionBase?, var count: Int) {
    var isSucceed = false
    var calledTiles: MutableList<TileViewModel> = mutableListOf()

    constructor(
        calledTiles: MutableList<TileViewModel>,
        count: Int,
        isSucceed: Boolean
    ) : this(null, count) {
        Logger.d("ActionItem: calledTiles=${calledTiles}")
        this.isSucceed = isSucceed
        calledTiles.forEach({
            this.calledTiles.add(it.clone())
        })
    }

    fun getAnswerText(): String? {
        if (question != null) {
            return question!!.answerText
        }
        return if (isSucceed) {
            "正解"
        } else {
            "はずれ"
        }
    }

    fun getSummaryText(): String {
        if (question != null) {

            return question?.summaryText!!
        }
        return "宣言"
    }

    fun getCountText(): String {
        return count.toString()
    }

    override fun toString(): String {
        if (question != null) {
            return "${getSummaryText()}, ${getAnswerText()}, ${getCountText()}"
        }
        return "${calledTiles}, ${getAnswerText()}, ${getCountText()}"
    }
}