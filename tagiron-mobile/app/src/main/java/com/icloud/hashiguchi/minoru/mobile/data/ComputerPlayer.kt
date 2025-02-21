package com.icloud.hashiguchi.minoru.mobile.data

import android.util.Log
import com.icloud.hashiguchi.minoru.tagiron.TileViewModel
import com.icloud.hashiguchi.minoru.tagiron.constants.Constant
import com.icloud.hashiguchi.minoru.tagiron.questions.QuestionBase
import com.icloud.hashiguchi.minoru.tagiron.questions.QuestionWhereNoBySelect
import java.util.Random


class ComputerPlayer(name: String) : Player(name) {

    override fun selectAction(questions: MutableList<QuestionBase>): Int? {
        // 場の質問カードがないときは宣言のみ
        if (questions.size == 0 || patterns.size == 1) {
            Log.println(Log.INFO, Constant.LOG_TAG + "[${name}]", "宣言する")
            return null
        }
        // TODO 質問は暫定でランダム
        val pickedIndex: Int = Random().nextInt(questions.size)
        return pickedIndex
    }

    override fun call(): Array<TileViewModel> {
        val index = Random().nextInt(patterns.size)
        return patterns.elementAt(index)
    }

    override fun selectNumber(question: QuestionWhereNoBySelect): Int {
        val values = question.selectNumbers
        // TODO 暫定でランダム
        val index: Int = Random().nextInt(values.size)
        return values[index]
    }
}