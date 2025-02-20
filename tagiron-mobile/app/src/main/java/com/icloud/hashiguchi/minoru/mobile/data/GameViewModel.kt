package com.icloud.hashiguchi.minoru.mobile.data

import android.content.Intent
import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.map
import com.icloud.hashiguchi.minoru.tagiron.TileViewModel
import com.icloud.hashiguchi.minoru.tagiron.constants.Color
import com.icloud.hashiguchi.minoru.tagiron.constants.Constant
import com.icloud.hashiguchi.minoru.tagiron.questions.QuestionBase
import com.icloud.hashiguchi.minoru.tagiron.questions.ShareableQuestion
import java.util.Objects
import kotlin.random.Random


class GameViewModel(intent: Intent) : ViewModel() {
    // ViewModelに引数を追加する設定
    class Factory(private val intent: Intent) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            GameViewModel(intent) as T
    }

    companion object {
        const val SEND_MESSAGE = "SEND_MESSAGE"
    }

    private val _isFirstMove: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>(true) }
    private var _isPlaying = MutableLiveData(true)
    private var _isQuestion = MutableLiveData(true)
    private var _gameTiles = Constant.TILES.toMutableList()
    private var _gameQuestions = Constant.QUESTIONS.toMutableList()
    private var _thinkingTiles = MutableLiveData<MutableList<TileViewModel>>(
        mutableListOf(
            TileViewModel(),
            TileViewModel(),
            TileViewModel(),
            TileViewModel(),
            TileViewModel()
        )
    )
    private var _fieldQuestions = MutableLiveData<MutableList<QuestionBase>>(mutableListOf())
    private var _selectedThinkingTilePosition = MutableLiveData<Int>(null)
    private var _computerSelectedQuestion = MutableLiveData<QuestionBase?>(null)
    private var _computerCalledTiles = MutableLiveData<MutableList<TileViewModel>>()
    private var _isMyTurn = MutableLiveData(true)
    private var _turnCount = MutableLiveData(1)
    private var _isPlayerWin = MutableLiveData(false)
    private var _isShownInitDialog = MutableLiveData<Boolean>(false)

    private var me = HumanPlayer("あなた")
    private var you = ComputerPlayer("相手")

    init {
        val firstMoveOrLastAtackNo = intent.getIntExtra(SEND_MESSAGE, 0)
        _isFirstMove.value = when (firstMoveOrLastAtackNo) {
            0 -> true
            1 -> false
            else -> Random.nextBoolean()
        }

        // シャッフル
        _gameTiles.shuffle()
        _gameQuestions.shuffle()

        listOf(me, you).forEach {
            it.pickTilesAfterThatSort(_gameTiles)
            it.createTilePattern()
        }

        replenishQuestions()
        Log.d(Constant.LOG_TAG, "_isPlaying=${_isPlaying.value}, _isQuestion=${_isQuestion.value}")
    }

    val ownTiles: LiveData<MutableList<TileViewModel>> = me.ownTiles
    val computerTiles: LiveData<MutableList<TileViewModel>> = you.ownTiles
    val fieldQuestions: LiveData<MutableList<QuestionBase>> = _fieldQuestions
    val playerQuestionHistory: LiveData<MutableList<QuestionBase>> = me.questionsAndAnswers
    val computerQuestionsHistory: LiveData<MutableList<QuestionBase>> = you.questionsAndAnswers
    val thinkingTiles: LiveData<MutableList<TileViewModel>> = _thinkingTiles
    val showQuestionSelector: LiveData<Boolean> = _isQuestion
    val showCallEditor: LiveData<Boolean> = _isQuestion.map { !it }
    val selectedTilePosition: LiveData<Int> = _selectedThinkingTilePosition
    val computerSelectedQuestion: LiveData<QuestionBase?> = _computerSelectedQuestion
    val isPlaying: LiveData<Boolean> = _isPlaying
    val computerCalledTiles: LiveData<MutableList<TileViewModel>> = _computerCalledTiles
    val isMyTurn: LiveData<Boolean> = _isMyTurn
    var isFirstMove: LiveData<Boolean> = _isFirstMove
    val turnCount: LiveData<Int> = _turnCount
    val isPlayerWin: LiveData<Boolean> = _isPlayerWin
    val isShownInitDialog: LiveData<Boolean> = _isShownInitDialog

    fun onClickInitDialogButton() {
        _isShownInitDialog.postValue(true)
        _isPlaying.postValue(true)
        if (_isFirstMove.value == false) {
            computerAutoPlay()
        }
    }

    fun onClickSelectQestion(view: View) {
        _isQuestion.postValue(true)
    }

    fun onClickSelectCall(view: View) {
        _isQuestion.postValue(false)
    }

    /**
     * 予想中のタイルをタップした時の処理
     *
     * プレイ中のみ、タップしたタイル下に編集中カーソルを表示する
     */
    fun onClickThinkingTile(number: Int) {
        if (_isPlaying.value == true) {
            _selectedThinkingTilePosition.postValue(number)
        }
    }

    fun onClickNumber(number: Int) {
        var tile = _selectedThinkingTilePosition.value?.let { _thinkingTiles.value?.get(it - 1) }
        if (tile != null) {
            tile.setNo(number)
        }
    }

    fun onClickColor(strColor: String) {
        var tile = _selectedThinkingTilePosition.value?.let { _thinkingTiles.value?.get(it - 1) }
        if (tile != null) {
            when (strColor) {
                "RED" -> tile.setColor(Color.RED)
                "BLUE" -> tile.setColor(Color.BLUE)
                "YELLOW" -> tile.setColor(Color.YELLOW)
            }
        }
    }

    fun onClickClear() {
        var tile = _selectedThinkingTilePosition.value?.let { _thinkingTiles.value?.get(it - 1) }
        if (tile != null) {
            tile.setNo(null)
            tile.setColor(null)
        }
    }

    fun getQuestion(index: Int): QuestionBase {
        return _fieldQuestions.value?.get(index)!!
    }

    /**
     * プレイヤーが質問を選択した時の処理
     *
     * @param index プレイヤーが選択した場のカードの場所
     * @param selected 選択式質問カードの選択済み要素番号
     * @return 解答付きの質問カード
     */
    fun onSelectQuestion(index: Int, selected: Int): QuestionBase {
        val picked = me.pickQuestion(index, _fieldQuestions)
        me.selectPosition = selected
        me.askQuestion(you.ownTiles.value!!, picked)
        return picked
    }

    /**
     * プレイヤーが質問を選択して解答を表示後の処理
     *
     * @param picked 選択した質問カード
     * @return 質問が共有情報カードの場合は複製した質問カード、そうでなければnullを返却
     */
    fun doShareableQuestionAfterOnSelectQuestion(picked: QuestionBase): QuestionBase? {
        var selectableQuestion: QuestionBase? = null
        // 共有情報カードの場合は自分も相手に回答する
        if (picked is ShareableQuestion) {
            selectableQuestion = picked.clone()
            you.askQuestion(me.ownTiles.value!!, selectableQuestion)
        }
        return selectableQuestion
    }

    /**
     * 自ターンの終了時処理
     *
     * 相手のターンに移行する
     */
    fun finalizePlayerTurn() {
        if (_isFirstMove.value == false) {
            _turnCount.postValue(_turnCount.value!! + 1)
        }
        replenishQuestions()
        computerAutoPlay()
    }

    private fun replenishQuestions() {
        Log.d(Constant.LOG_TAG, "replenishQuestions -- begin")
        val old = _fieldQuestions.value?.size
        // 不足数
        val count: Int = Constant.OPEN_QUESTIONS_COUNT - _fieldQuestions.value?.size!!
        for (i in 0 until count) {
            if (_gameQuestions.size == 0) {
                println("質問の山札なし")
                break
            }

            // 山札の先頭を場に追加
            _fieldQuestions.value!!.add(_gameQuestions.get(0))
            _fieldQuestions.postValue(_fieldQuestions.value)
            // 山札の先頭を削除
            _gameQuestions.removeAt(0)
        }
        Log.d(
            Constant.LOG_TAG,
            "replenishQuestions -- end (${old} -> ${_fieldQuestions.value!!.size})"
        )
    }

    private fun computerAutoPlay() {
        Log.d(Constant.LOG_TAG, "autoPlay -- begin")
        Log.d(Constant.LOG_TAG, "_isPlaying=${_isPlaying.value}")
        _isMyTurn.postValue(false)

        // 行動の決定
        val actionNo = you.selectAction(_fieldQuestions.value!!)

        if (actionNo == null) {
            // 宣言
            _computerCalledTiles.postValue(you.patterns.elementAt(0).toMutableList())
            val result = you.call(me.ownTiles.value!!)
            if (result) {
                finalize(false)
            } else {
                finalizeComputerTurn()
            }
        } else {
            // 質問
            val picked = you.pickQuestion(actionNo, _fieldQuestions)
            _computerSelectedQuestion.postValue(picked)

            Log.d(Constant.LOG_TAG, "autoPlay -- end")
        }
    }

    /**
     * コンピュータが質問を選択した時の処理
     *
     * @param picked コンピュータが選択した質問カード
     * @return 解答付きの質問カード
     */
    fun onComputerSelectedQuestion(picked: QuestionBase): QuestionBase {
        _computerSelectedQuestion.postValue(null)
        you.askQuestion(me.ownTiles.value!!, picked)
        return picked
    }

    /**
     * コンピュータの質問と解答を表示後の処理
     *
     * @param picked 選択した質問カード
     * @return 質問が共有情報カードの場合は複製した質問カード、そうでなければnullを返却
     */
    fun doShareableQuestionAfterOnComputerSelectQuestion(picked: QuestionBase): QuestionBase? {
        var selectableQuestion: QuestionBase? = null
        // 共有情報カードの場合は自分も相手に回答する
        if (picked is ShareableQuestion) {
            selectableQuestion = picked.clone()
            me.askQuestion(you.ownTiles.value!!, selectableQuestion)
        }
        return selectableQuestion
    }

    /**
     * コンピュータターンの終了時処理
     */
    fun finalizeComputerTurn() {
        if (_isFirstMove.value == true) {
            _turnCount.postValue(_turnCount.value!! + 1)
        }
        _isMyTurn.postValue(true)
        replenishQuestions()
    }

    /**
     * 予想タイルの入力チェックを実施する
     *
     * @return 番号か色どちらかが一つでも未入力の場合はNG
     */
    fun isFailedCheckThinkingTiles(): Boolean {
        val ngCount = _thinkingTiles.value?.filter {
            it.no.value == null || it.color.value == null
        }?.size
        return ngCount != 0
    }

    /**
     * プレイヤーが宣言する
     *
     * @return true: 成功、false: 失敗
     */
    fun onCall(): Boolean {
        val result = Objects.deepEquals(_thinkingTiles.value, you.ownTiles.value)
        return result
    }

    /**
     * 勝敗が決した時の終了処理
     *
     * @param isPlayerWin プレイヤーの勝敗
     */
    fun finalize(isPlayerWin: Boolean) {
        _isPlaying.postValue(false)
        _isPlayerWin.postValue(isPlayerWin)
        _selectedThinkingTilePosition.postValue(-1)
    }
}