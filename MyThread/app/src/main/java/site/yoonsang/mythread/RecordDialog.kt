package site.yoonsang.mythread

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.widget.Toast
import site.yoonsang.mythread.databinding.RecordDialogBinding

class RecordDialog(context: Context, private val time: Int) : Dialog(context) {
    private val binding: RecordDialogBinding = RecordDialogBinding.inflate(layoutInflater)
    private lateinit var listener: RecordOKClickedListener

    fun start() {
        setCanceledOnTouchOutside(false)
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setContentView(binding.root)

        val sec = time / 100
        val mSec = time % 100
        val stringSec = String.format("%03d", sec)
        val stringMSec = String.format("%02d", mSec)

        binding.recordTimeSecond.text = stringSec
        binding.recordTimeMilliSecond.text = stringMSec

        binding.recordOk.setOnClickListener {
            if (binding.recordEditName.text.isNotEmpty()) {
                val name = binding.recordEditName.text.toString()
                val score = "${stringSec}.${stringMSec}"
                val helper = DBHelper(context, DB_NAME, DB_VERSION)
                helper.insertRecord(Record(name, time, score))
                listener.onOKClicked()
                dismiss()
            } else {
                Toast.makeText(context, "기록할 이름을 입력해주세요.", Toast.LENGTH_SHORT).show()
            }
        }

        show()
    }

    fun setOnOKClickedListener(listener: () -> Unit) {
        this.listener = object : RecordOKClickedListener {
            override fun onOKClicked() {
                listener()
            }
        }
    }

    interface RecordOKClickedListener {
        fun onOKClicked()
    }

    override fun onBackPressed() {}
}