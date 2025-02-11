package com.icloud.hashiguchi.minoru.mobile.data

import com.icloud.hashiguchi.minoru.tagiron.TileViewModel
import com.icloud.hashiguchi.minoru.tagiron.questions.QuestionBase
import com.icloud.hashiguchi.minoru.tagiron.questions.QuestionWhereNoBySelect


class HumanPlayer(name: String) : Player(name) {

    override fun selectAction(questions: MutableList<QuestionBase>): Int? {
        TODO("Not yet implemented")
    }

    override fun call(yourTiles: MutableList<TileViewModel>): Boolean {
        TODO("Not yet implemented")
    }

    override fun selectNumber(question: QuestionWhereNoBySelect): Int {
        TODO("Not yet implemented")
    }
}