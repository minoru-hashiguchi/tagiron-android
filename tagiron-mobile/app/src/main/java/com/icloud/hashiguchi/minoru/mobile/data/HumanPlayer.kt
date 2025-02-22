package com.icloud.hashiguchi.minoru.mobile.data

import androidx.lifecycle.MutableLiveData
import com.icloud.hashiguchi.minoru.tagiron.TileViewModel
import com.icloud.hashiguchi.minoru.tagiron.questions.QuestionBase
import com.icloud.hashiguchi.minoru.tagiron.questions.QuestionWhereNoBySelect
import java.util.Arrays


class HumanPlayer(name: String) : Player(name) {

    var selectPosition: Int? = null
    var thinkingTiles = MutableLiveData<MutableList<TileViewModel>>(
        mutableListOf(
            TileViewModel(),
            TileViewModel(),
            TileViewModel(),
            TileViewModel(),
            TileViewModel()
        )
    )

    override fun selectAction(questions: MutableList<QuestionBase>): Int? {
        TODO("Not yet implemented")
    }

    /**
     * プレイヤーが宣言する
     *
     * パターンのインスタンスを取得する理由は、宣言後にパターン削除ができなくなるため
     * @return 予想タイルに合致するパターンが存在する場合はその配列インスタンス、それ以外は予想タイルを配列変換したインスタンス
     */
    override fun call(): Array<TileViewModel> {
        val target = thinkingTiles.value!!.toTypedArray()
        val found = patterns.find { Arrays.deepEquals(it, target) }
        return if (found != null) found else thinkingTiles.value!!.toTypedArray()
    }

    override fun selectNumber(question: QuestionWhereNoBySelect): Int {
        try {
            val items = question.selectNumbers
            return items.get(selectPosition!!)
        } finally {
            selectPosition = null
        }
    }
}