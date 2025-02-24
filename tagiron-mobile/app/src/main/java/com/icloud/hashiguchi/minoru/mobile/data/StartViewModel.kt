package com.icloud.hashiguchi.minoru.mobile.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class StartViewModel : ViewModel() {

    private val _onMoveGameActivity: MutableLiveData<Event<Parameter>> by lazy { MutableLiveData<Event<Parameter>>() }
    val onMoveGameActivity: LiveData<Event<Parameter>> = _onMoveGameActivity

    //　「始める」をクリックしたときの処理
    fun onClickButton(firstOrSecondMove: FirstOrSecondMove, level: Level) {
        _onMoveGameActivity.value = Event(Parameter(firstOrSecondMove, level))
    }

    class Parameter(val firstOrSecondMove: FirstOrSecondMove, val level: Level)
}