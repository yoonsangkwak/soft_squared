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
    private var now = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val _1to25 = Vector<Int>()
        val _26to50 = Vector<Int>()

        for (i in 1..25) {
            _1to25.add(i)
            _26to50.add(i + 25)
        }

        val tileAdapter = TileAdapter(this)

        for (i in 1..25) {
            val rand: Int = (Math.random() * _1to25.size).toInt()
            tileAdapter.init1to25(_1to25[rand])
            _1to25.remove(rand)
            tileAdapter.notifyDataSetChanged()
        }

        binding.gameRecyclerView.addOnItemTouchListener(object :
            RecyclerView.OnItemTouchListener {
            override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                if (e.action == MotionEvent.ACTION_UP) {
                    val child: AppCompatButton? = rv.findChildViewUnder(e.x, e.y) as AppCompatButton?
                    if (child != null) {
                        val selected = Integer.parseInt(child.text.toString())
                        if (selected == now) {
                            val position = rv.getChildAdapterPosition(child)
                            if (selected >= 26 && selected == now) tileAdapter.setUpVisible(position)
                            now++
                            val rand: Int = (Math.random() * _26to50.size).toInt()
                            tileAdapter.update26to50(position, _26to50[rand])
                            _26to50.remove(rand)
                            tileAdapter.notifyItemChanged(position)
                        }
                    }
                }
                return false
            }

            override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}

            override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}
        })

        binding.gameRecyclerView.viewTreeObserver.addOnGlobalLayoutListener {
            val width: Int = binding.gameRecyclerView.width / 5
            val height: Int = binding.gameRecyclerView.width / 5
            tileAdapter.setLength(width, height)
            tileAdapter.notifyDataSetChanged()
        }

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
