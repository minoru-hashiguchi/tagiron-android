package com.icloud.hashiguchi.minoru.mobile.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.icloud.hashiguchi.minoru.mobile.data.EventObserver
import com.icloud.hashiguchi.minoru.mobile.data.GameViewModel
import com.icloud.hashiguchi.minoru.mobile.data.StartViewModel
import com.icloud.hashiguchi.tagironmobile.R
import com.icloud.hashiguchi.tagironmobile.databinding.ActivityStartBinding
import kotlin.random.Random

class StartActivity : AppCompatActivity() {
    private var isFirstMove = true
    private lateinit var binding: ActivityStartBinding
    private lateinit var viewModel: StartViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // viewBinding初期化
        binding = ActivityStartBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // viewModel初期化
        viewModel = ViewModelProvider(this)[StartViewModel::class.java]

        binding.buttonStart.setOnClickListener {
            viewModel.onClickButton(isFirstMove)
        }

        var spinner = findViewById<Spinner>(R.id.planets_spinner)
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                isFirstMove = when (position) {
                    0 -> true
                    1 -> false
                    else -> Random.nextBoolean()
                }
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