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

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): QuestionCardListRecyclerViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.question_card_layout, parent, false)
        return QuestionCardListRecyclerViewHolder(view)
    }

    override fun getItemCount(): Int {
        return questionCardList.value?.size ?: 0
    }

    override fun onBindViewHolder(holder: QuestionCardListRecyclerViewHolder, position: Int) {
        holder.countText.text = questionCardList.value?.get(position)?.text

        holder.itemView.setOnClickListener {
            listener.contentTapped(position)
        }
    }

    class QuestionCardListRecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var countText: TextView = itemView.findViewById(R.id.textViewQuestionText)
    }

    fun setListener(listener: FieldQuestionCardsAdapterListener) {
        this.listener = listener
    }

    interface FieldQuestionCardsAdapterListener {
        fun contentTapped(position: Int)
    }
}