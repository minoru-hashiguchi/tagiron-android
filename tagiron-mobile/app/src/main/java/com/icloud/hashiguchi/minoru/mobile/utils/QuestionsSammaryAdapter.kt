package com.icloud.hashiguchi.minoru.mobile.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.icloud.hashiguchi.minoru.tagiron.questions.QuestionBase
import com.icloud.hashiguchi.tagironmobile.R

class QuestionsSammaryAdapter(private val questionCardList: LiveData<MutableList<QuestionBase>>) :
    RecyclerView.Adapter<QuestionsSammaryAdapter.QuestionCardListRecyclerViewHolder>() {

    private lateinit var listener: QuestionsSammaryAdapterListener

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): QuestionCardListRecyclerViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.question_sammary_layout, parent, false)
        return QuestionCardListRecyclerViewHolder(view)
    }

    override fun getItemCount(): Int {
        return questionCardList.value?.size ?: 0
    }

    override fun onBindViewHolder(holder: QuestionCardListRecyclerViewHolder, position: Int) {
        holder.questionText.text = questionCardList.value?.get(position)?.summaryText
        holder.answerText.text = questionCardList.value?.get(position)?.answerText

        holder.itemView.setOnClickListener {
            listener.contentTapped(position)
        }
    }

    class QuestionCardListRecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var questionText: TextView = itemView.findViewById(R.id.textViewQuestionText)
        var answerText: TextView = itemView.findViewById(R.id.textViewAnswerText)
    }

    fun setListener(listener: QuestionsSammaryAdapterListener) {
        this.listener = listener
    }

    interface QuestionsSammaryAdapterListener {
        fun contentTapped(position: Int)
    }
}