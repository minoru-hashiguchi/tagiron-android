package com.icloud.hashiguchi.minoru.mobile.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.icloud.hashiguchi.minoru.tagiron.questions.QuestionBase
import com.icloud.hashiguchi.tagironmobile.R

class FieldQuestionCardsAdapter(private val questionCardList: LiveData<MutableList<QuestionBase>>) :
    RecyclerView.Adapter<FieldQuestionCardsAdapter.QuestionCardListRecyclerViewHolder>() {

    private lateinit var listener: FieldQuestionCardsAdapterListener

    // RecyclerViewに表示するデータオブジェクト
    var data: MutableList<QuestionBase> = questionCardList.value!!
        set(value) {
            field = value
            // データ変更時に自動で再描画するための設定
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): QuestionCardListRecyclerViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.question_card_layout, parent, false)
        return QuestionCardListRecyclerViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: QuestionCardListRecyclerViewHolder, position: Int) {
        holder.questionText.text = data.get(position).text

        holder.itemView.setOnClickListener {
            listener.contentTapped(position)
        }
    }

    class QuestionCardListRecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var questionText: TextView = itemView.findViewById(R.id.textViewQuestionText)
    }

    fun setListener(listener: FieldQuestionCardsAdapterListener) {
        this.listener = listener
    }

    interface FieldQuestionCardsAdapterListener {
        fun contentTapped(position: Int)
    }
}