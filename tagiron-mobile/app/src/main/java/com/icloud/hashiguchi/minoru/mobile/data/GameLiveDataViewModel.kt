package com.icloud.hashiguchi.minoru.mobile.data

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.icloud.hashiguchi.minoru.tagiron.TileViewModel
import com.icloud.hashiguchi.minoru.tagiron.constants.Constant
import com.icloud.hashiguchi.minoru.tagiron.questions.QuestionBase
import com.icloud.hashiguchi.minoru.tagiron.questions.ShareableQuestion


class GameLiveDataViewModel : ViewModel() {
    private var _isMyTurn = MutableLiveData(false)
    private var me = HumanPlayer("あなた")
    private var you = ComputerPlayer("相手")
    private var _isPlaying = MutableLiveData(true)
    private var _isQuestion = MutableLiveData(true)
    private var _gameTiles = Constant.TILES.toMutableList()
    private var _gameQuestions = Constant.QUESTIONS.toMutableList()
    private var _thinkingTiles = MutableLiveData<MutableList<TileViewModel>>(
        mutableListOf(
            TileViewModel(),
            TileViewModel(),
            TileViewModel(),
            TileViewModel(),
            TileViewModel()
        )
    )
    private var _fieldQuestions = MutableLiveData<MutableList<QuestionBase>>(mutableListOf())

    private var _selectedThinkingTilePosition = MutableLiveData<Int>(null)

    init {
        // シャッフル
        _gameTiles.shuffle()
        _gameQuestions.shuffle()

        listOf(me, you).forEach {
            it.pickTilesAfterThatSort(_gameTiles)
            it.createTilePattern()
        }

        replenishQuestions()

        autoPlay()
    }

    val ownTiles: LiveData<MutableList<TileViewModel>> = me.ownTiles
    val fieldQuestions: LiveData<MutableList<QuestionBase>> = _fieldQuestions
    val thinkingTiles: LiveData<MutableList<TileViewModel>> = _thinkingTiles
    val showQuestionSelector: LiveData<Boolean> = _isQuestion.map { it && _isPlaying.value!! }
    val showCallEditor: LiveData<Boolean> = _isQuestion.map { !it && _isPlaying.value!! }
    val selectedTilePosition: LiveData<Int> = _selectedThinkingTilePosition

    fun onClickSelectQestion(view: View) {
        _isQuestion.postValue(true)
    }

    fun onClickSelectCall(view: View) {
        _isQuestion.postValue(false)
    }

    fun onClickTile(number: Int) {
        _selectedThinkingTilePosition.postValue(number)
    }

    fun onClickNumber(number: Int) {
        var tile = _selectedThinkingTilePosition.value?.let { _thinkingTiles.value?.get(it - 1) }
        if (tile != null) {
            tile.setNo(number)
        }
    }

    private fun replenishQuestions() {
        // 不足数
        val count: Int = Constant.OPEN_QUESTIONS_COUNT - _fieldQuestions.value?.size!!
        for (i in 0 until count) {
            if (_gameQuestions.size == 0) {
                println("質問の山札なし")
                break
            }

            // 山札の先頭を場に追加
            _fieldQuestions.value!!.add(_gameQuestions.get(0))
            // 山札の先頭を削除
            _gameQuestions.removeAt(0)
        }
    }

    private fun autoPlay() {
        _isMyTurn.postValue(false)

        // 行動の決定
        val actionNo = you.selectAction(_fieldQuestions.value!!)

        if (actionNo == null) {
            // 宣言
            val result = you.call(me.ownTiles.value!!)
            when (result) {
//                isPlaying = false
                true -> return
                false -> _isMyTurn.postValue(true)
            }
        } else {
            // 質問
            val picked = you.pickQuestion(actionNo, _fieldQuestions.value!!)
            you.askQuestion(me.ownTiles.value!!, picked)

            // 共有情報カードの場合は自分も相手に回答する
            if (picked is ShareableQuestion) {
                me.askQuestion(you.ownTiles.value!!, picked)
            }
            _isMyTurn.postValue(true)
        }
    }
}