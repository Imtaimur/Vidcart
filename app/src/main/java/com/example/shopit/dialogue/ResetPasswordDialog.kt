package com.example.shopit.dialogue

import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.example.shopit.R
import com.google.android.material.bottomsheet.BottomSheetDialog

fun Fragment.setupBottomsheetDialog(
    onSendClick:(String) ->Unit
){

    val dialog=BottomSheetDialog(requireContext(),R.style.DialogStyle)
    val view=layoutInflater.inflate(R.layout.reset_password_dialogue,null)
    dialog.setContentView(view)
    dialog.show()
    val edEmail=view.findViewById<EditText>(R.id.edtext)
    val buttonreset=view.findViewById<Button>(R.id.resetbutton)
    val buttoncancel=view.findViewById<Button>(R.id.cancelbutton)
    buttonreset.setOnClickListener{
        val email=edEmail.text.trim()
        onSendClick(email.toString())
        dialog.dismiss()

    }
    buttoncancel.setOnClickListener{
        dialog.dismiss()
    }

}