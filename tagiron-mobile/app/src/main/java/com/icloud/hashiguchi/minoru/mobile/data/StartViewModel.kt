package com.icloud.hashiguchi.minoru.mobile.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class StartViewModel : ViewModel() {

    private val _onMoveSubActivity: MutableLiveData<Event<Boolean>> by lazy { MutableLiveData<Event<Boolean>>() }
    val onMoveGameActivity: LiveData<Event<Boolean>> = _onMoveSubActivity

    //　「始める」をクリックしたときの処理
    fun onClickButton(isFirstMove: Boolean) {
        _onMoveSubActivity.value = Event(isFirstMove)
    }
}