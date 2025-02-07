package com.icloud.hashiguchi.tagironmobile.ui.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.icloud.hashiguchi.tagironmobile.R

class StartActivity : Activity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        val button = findViewById<Button>(R.id.buttonStart)
        button.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        val intent = Intent(this, GameActivity::class.java)
        startActivity(intent)
    }
}