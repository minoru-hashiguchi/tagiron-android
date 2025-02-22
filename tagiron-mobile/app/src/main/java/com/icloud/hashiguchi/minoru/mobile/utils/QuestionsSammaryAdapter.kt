package com.icloud.hashiguchi.minoru.mobile.utils

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.icloud.hashiguchi.minoru.mobile.data.ActionItem
import com.icloud.hashiguchi.tagironmobile.R

class QuestionsSammaryAdapter(
    private val questionCardList: LiveData<MutableList<ActionItem>>,
    private val _isPlaying: LiveData<Boolean>,
    private val resource: Resources
) :
    RecyclerView.Adapter<QuestionsSammaryAdapter.QuestionCardListRecyclerViewHolder>() {

    // RecyclerViewに表示するデータオブジェクト
    var data: MutableList<ActionItem> = questionCardList.value!!
        set(value) {
            field = value
            // データ変更時に自動で再描画するための設定
            notifyItemInserted(field.size - 1)
        }
    var isPlaying: Boolean = _isPlaying.value!!
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): QuestionCardListRecyclerViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.question_sammary_layout, parent, false)
        return QuestionCardListRecyclerViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: QuestionCardListRecyclerViewHolder, position: Int) {
        val item = data.get(position)
        if ("宣言" == data.get(position).getSummaryText()) {
            holder.questionText.visibility = View.GONE
            holder.linearLayoutTiles.visibility = View.VISIBLE
            val tiles = data.get(position).calledTiles
            holder.tile1.text = tiles.get(0).noText.value
            holder.tile1.background =
                resource.getDrawable(tiles.get(0).drawabeResource.value!!, null)
            holder.tile2.text = tiles.get(1).noText.value
            holder.tile2.background =
                resource.getDrawable(tiles.get(1).drawabeResource.value!!, null)
            holder.tile3.text = tiles.get(2).noText.value
            holder.tile3.background =
                resource.getDrawable(tiles.get(2).drawabeResource.value!!, null)
            holder.tile4.text = tiles.get(3).noText.value
            holder.tile4.background =
                resource.getDrawable(tiles.get(3).drawabeResource.value!!, null)
            holder.tile5.text = tiles.get(4).noText.value
            holder.tile5.background =
                resource.getDrawable(tiles.get(4).drawabeResource.value!!, null)
        } else {
            holder.questionText.visibility = View.VISIBLE
            holder.linearLayoutTiles.visibility = View.GONE
            holder.questionText.text = item.getSummaryText()
        }
        holder.answerText.text = item.getAnswerText()
        holder.textViewPatternCount.text = item.getCountText()
        holder.textViewPatternCount.visibility = if (isPlaying) View.GONE else View.VISIBLE
    }

    class QuestionCardListRecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var questionText: TextView = itemView.findViewById(R.id.textViewQuestionText)
        var answerText: TextView = itemView.findViewById(R.id.textViewAnswerText)
        var textViewPatternCount: TextView = itemView.findViewById(R.id.textViewPatternCount)
        var linearLayoutTiles: LinearLayout = itemView.findViewById(R.id.linearLayoutTiles)
        var tile1: TextView = itemView.findViewById(R.id.textViewNo1)
        var tile2: TextView = itemView.findViewById(R.id.textViewNo2)
        var tile3: TextView = itemView.findViewById(R.id.textViewNo3)
        var tile4: TextView = itemView.findViewById(R.id.textViewNo4)
        var tile5: TextView = itemView.findViewById(R.id.textViewNo5)
    }
}