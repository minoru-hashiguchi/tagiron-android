package com.icloud.hashiguchi.minoru.mobile.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.icloud.hashiguchi.minoru.tagiron.Tile
import com.icloud.hashiguchi.minoru.tagiron.constants.Color
import com.icloud.hashiguchi.minoru.tagiron.constants.Constant
import com.icloud.hashiguchi.minoru.tagiron.questions.QuestionBase

class GameLiveDataViewModel : ViewModel() {
    private var _text = MutableLiveData("あなたの手札")
    private var _myTiles = MutableLiveData<MutableList<Tile>>(
        mutableListOf(
            Tile(1, Color.RED),
            Tile(1, Color.BLUE),
            Tile(5, Color.YELLOW),
            Tile(7, Color.BLUE),
            Tile(8, Color.RED)
        )
    )
    private var _fieldQuestions = MutableLiveData<MutableList<QuestionBase>>(mutableListOf())

    init {
        for (i in 0..5) {
            _fieldQuestions.value?.add(Constant.QUESTIONS.get(i))
        }
    }

    val myTiles: LiveData<MutableList<Tile>>
        get() = _myTiles

    val text: LiveData<String>
        get() = _text

    val fieldQuestions: LiveData<MutableList<QuestionBase>>
        get() = _fieldQuestions
}