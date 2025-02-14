package com.icloud.hashiguchi.minoru.mobile.ui.activities

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.DialogFragment
import com.icloud.hashiguchi.minoru.tagiron.constants.Constant

class SampleDialog : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            var a = arrayOf("a", "b")
            // Use the Builder class for convenient dialog construction.
            val builder = AlertDialog.Builder(it)
            builder
                .setMessage("Start game")
                .setPositiveButton("Start1") { dialog, id ->
                    // START THE GAME!
                    Log.d(Constant.LOG_TAG, "id:${id}")
                }
                .setNegativeButton("やめる") { dialog, id ->
                    // User cancelled the dialog.
                }
                .setSingleChoiceItems(
                    arrayOf("Item One", "Item Two", "Item Three"), 0
                ) { dialog, which ->
                    // Do something.
                }

            // Create the AlertDialog object and return it.
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}