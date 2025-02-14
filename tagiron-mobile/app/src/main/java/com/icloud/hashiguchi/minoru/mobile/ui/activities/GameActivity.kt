package com.icloud.hashiguchi.minoru.mobile.ui.activities

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBindings
import com.icloud.hashiguchi.minoru.mobile.data.GameLiveDataViewModel
import com.icloud.hashiguchi.minoru.mobile.utils.FieldQuestionCardsAdapter
import com.icloud.hashiguchi.minoru.mobile.utils.QuestionsSammaryAdapter
import com.icloud.hashiguchi.minoru.tagiron.constants.Constant
import com.icloud.hashiguchi.minoru.tagiron.questions.QuestionBase
import com.icloud.hashiguchi.minoru.tagiron.questions.QuestionWhereNoBySelect
import com.icloud.hashiguchi.tagironmobile.R
import com.icloud.hashiguchi.tagironmobile.databinding.ActivityGameBinding
import com.icloud.hashiguchi.tagironmobile.databinding.CallLayoutBinding

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
        viewModel.commuterSelectedQuestion.observe(this) {
            Log.d(
                Constant.LOG_TAG,
                "comSelectedQuestion.observe -- ${viewModel.commuterSelectedQuestion.value}"
            )
            if (viewModel.commuterSelectedQuestion.value != null) {
                Thread.sleep(1000)
                val inflater = this@GameActivity.layoutInflater
                val dialogView = inflater.inflate(R.layout.question_card_layout, null)
                val text: TextView =
                    ViewBindings.findChildViewById<View>(
                        dialogView,
                        R.id.textViewQuestionText
                    ) as TextView
                val q = viewModel.commuterSelectedQuestion.value
                text.text = q?.text

                val builder: AlertDialog.Builder = AlertDialog.Builder(this@GameActivity)
                builder
                    .setTitle("相手からの質問です")
                    .setView(dialogView)
//                .setPositiveButton("OK") { dialog, which ->
//                    // Do something.
//                    viewModel.onSelectQuestion(position, selectPosition)
//                }
                    .setNegativeButton("OK") { dialog, which ->
                        viewModel.clearComputerSelectedQuestion()
                        viewModel.computerAutoPlayAskQuestion(q!!)
                    }

//            if (selectable) {
//                val items: Array<String> =
//                    (q as QuestionWhereNoBySelect).selectNumbers.map { "${it} にする" }
//                        .toTypedArray()
//                builder
//                    .setSingleChoiceItems(
//                        items, 0
//                    ) { dialog, which ->
//                        // Do something.
//                        selectPosition = which
//                    }
//            }

                val dialog: AlertDialog = builder.create()
                dialog.show()
            }
        }

        viewModel.computerCalledTiles.observe(this) {
            if (viewModel.computerCalledTiles.value?.isNotEmpty()!!) {
                val inflater = this@GameActivity.layoutInflater
                val binding: CallLayoutBinding = CallLayoutBinding.inflate(inflater)
                binding.viewmodel = viewModel
                binding.lifecycleOwner = this
                val builder: AlertDialog.Builder = AlertDialog.Builder(this@GameActivity)
                builder
                    .setTitle("相手が宣言しました")
                    .setView(binding.root)
                    .setNegativeButton("OK") { dialog, which ->

                    }
                val dialog: AlertDialog = builder.create()
                dialog.show()
            }
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

            /**
             * 場の質問カードを選択したとき、確認ダイアログを表示する</br>
             *
             * この際、選択式の質問の場合は数字の選択肢を合わせて表示する
             * @param position タップした質問カードの位置
             */
            override fun contentTapped(position: Int) {

                val inflater = this@GameActivity.layoutInflater
                val dialogView = inflater.inflate(R.layout.question_card_layout, null)
                val text: TextView =
                    ViewBindings.findChildViewById<View>(
                        dialogView,
                        R.id.textViewQuestionText
                    ) as TextView
                val q = viewModel.getQuestion(position)
                text.text = q.text
                val selectable = q is QuestionWhereNoBySelect
                var selectPosition = 0

                val builder: AlertDialog.Builder = AlertDialog.Builder(this@GameActivity)
                builder
                    .setTitle("この質問で良いですか？")
                    .setView(dialogView)
                    .setPositiveButton("OK") { dialog, which ->
                        viewModel.onSelectQuestion(position, selectPosition)
                    }
                    .setNegativeButton("やめる") { dialog, which ->
                        // Do nothing.
                    }

                if (selectable) {
                    val items: Array<String> =
                        (q as QuestionWhereNoBySelect).selectNumbers.map { "${it} にする" }
                            .toTypedArray()
                    builder
                        .setSingleChoiceItems(
                            items, 0
                        ) { dialog, which ->
                            // Do something.
                            selectPosition = which
                        }
                }

                val dialog: AlertDialog = builder.create()
                dialog.show()
            }
        })

        binding.recyclerViewQuestions.adapter = fqcAdapter
        viewModel.fieldQuestions.observe(this, Observer {
            it.let {
                Log.d(Constant.LOG_TAG, "fieldQuestions.observe -> ${it.size}")
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
        viewModel.computerAutoPlay()
        Log.d(Constant.LOG_TAG, "onStart -- end")
    }
}

