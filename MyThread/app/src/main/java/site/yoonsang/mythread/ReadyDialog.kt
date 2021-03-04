package site.yoonsang.mythread

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import site.yoonsang.mythread.databinding.ReadyDialogBinding

class ReadyDialog(context: Context) : Dialog(context) {

    val binding: ReadyDialogBinding = ReadyDialogBinding.inflate(layoutInflater)

    init {
        setCancelable(false)
        setCanceledOnTouchOutside(false)
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setContentView(binding.root)
    }
}