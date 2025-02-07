package com.icloud.hashiguchi.tagironmobile.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.icloud.hashiguchi.tagironmobile.R
import com.icloud.hashiguchi.tagironmobile.constants.TileColor
import com.icloud.hashiguchi.tagironmobile.constants.TileColor.BLUE
import com.icloud.hashiguchi.tagironmobile.constants.TileColor.RED
import com.icloud.hashiguchi.tagironmobile.constants.TileColor.YELLOW

class Tile(no: Int?, color: TileColor?) : ViewModel(), Cloneable {
    private val _no: MutableLiveData<Int?> = MutableLiveData(no)
    private val _color: MutableLiveData<TileColor?> = MutableLiveData(color)

    constructor() : this(null, null)

    val no: LiveData<Int?>
        get() = _no
    val color: LiveData<TileColor?>
        get() = _color
    val colorInt: LiveData<Int>
        get() = _color.map { it?.colorInt ?: R.color.tile_color_undefined }

    val noText: LiveData<String> = _no.map {
        it.toString()
    }

    val colorStr: LiveData<Int> = _color.map {
        when (it) {
            RED -> R.color.tile_color_red
            BLUE -> R.color.tile_color_blue
            YELLOW -> R.color.tile_color_yellow
            null -> R.color.tile_color_undefined
        }
    }

    override fun equals(other: Any?): Boolean {
        if (other !is Tile) {
            return false
        }
        return this.toString() == other.toString()
    }

    override fun toString(): String {
        return _color.value.toString() + _no.value.toString()
    }

    override fun clone(): Tile {
        try {
            return super.clone() as Tile
        } catch (e: CloneNotSupportedException) {
            e.printStackTrace()
            throw RuntimeException(e)
        }
    }
}

