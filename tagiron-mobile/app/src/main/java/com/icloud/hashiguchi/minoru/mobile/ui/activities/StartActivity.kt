package com.icloud.hashiguchi.minoru.mobile.ui.activities

import android.content.Intent
import android.content.pm.PackageInfo
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.icloud.hashiguchi.minoru.mobile.data.EventObserver
import com.icloud.hashiguchi.minoru.mobile.data.GameViewModel
import com.icloud.hashiguchi.minoru.mobile.data.StartViewModel
import com.icloud.hashiguchi.tagironmobile.R
import com.icloud.hashiguchi.tagironmobile.databinding.ActivityStartBinding

class StartActivity : AppCompatActivity() {
    private var firstMoveOrLastAtackNo = 0
    private lateinit var binding: ActivityStartBinding
    private lateinit var viewModel: StartViewModel

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

        binding.buttonStart.setOnClickListener {
            viewModel.onClickButton(firstMoveOrLastAtackNo)
        }

        var spinner = findViewById<Spinner>(R.id.planets_spinner)
        spinner.adapter =
            ArrayAdapter(
                this,
                R.layout.view_spinner_item_layout,
                resources.getTextArray(R.array.first_move_last_attack_list)
            )
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                firstMoveOrLastAtackNo = position
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }

        // 画面遷移の処理
        viewModel.onMoveGameActivity.observe(this, EventObserver {
            val intent = Intent(this, GameActivity::class.java)
            intent.putExtra(GameViewModel.SEND_MESSAGE, it)
            startActivity(intent)
        })
    }

//    override fun onClick(v: View?) {
//        val intent = Intent(this, GameActivity::class.java)
//        intent.putExtra(Constant.INTENT_KEY_IS_FIRST_MOVE, isFirstMove)
//        startActivity(intent)
//    }

//    @SuppressLint("RestrictedApi")
//    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
//        Toast.makeText(
//            this,
//            "${parent?.selectedItem}が選択されました [${position}]",
//            Toast.LENGTH_SHORT
//        ).show()
//    }
}