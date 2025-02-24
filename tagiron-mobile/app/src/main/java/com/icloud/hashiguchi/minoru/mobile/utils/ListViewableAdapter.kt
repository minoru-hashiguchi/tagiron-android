package com.icloud.hashiguchi.minoru.mobile.utils

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.icloud.hashiguchi.minoru.mobile.data.ListViewable
import com.icloud.hashiguchi.tagironmobile.R


class ListViewableAdapter(context: Context, list: MutableList<ListViewable>) :
    ArrayAdapter<ListViewable>(context, R.layout.view_spinner_item_layout, list) {

    init {
        setDropDownViewResource(R.layout.view_spinner_item_layout)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val textView = super.getView(position, convertView, parent) as TextView
        textView.text = getItem(position)?.displayName
        return textView
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val textView = super.getDropDownView(position, convertView, parent) as TextView
        textView.text = getItem(position)?.displayName
        return textView
    }
}