package site.yoonsang.mythread

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.recyclerview.widget.LinearLayoutManager
import site.yoonsang.mythread.databinding.HighScoreDialogBinding

class HighScoreDialog(context: Context): Dialog(context) {
    private val binding: HighScoreDialogBinding = HighScoreDialogBinding.inflate(layoutInflater)

    fun start() {
        setCanceledOnTouchOutside(false)
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setContentView(binding.root)

        val helper = DBHelper(context, DB_NAME, DB_VERSION)
        val recordList = helper.selectRecord()

        binding.highScoreRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = HighScoreAdapter(context, recordList)
        }

        binding.highScoreClose.setOnClickListener {
            dismiss()
        }
        show()
    }
}