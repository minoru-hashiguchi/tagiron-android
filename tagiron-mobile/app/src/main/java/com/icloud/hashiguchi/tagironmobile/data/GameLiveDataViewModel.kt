package com.icloud.hashiguchi.tagironmobile.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.icloud.hashiguchi.tagironmobile.constants.TileColor

class GameLiveDataViewModel : ViewModel() {
    private var _text = MutableLiveData("あなたの手札")
    private var _myTiles = MutableLiveData<MutableList<Tile>>(
        mutableListOf(
            Tile(1, TileColor.RED),
            Tile(1, TileColor.BLUE),
            Tile(5, TileColor.YELLOW),
            Tile(7, TileColor.BLUE),
            Tile(8, TileColor.RED)
        )
    )

    val myTiles: LiveData<MutableList<Tile>>
        get() = _myTiles

    val text: LiveData<String>
        get() = _text
}