package com.icloud.hashiguchi.minoru.mobile.ui.activities

import android.content.Intent
import android.content.pm.PackageInfo
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.icloud.hashiguchi.minoru.mobile.data.EventObserver
import com.icloud.hashiguchi.minoru.mobile.data.FirstOrSecondMove
import com.icloud.hashiguchi.minoru.mobile.data.GameViewModel
import com.icloud.hashiguchi.minoru.mobile.data.Level
import com.icloud.hashiguchi.minoru.mobile.data.StartViewModel
import com.icloud.hashiguchi.minoru.mobile.utils.ListViewableAdapter
import com.icloud.hashiguchi.minoru.mobile.utils.Logger
import com.icloud.hashiguchi.minoru.tagiron.constants.Constant
import com.icloud.hashiguchi.tagironmobile.R
import com.icloud.hashiguchi.tagironmobile.databinding.ActivityStartBinding

class StartActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    private var firstOrSecondMove = FirstOrSecondMove.RANDOM
    private var computerLevel = Level.NORMAL
    private lateinit var binding: ActivityStartBinding
    private lateinit var viewModel: StartViewModel
    private var clickCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // viewBinding初期化
        binding = ActivityStartBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // version
        val info: PackageInfo = packageManager.getPackageInfo(packageName, 0)
        binding.textViewVersion.text = "Ver.${info.versionName}"

        // viewModel初期化
        viewModel = ViewModelProvider(this)[StartViewModel::class.java]

        // スピナーの設定
        binding.spinnerFirstMoveLastAttack.adapter =
            ListViewableAdapter(this, Constant.FIRST_OR_SECOND_SPINNER_ITEMS.toMutableList())
        binding.spinnerComputerLevel.onItemSelectedListener = this
        binding.spinnerComputerLevel.adapter =
            ListViewableAdapter(this, Constant.COMPUTER_LEVEL_SPINNER_ITEMS.toMutableList())
        binding.spinnerComputerLevel.onItemSelectedListener = this

        // ゲーム画面への遷移の処理
        binding.buttonStart.setOnClickListener {
            Logger.i(
                Constant.LOG_TAG,
                "onClick start button : ${firstOrSecondMove}, $computerLevel"
            )
            viewModel.onClickButton(firstOrSecondMove, computerLevel)
        }
        viewModel.onMoveGameActivity.observe(this, EventObserver {
            val intent = Intent(this, GameActivity::class.java)
            intent.putExtra(GameViewModel.SEND_FIRST_MOVE, it.firstOrSecondMove.name)
            intent.putExtra(GameViewModel.SEND_COMPUTER_LEVEL, it.level.name)
            startActivity(intent)
        })

        // ログ表示画面への遷移
        binding.textViewVersion.setOnClickListener {
            if (clickCount < Constant.CLICK_COUNT_FOR_LOG_SCREEN) {
                clickCount++
            } else {
                clickCount = 0
                val intent = Intent(this, LogViewActivity::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
        when (parent.id) {
            R.id.spinnerFirstMoveLastAttack -> {
                firstOrSecondMove = parent.selectedItem as FirstOrSecondMove
            }

            R.id.spinnerComputerLevel -> {
                computerLevel = parent.selectedItem as Level
            }
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }
}