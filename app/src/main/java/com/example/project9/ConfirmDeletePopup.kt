package com.example.project9

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.DialogFragment

class ConfirmDeletePopup (val imageId : String, val clickListener: (imageId: String) -> Unit) : DialogFragment() {
    val TAG = "ConfirmDeletePopup"
    interface myClickListener {
        fun yesPressed()
    }

    var listener: myClickListener? = null

    //confirm delete pop up config
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        AlertDialog.Builder(requireContext())
            .setMessage("Are you sure you want to delete this image?")
            .setPositiveButton("Yes") { _,_ -> clickListener(imageId)}
            .setNegativeButton("No") { _,_ -> }

            .create()

    companion object {
        const val TAG = "ConfirmDeletePopup"
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = context as myClickListener
        } catch (e: Exception) {
            Log.d("ConfirmDeletePopup", e.message.toString())
        }
    }

}