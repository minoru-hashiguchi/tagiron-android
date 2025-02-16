package com.icloud.hashiguchi.minoru.mobile.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class StartViewModel : ViewModel() {

    private val _onMoveSubActivity: MutableLiveData<Event<Int>> by lazy { MutableLiveData<Event<Int>>() }
    val onMoveGameActivity: LiveData<Event<Int>> = _onMoveSubActivity

    //　「始める」をクリックしたときの処理
    fun onClickButton(firstMoveOrLastAtackNo: Int) {
        _onMoveSubActivity.value = Event(firstMoveOrLastAtackNo)
    }
}