package com.icloud.hashiguchi.minoru.mobile.ui.activities

import android.os.Bundle
import android.widget.LinearLayout
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.icloud.hashiguchi.minoru.mobile.data.GameLiveDataViewModel
import com.icloud.hashiguchi.minoru.mobile.utils.FieldQuestionCardsAdapter
import com.icloud.hashiguchi.minoru.mobile.utils.QuestionsSammaryAdapter
import com.icloud.hashiguchi.tagironmobile.R
import com.icloud.hashiguchi.tagironmobile.databinding.ActivityGameBinding

class GameActivity : AppCompatActivity() {
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>

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

        binding.recyclerViewQuestions.adapter

        setup(viewModel, binding)

//        val bottomSheetLayout = findViewById<LinearLayout>(R.id.bottomSheetLayout)
//        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetLayout)
//        bottomSheetBehavior.let { behavior ->
//            // maxHeightやpeekHeightは任意の高さを設定してください。
//            behavior.maxHeight = 1000 // BottomSheetが最大限まで拡張されたときの高さを設定
//            behavior.peekHeight = 500 // BottomSheetが初期状態で表示される高さを設定
//            behavior.isHideable = false
//            behavior.isDraggable = true
//        }
    }

    private fun setup(viewModel: GameLiveDataViewModel, binding: ActivityGameBinding) {

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
                viewModel.onSelectQuestion(position)
            }
        })

        binding.recyclerViewQuestions.adapter = fqcAdapter
        viewModel.fieldQuestions.observe(this, Observer {
            it.let {
                fqcAdapter.data = it
            }
        })


        val qsLeftAdapter = QuestionsSammaryAdapter(viewModel.leftQuestionsHistory)
        val leftRecyclerView: RecyclerView = findViewById(R.id.recyclerViewQuestions1)
        leftRecyclerView.adapter = qsLeftAdapter
        leftRecyclerView.layoutManager = LinearLayoutManager(this)
        val separate1 = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        leftRecyclerView.addItemDecoration(separate1)

        binding.recyclerViewQuestions1.adapter = qsLeftAdapter
        viewModel.leftQuestionsHistory.observe(this, Observer {
            it.let {
                qsLeftAdapter.data = it
            }
        })

        val qsRightAdapter = QuestionsSammaryAdapter(viewModel.rightQuestionsHistory)
        val rightRecyclerView: RecyclerView = findViewById(R.id.recyclerViewQuestions2)
        rightRecyclerView.adapter = qsRightAdapter
        rightRecyclerView.layoutManager = LinearLayoutManager(this)
        val separate2 = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        rightRecyclerView.addItemDecoration(separate2)

        binding.recyclerViewQuestions2.adapter = qsRightAdapter
        viewModel.rightQuestionsHistory.observe(this, Observer {
            it.let {
                qsRightAdapter.data = it
            }
        })

//        qsLeftAdapter.setListener(object : QuestionsSammaryAdapter.QuestionsSammaryAdapterListener {
//            override fun contentTapped(position: Int) {
////                TODO("Not yet implemented")
//            }
//
//        })
    }

}