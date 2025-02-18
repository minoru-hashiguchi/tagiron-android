package com.icloud.hashiguchi.minoru.mobile.ui.activities

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.icloud.hashiguchi.minoru.mobile.data.GameViewModel
import com.icloud.hashiguchi.minoru.mobile.utils.FieldQuestionCardsAdapter
import com.icloud.hashiguchi.minoru.mobile.utils.QuestionsSammaryAdapter
import com.icloud.hashiguchi.minoru.tagiron.constants.Constant
import com.icloud.hashiguchi.minoru.tagiron.questions.QuestionBase
import com.icloud.hashiguchi.minoru.tagiron.questions.QuestionWhereNoBySelect
import com.icloud.hashiguchi.tagironmobile.R
import com.icloud.hashiguchi.tagironmobile.databinding.ActivityGameBinding
import com.icloud.hashiguchi.tagironmobile.databinding.AnswerDialogLayoutBinding
import com.icloud.hashiguchi.tagironmobile.databinding.CallLayoutBinding
import com.icloud.hashiguchi.tagironmobile.databinding.QuestionCardLayoutBinding
import com.icloud.hashiguchi.tagironmobile.databinding.SimpleDialogLayoutBinding
import com.icloud.hashiguchi.tagironmobile.databinding.TilesLayoutBinding

class GameActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGameBinding
    private lateinit var viewModel: GameViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(Constant.LOG_TAG, "onCreate -- begin")
        super.onCreate(savedInstanceState)

        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(
            this,
            GameViewModel.Factory(intent)
        )[GameViewModel::class.java]
        binding.viewmodel = viewModel
        binding.lifecycleOwner = this

        // 場の質問カードのRecyclerViewの設定
        setupFieldQuestionCards(viewModel, binding)
        // プレイヤー用の質問履歴RecyclerViewの設定
        setupQuestionHistory(
            viewModel.playerQuestionHistory,
            binding.recyclerViewPlayerQuestionHistory
        )
        // コンピュータ用の質問履歴RecyclerViewの設定
        setupQuestionHistory(
            viewModel.computerQuestionsHistory,
            binding.recyclerViewComputerQuestionHistory
        )
        // コンピュータが選択する質問の監視
        viewModel.computerSelectedQuestion.observe(this) {
            Log.d(
                Constant.LOG_TAG,
                "comSelectedQuestion.observe -- ${it?.text}"
            )
            // コンピュータが選択した質問が更新された場合、質問表示ダイアログをモーダル表示する
            if (it != null) {
                // 表示が早すぎるので、気持ちDelayを入れる
                Thread.sleep(1000)
                val question = viewModel.onComputerSelectedQuestion(it)
                showModalDialogComputerSelectedQuestion(question, viewModel)
            }
        }
        // コンピュータが宣言するタイルの監視
        viewModel.computerCalledTiles.observe(this) {
            if (it.isNotEmpty()) {
                val inflater = this@GameActivity.layoutInflater
                val binding: CallLayoutBinding = CallLayoutBinding.inflate(inflater)
                binding.viewmodel = viewModel
                binding.tileList = it
                binding.lifecycleOwner = this
                val builder: AlertDialog.Builder = AlertDialog.Builder(this@GameActivity)
                builder
                    .setCancelable(false)
                    .setTitle("相手が宣言しました")
                    .setView(binding.root)
                    .setNegativeButton("OK") { dialog, which ->
                        // Do nothing.
                    }
                val dialog: AlertDialog = builder.create()
                dialog.show()
            }
        }

        // ゲーム開始の最初だけダイアログを表示
        if (viewModel.isShownInitDialog.value == false) {
            val name: String = if (viewModel.isFirstMove.value == true) "あなた" else "相手"
            showSimpleModalDialog(
//                getString(R.string.dialog_title_game_start),
                name + getString(R.string.message_game_start),
                "",
                false,
                { viewModel.onClickInitDialogButton() })

        }

        // OSの戻るボタン押下時のコールバックを登録
        onBackPressedDispatcher.addCallback(callbackBackPressed)
    }

    val callbackBackPressed = object : OnBackPressedCallback(true) {
        //コールバックのhandleOnBackPressedを呼び出して、戻るキーを押したときの処理を記述
        override fun handleOnBackPressed() {
            val builder: AlertDialog.Builder = AlertDialog.Builder(this@GameActivity)
            builder
                .setTitle("ゲームを終了しますか？")
                .setPositiveButton("終わる") { dialog, which ->
                    finish()
                }
                .setNegativeButton("まだ続ける") { dialog, which ->
                    // Do nothing.
                }

            val dialog: AlertDialog = builder.create()
            dialog.show()
            return
        }
    }

    private fun setupFieldQuestionCards(
        viewModel: GameViewModel,
        binding: ActivityGameBinding
    ) {

        val adapter = FieldQuestionCardsAdapter(viewModel.fieldQuestions)
        binding.recyclerViewQuestions.adapter = adapter
        binding.recyclerViewQuestions.layoutManager = GridLayoutManager(
            this,
            2,
            RecyclerView.VERTICAL,
            false
        )

        adapter.setListener(object :
            FieldQuestionCardsAdapter.FieldQuestionCardsAdapterListener {

            /**
             * 場の質問カードを選択したとき、確認ダイアログを表示する</br>
             *
             * この際、選択式の質問の場合は数字の選択肢を合わせて表示する
             * @param position タップした質問カードの位置
             */
            override fun contentTapped(position: Int) {

                if (viewModel.isPlaying.value == true) {
                    val question = viewModel.getQuestion(position)
                    var selectPosition = 0
                    val items: Array<String> = if (question is QuestionWhereNoBySelect) {
                        question.selectNumbers.map { "${it} にする" }.toTypedArray()
                    } else {
                        arrayOf()
                    }

                    val inflater = this@GameActivity.layoutInflater
                    val binding: QuestionCardLayoutBinding =
                        QuestionCardLayoutBinding.inflate(inflater)
                    binding.textViewQuestionText.text = question.text
                    val builder: AlertDialog.Builder = AlertDialog.Builder(this@GameActivity)
                    builder
                        .setTitle(getString(R.string.confirm_message_on_ask_question))
                        .setView(binding.root)
                        .setPositiveButton(getString(R.string.button_label_ok)) { dialog, which ->
                            var picked = viewModel.onSelectQuestion(position, selectPosition)
                            showModalDialogOnAnswerReceived(picked, viewModel)
                        }
                        .setNegativeButton(R.string.button_label_cancel) { dialog, which ->
                            // Do nothing.
                        }
                    if (items.isNotEmpty()) {
                        builder.setSingleChoiceItems(items, 0) { dialog, which ->
                            selectPosition = which
                        }
                    }
                    val dialog: AlertDialog = builder.create()
                    dialog.show()
                } else {
                    // プレイ終了後の質問カードのタップを抑止する
                }
            }
        })

        binding.recyclerViewQuestions.adapter = adapter
        viewModel.fieldQuestions.observe(this, Observer {
            it.let {
                Log.d(Constant.LOG_TAG, "fieldQuestions.observe -> ${it.size}")
                adapter.data = it
            }
        })
    }

    private fun setupQuestionHistory(
        liveData: LiveData<MutableList<QuestionBase>>,
        recyclerView: RecyclerView
    ) {
        val adapter = QuestionsSammaryAdapter(liveData)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        val separate = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        recyclerView.addItemDecoration(separate)

        recyclerView.adapter = adapter
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

    fun onClickCall(view: View) {
        Log.d(Constant.LOG_TAG, "onClickCall -- begin")
        val viewModel by viewModels<GameViewModel>()
        if (viewModel.isFailedCheckThinkingTiles()) {
            showSimpleModalDialog(getString(R.string.ng_message_on_call_check), "", true, {})
            return
        }

        val inflater = this@GameActivity.layoutInflater
        val binding: TilesLayoutBinding = TilesLayoutBinding.inflate(inflater)
        binding.viewmodel = viewModel
        binding.tileList = viewModel.thinkingTiles.value
        binding.lifecycleOwner = this
        val builder: AlertDialog.Builder = AlertDialog.Builder(this@GameActivity)
        builder
            .setTitle(getString(R.string.confirm_message_on_call))
            .setView(binding.root)
            .setPositiveButton(getString(R.string.button_label_ok)) { dialog, which ->
                // プレイヤーの宣言後に、結果をモーダルダイアログに表示する
                // 宣言が失敗した時は、相手プレイヤーのターンへと移行する
                val isSucceed = viewModel.onCall()
                val title = getString(R.string.dialog_title_called)
                val text: String
                val callback: () -> Unit
                if (isSucceed) {
                    text = getString(R.string.message_on_player_call_succeed)
                    callback = { viewModel.finalize(true) }
                } else {
                    text = getString(R.string.message_on_player_call_failed)
                    callback = { viewModel.finalizePlayerTurn() }
                }
                showSimpleModalDialog(title, text, false, callback)
            }
            .setNegativeButton(getString(R.string.button_label_no)) { dialog, which ->
                // Do nothing.
            }
        val dialog: AlertDialog = builder.create()
        dialog.show()
        Log.d(Constant.LOG_TAG, "onClickCall -- end")
    }

    private fun showSimpleModalDialog(
        title: String,
        text: String,
        cancelable: Boolean,
        callback: () -> Unit
    ) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this@GameActivity)
        if (text.isNotEmpty()) {
            val inflater = this@GameActivity.layoutInflater
            val binding: SimpleDialogLayoutBinding = SimpleDialogLayoutBinding.inflate(inflater)
            binding.lifecycleOwner = this
            binding.messageColor = getColor(R.color.white)
            binding.textViewOnCallResult.text = text
            builder.setView(binding.root)
        }
        builder
            .setCancelable(cancelable)
            .setTitle(title)
            .setPositiveButton(getString(R.string.button_label_ok)) { dialog, which ->
                callback()
            }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    /**
     * 相手の質問に対する解答をモーダル表示する
     *
     * モーダル終了後、共有情報カードの場合はその解答モーダルを表示、それ以外は相手のターンに移行する
     */
    private fun showModalDialogOnAnswerReceived(picked: QuestionBase, viewModel: GameViewModel) {
        val inflater = this@GameActivity.layoutInflater
        val binding: AnswerDialogLayoutBinding = AnswerDialogLayoutBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.textViewMessage.text = picked.answerText
        binding.messageColor = getColor(R.color.red)
        var text = picked.text
        if (picked is QuestionWhereNoBySelect) {
            text += "\n︎" + getString(R.string.selectable_question_text_signal) + picked.summaryText
        }
        binding.textViewQuestionText.text = text
        val builder: AlertDialog.Builder = AlertDialog.Builder(this@GameActivity)
        builder
            .setCancelable(false)
            .setTitle(getString(R.string.dialog_title_answer))
            .setView(binding.root)
            .setPositiveButton(getString(R.string.button_label_ok)) { dialog, which ->
                val sharedQuestion = viewModel.doShareableQuestionAfterOnSelectQuestion(picked)
                if (sharedQuestion != null) {
                    showModalDialogSharedQuestion(
                        sharedQuestion,
                        R.string.dialog_title_shared_answer,
                        { viewModel.finalizePlayerTurn() }
                    )
                } else {
                    viewModel.finalizePlayerTurn()
                }
            }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    /**
     * プレイヤーが共有情報カードを選んだ時の、相手プレイヤーへの解答内容をモーダル表示する
     *
     * モーダル終了後、相手のターンに移行する
     */
    private fun showModalDialogSharedQuestion(
        sharedQuestion: QuestionBase,
        titleResourceId: Int,
        afterExecute: () -> Unit
    ) {
        val inflater = this@GameActivity.layoutInflater
        val binding: AnswerDialogLayoutBinding = AnswerDialogLayoutBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.textViewMessage.text = sharedQuestion.answerText
        binding.messageColor = getColor(R.color.red)
        binding.question = sharedQuestion
        val builder: AlertDialog.Builder = AlertDialog.Builder(this@GameActivity)
        builder
            .setCancelable(false)
            .setTitle(getString(titleResourceId))
            .setView(binding.root)
            .setPositiveButton(getString(R.string.button_label_ok)) { dialog, which ->
                afterExecute()
            }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    /**
     * コンピュータからの質問とその解答をモーダルダイアログに表示する
     *
     * モーダル終了後、共有情報カードの場合はその解答モーダルを表示、それ以外は相手のターンに移行する
     * @param question 解答付きの質問カード
     * @param viewModel ゲーム画面のViewModel
     */
    private fun showModalDialogComputerSelectedQuestion(
        question: QuestionBase,
        viewModel: GameViewModel
    ) {
        var text = question.text
        if (question is QuestionWhereNoBySelect) {
            text += "\n︎" + getString(R.string.selectable_question_text_signal) + question.summaryText
        }
        val inflater = this@GameActivity.layoutInflater
        val binding: AnswerDialogLayoutBinding = AnswerDialogLayoutBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.textViewQuestionText.text = text
        binding.textViewMessage.text = question.answerText
        binding.messageColor = getColor(R.color.red)
        val builder: AlertDialog.Builder = AlertDialog.Builder(this@GameActivity)
        builder
            .setCancelable(false)
            .setTitle(getString(R.string.dialog_title_received_question))
            .setView(binding.root)
            .setPositiveButton(getString(R.string.button_label_ok)) { dialog, which ->
                val sharedQuestion =
                    viewModel.doShareableQuestionAfterOnComputerSelectQuestion(question)
                if (sharedQuestion != null) {
                    showModalDialogSharedQuestion(
                        sharedQuestion,
                        R.string.dialog_title_shared_answer_from,
                        { viewModel.finalizeComputerTurn() }
                    )
                } else {
                    viewModel.finalizeComputerTurn()
                }
            }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
}