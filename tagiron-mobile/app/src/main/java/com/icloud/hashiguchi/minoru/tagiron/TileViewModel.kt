package com.icloud.hashiguchi.minoru.tagiron

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.icloud.hashiguchi.minoru.tagiron.constants.Color
import com.icloud.hashiguchi.minoru.tagiron.constants.Color.BLUE
import com.icloud.hashiguchi.minoru.tagiron.constants.Color.RED
import com.icloud.hashiguchi.minoru.tagiron.constants.Color.YELLOW
import com.icloud.hashiguchi.tagironmobile.R

class TileViewModel(no: Int?, color: Color?) : ViewModel(), Cloneable {
    private val _no: MutableLiveData<Int?> = MutableLiveData(no)
    private val _color: MutableLiveData<Color?> = MutableLiveData(color)

    constructor() : this(null, null)

    val no: LiveData<Int?> = _no
    val color: LiveData<Color?> = _color
    val noText: LiveData<String?> = _no.map {
        it?.toString()
    }

    fun setNo(no: Int?) {
        _no.postValue(no)
    }

    fun setColor(color: Color?) {
        _color.postValue(color)
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
        if (other !is TileViewModel) {
            return false
        }
        return this.toString() == other.toString()
    }

    override fun toString(): String {
        return _color.value.toString() + _no.value.toString()
    }

    public override fun clone(): TileViewModel {
        try {
            return super.clone() as TileViewModel
        } catch (e: CloneNotSupportedException) {
            e.printStackTrace()
            throw RuntimeException(e)
        }
    }

    fun greaterThan(tile: TileViewModel): Boolean {
        if (this._no.value == null || tile.no.value == null) {
            return false
        }
        return this._no.value!! > tile.no.value!!
    }

    fun match(color: Color): Boolean {
        return this._color.value == color
    }
}

