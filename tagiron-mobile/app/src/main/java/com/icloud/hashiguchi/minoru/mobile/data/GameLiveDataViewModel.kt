package com.icloud.hashiguchi.minoru.mobile.data

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.icloud.hashiguchi.minoru.tagiron.Tile
import com.icloud.hashiguchi.minoru.tagiron.constants.Constant
import com.icloud.hashiguchi.minoru.tagiron.questions.QuestionBase

class GameLiveDataViewModel : ViewModel() {
    private var _isPlaying = MutableLiveData(true)
    private var _isQuestion = MutableLiveData(true)
    private var _gameTiles =
        MutableLiveData<MutableList<Tile>>(Constant.TILES.toMutableList())
    private var _gameQuestions =
        MutableLiveData<MutableList<QuestionBase>>(Constant.QUESTIONS.toMutableList())
    private var _ownTiles = MutableLiveData<MutableList<Tile>>(mutableListOf())
    private var _thinkingTiles = MutableLiveData<MutableList<Tile>>(mutableListOf())
    private var _fieldQuestions = MutableLiveData<MutableList<QuestionBase>>(mutableListOf())

    private var _selectedThinkingTilePosition = MutableLiveData<Int>(null)

    init {
        // シャッフル
        _gameTiles.value?.shuffle()
        _gameQuestions.value?.shuffle()

        for (i in 0..4) {
            val item = _gameTiles.value?.get(0)
            if (item != null) {
                _ownTiles.value?.add(item)
                _gameTiles.value?.remove(item)
            }
            _thinkingTiles.value?.add(Tile())
        }
        _ownTiles.value?.sortWith(compareBy<Tile> { it.no.value }.thenBy { it.color.value })

        for (i in 0..5) {
            val item = _gameQuestions.value?.get(0)
            if (item != null) {
                _fieldQuestions.value?.add(item)
                _gameQuestions.value?.remove(item)
            }
        }
    }

    val ownTiles: LiveData<MutableList<Tile>> = _ownTiles
    val fieldQuestions: LiveData<MutableList<QuestionBase>> = _fieldQuestions
    val thinkingTiles: LiveData<MutableList<Tile>> = _thinkingTiles
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
}