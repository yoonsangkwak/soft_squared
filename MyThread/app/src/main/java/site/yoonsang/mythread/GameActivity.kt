package site.yoonsang.mythread

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.ViewTreeObserver
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import site.yoonsang.mythread.databinding.ActivityGameBinding
import java.util.*

class GameActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGameBinding
    private var time = 0
    private var readyTime = 3

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val readyDialog = ReadyDialog(this)
        readyDialog.binding.readyCount.text = readyTime.toString()
        readyDialog.show()

        val readyTimerTask = kotlin.concurrent.timer(initialDelay = 1000, period = 1000) {
            if (readyTime > -1) readyTime--
            runOnUiThread {
                when (readyTime) {
                    0 -> {
                        readyDialog.binding.readyCount.text = "Go!"
                    }
                    -1 -> {
                        readyDialog.dismiss()
                        cancel()
                        gameStart()
                    }
                    else -> {
                        readyDialog.binding.readyCount.text = readyTime.toString()
                    }
                }
            }
        }

        binding.gameRecyclerView.apply{
            layoutManager = GridLayoutManager(context, 5)
            adapter = TileAdapter(context)
        }
    }

    private fun gameStart() {
        val timerTask = kotlin.concurrent.timer(period = 10) {
            time++
            val sec = time / 100
            val mSec = time % 100

            runOnUiThread {
                binding.gameSecond.text = String.format("%03d", sec)
                binding.gameMilliSecond.text = String.format("%02d", mSec)
            }
        }
    }
}
