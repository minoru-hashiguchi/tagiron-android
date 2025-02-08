package com.icloud.hashiguchi.minoru.mobile.ui.activities

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.icloud.hashiguchi.minoru.mobile.data.GameLiveDataViewModel
import com.icloud.hashiguchi.minoru.mobile.utils.FieldQuestionCardsAdapter
import com.icloud.hashiguchi.tagironmobile.R
import com.icloud.hashiguchi.tagironmobile.databinding.ActivityGameBinding

class GameActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel by viewModels<GameLiveDataViewModel>()

        // Obtain binding
        val binding: ActivityGameBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_game)

        // Bind layout with ViewModel
        binding.viewmodel = viewModel

        // LiveData needs the lifecycle owner
        binding.lifecycleOwner = this

        setup(viewModel)
    }

    private fun setup(viewModel: GameLiveDataViewModel) {

        val adapter = FieldQuestionCardsAdapter(viewModel.fieldQuestions)
        val homeRecyclerView: RecyclerView = findViewById(R.id.recyclerViewQuestions)
        homeRecyclerView.adapter = adapter
        homeRecyclerView.layoutManager = GridLayoutManager(
            this,
            2,
            RecyclerView.VERTICAL,
            false
        )

        adapter.setListener(object : FieldQuestionCardsAdapter.FieldQuestionCardsAdapterListener {
            override fun contentTapped(position: Int) {
                // セルをタップした時の処理
                Toast.makeText(
                    applicationContext,
                    "[${position}] ${viewModel.fieldQuestions.value?.get(position)?.text}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

}