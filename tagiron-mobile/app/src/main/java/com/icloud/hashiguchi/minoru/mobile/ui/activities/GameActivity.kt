package com.icloud.hashiguchi.minoru.mobile.ui.activities

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.icloud.hashiguchi.minoru.mobile.data.GameLiveDataViewModel
import com.icloud.hashiguchi.minoru.mobile.utils.FieldQuestionCardsAdapter
import com.icloud.hashiguchi.minoru.mobile.utils.QuestionsSammaryAdapter
import com.icloud.hashiguchi.minoru.tagiron.constants.Constant
import com.icloud.hashiguchi.minoru.tagiron.questions.QuestionBase
import com.icloud.hashiguchi.tagironmobile.R
import com.icloud.hashiguchi.tagironmobile.databinding.ActivityGameBinding

class GameActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(Constant.LOG_TAG, "onCreate -- begin")
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

        setupFieldQuestionCards(viewModel, binding)

        setupQuestionHistory(
            R.id.recyclerViewQuestions1,
            viewModel.leftQuestionsHistory,
            binding.recyclerViewQuestions1
        )

        setupQuestionHistory(
            R.id.recyclerViewQuestions2,
            viewModel.rightQuestionsHistory,
            binding.recyclerViewQuestions2
        )

        viewModel.showQuestionSelector.observe(this) {
            Log.d(Constant.LOG_TAG, "showQuestionSelector.observe=${it}")
        }
        viewModel.showCallEditor.observe(this) {
            Log.d(Constant.LOG_TAG, "showCallEditor.observe=${it}")
        }

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

    private fun setupFieldQuestionCards(
        viewModel: GameLiveDataViewModel,
        binding: ActivityGameBinding
    ) {

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

                SampleDialog().show(supportFragmentManager, "aaaas")
//                viewModel.onSelectQuestion(position)
            }
        })

        binding.recyclerViewQuestions.adapter = fqcAdapter
        viewModel.fieldQuestions.observe(this, Observer {
            it.let {
                fqcAdapter.data = it
            }
        })
    }

    fun setupQuestionHistory(
        rid: Int,
        liveData: LiveData<MutableList<QuestionBase>>,
        bindingRecyclerView: RecyclerView
    ) {
        val adapter = QuestionsSammaryAdapter(liveData)
        val recyclerView: RecyclerView = findViewById(rid)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        val separate = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        recyclerView.addItemDecoration(separate)

        bindingRecyclerView.adapter = adapter
        liveData.observe(this, Observer {
            it.let {
                adapter.data = it
            }
        })

        adapter.setListener(object : QuestionsSammaryAdapter.QuestionsSammaryAdapterListener {
            override fun contentTapped(position: Int) {
                // Do nothing.
            }

        })
    }

    override fun onStart() {
        super.onStart()
        Log.d(Constant.LOG_TAG, "onStart -- begin")

        val viewModel by viewModels<GameLiveDataViewModel>()
        Log.d(Constant.LOG_TAG, "showQuestionSelector=${viewModel.showQuestionSelector.value}")
        viewModel.autoPlay()
        Log.d(Constant.LOG_TAG, "onStart -- end")
    }
}

