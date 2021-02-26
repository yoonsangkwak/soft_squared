package site.yoonsang.mythread

import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
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

        val tileAdapter = TileAdapter(this)

        for (i in 1..25) {
            tileAdapter._1to25.add(i)
            tileAdapter._26to50.add(i + 25)
        }

        for (i in 0..24) {
            tileAdapter.visible.add(i, View.VISIBLE)
        }

        while (tileAdapter._1to25.size > 0) {
            val rand: Int = Random().nextInt(tileAdapter._1to25.size)
            if (tileAdapter._1to25[rand] !in tileAdapter._1to50) {
                tileAdapter.init1to25(tileAdapter._1to25[rand])
                tileAdapter._1to25.removeElement(tileAdapter._1to25[rand])
                tileAdapter.notifyDataSetChanged()
            }
        }

        binding.gameRecyclerView.addOnItemTouchListener(object : RecyclerView.OnItemTouchListener {
            override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                if (e.action == MotionEvent.ACTION_UP) {
                    val child = rv.findChildViewUnder(e.x, e.y) as AppCompatButton?
                    if (child != null) {
                        val selected = Integer.parseInt(child.text.toString())
                        if (selected == tileAdapter.now) {
                            val position = rv.getChildAdapterPosition(child)
                            if (selected >= 26 && selected == tileAdapter.now) tileAdapter.setUpVisible(position)
                            tileAdapter.now++
                            binding.gameTargetNumber.text = tileAdapter.now.toString()
                            if (tileAdapter._26to50.size > 0) {
                                val rand: Int = Random().nextInt(tileAdapter._26to50.size)
                                tileAdapter.update26to50(position, tileAdapter._26to50[rand])
                                tileAdapter._26to50.removeElement(tileAdapter._26to50[rand])
                            }
                            tileAdapter.notifyItemChanged(position)
                        }
                    }
                }
                return false
            }

            override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}

            override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}
        })
        binding.gameRecyclerView.apply {
            layoutManager = GridLayoutManager(context, 5)
            adapter = tileAdapter
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
