package com.icloud.hashiguchi.minoru.mobile.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.icloud.hashiguchi.minoru.tagiron.Tile
import com.icloud.hashiguchi.minoru.tagiron.constants.Constant
import com.icloud.hashiguchi.minoru.tagiron.questions.QuestionBase

class GameLiveDataViewModel : ViewModel() {
    private var _gameTiles =
        MutableLiveData<MutableList<Tile>>(Constant.TILES.toMutableList())
    private var _gameQuestions =
        MutableLiveData<MutableList<QuestionBase>>(Constant.QUESTIONS.toMutableList())
    private var _myTiles = MutableLiveData<MutableList<Tile>>(mutableListOf())
    private var _fieldQuestions = MutableLiveData<MutableList<QuestionBase>>(mutableListOf())

    init {
        // シャッフル
        _gameTiles.value?.shuffle()
        _gameQuestions.value?.shuffle()

        for (i in 0..4) {
            val item = _gameTiles.value?.get(0)
            if (item != null) {
                _myTiles.value?.add(item)
                _gameTiles.value?.remove(item)
            }
        }
        _myTiles.value?.sortWith(compareBy<Tile> { it.no.value }.thenBy { it.color.value })

        for (i in 0..5) {
            val item = _gameQuestions.value?.get(0)
            if (item != null) {
                _fieldQuestions.value?.add(item)
                _gameQuestions.value?.remove(item)
            }
        }
    }

    val myTiles: LiveData<MutableList<Tile>>
        get() = _myTiles

    val fieldQuestions: LiveData<MutableList<QuestionBase>>
        get() = _fieldQuestions
}