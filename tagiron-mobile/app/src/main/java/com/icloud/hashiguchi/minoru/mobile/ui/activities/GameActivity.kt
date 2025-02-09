package com.icloud.hashiguchi.minoru.mobile.ui.activities

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.icloud.hashiguchi.minoru.mobile.data.GameLiveDataViewModel
import com.icloud.hashiguchi.minoru.mobile.utils.FieldQuestionCardsAdapter
import com.icloud.hashiguchi.minoru.mobile.utils.QuestionsSammaryAdapter
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

        val fqcAdapter = FieldQuestionCardsAdapter(viewModel.fieldQuestions)
        val fqcRecyclerView: RecyclerView = findViewById(R.id.recyclerViewQuestions)
        fqcRecyclerView.adapter = fqcAdapter
        fqcRecyclerView.layoutManager = GridLayoutManager(
            this,
            2,
            RecyclerView.VERTICAL,
            false
        )

        fqcAdapter.setListener(object :
            FieldQuestionCardsAdapter.FieldQuestionCardsAdapterListener {
            override fun contentTapped(position: Int) {
                // セルをタップした時の処理
                Toast.makeText(
                    applicationContext,
                    "[${position}] ${viewModel.fieldQuestions.value?.get(position)?.text}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })

        val qsAdapter = QuestionsSammaryAdapter(viewModel.fieldQuestions)

        val leftRecyclerView: RecyclerView = findViewById(R.id.recyclerViewQuestions1)
        leftRecyclerView.adapter = qsAdapter
        leftRecyclerView.layoutManager = LinearLayoutManager(this)
        val separate1 = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        leftRecyclerView.addItemDecoration(separate1)

        val rightRecyclerView: RecyclerView = findViewById(R.id.recyclerViewQuestions2)
        rightRecyclerView.adapter = qsAdapter
        rightRecyclerView.layoutManager = LinearLayoutManager(this)
        val separate2 = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        rightRecyclerView.addItemDecoration(separate2)

        qsAdapter.setListener(object : QuestionsSammaryAdapter.QuestionsSammaryAdapterListener {
            override fun contentTapped(position: Int) {
//                TODO("Not yet implemented")
            }

        })
    }

}