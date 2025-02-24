package com.icloud.hashiguchi.minoru.mobile.ui.activities

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.icloud.hashiguchi.tagironmobile.R

class LogViewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_log_view)

        var files: Array<String> = fileList()
        var textView = findViewById<TextView>(R.id.textViewLog)
        var filename = files.filter { it.matches(Regex(".+\\.log")) }.toMutableList().get(0)
        textView.text = openFileInput(filename).bufferedReader().readText()
    }
}