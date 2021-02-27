package site.yoonsang.mythread

import android.media.SoundPool
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
    private var hintTime = 0
    private lateinit var timerTask: Timer
    private lateinit var readyTimerTask: Timer
    private lateinit var hintTimerTask: Timer
    private val sef = MyApplication.prefs.getBoolean("sef")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val soundPool = SoundPool.Builder().build()
        val soundSuccess = soundPool.load(this, R.raw.success_sound, 1)
        val soundFail = soundPool.load(this, R.raw.fail_sound, 1)

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
                if (e.action == MotionEvent.ACTION_DOWN) {
                    val child = rv.findChildViewUnder(e.x, e.y) as AppCompatButton?
                    if (child != null) {
                        val selected = Integer.parseInt(child.text.toString())
                        if (selected == tileAdapter.now) {
                            val position = rv.getChildAdapterPosition(child)
                            if (selected >= 26 && selected == tileAdapter.now) tileAdapter.setUpVisible(position)
                            tileAdapter.now++
                            if (tileAdapter.now < 51) binding.gameTargetNumber.text = tileAdapter.now.toString()
                            if (tileAdapter._26to50.size > 0) {
                                val rand: Int = Random().nextInt(tileAdapter._26to50.size)
                                tileAdapter.update26to50(position, tileAdapter._26to50[rand])
                                tileAdapter._26to50.removeElement(tileAdapter._26to50[rand])
                            }
                            if (sef) soundPool.play(soundSuccess, 1f, 1f, 0, 0, 1f)
                            tileAdapter.notifyItemChanged(position)
                        } else {
                            if (sef) soundPool.play(soundFail, 1f, 1f, 0, 0, 1f)
                        }
                    }
                    if (tileAdapter.now == 51) {
                        timerTask.cancel()
                        val recordDialog = RecordDialog(this@GameActivity, time)
                        recordDialog.setOnOKClickedListener {
                            finish()
                        }
                        recordDialog.start()
                    }
                }
                return true
            }

            override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}

            override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}
        })

        binding.gameRecyclerView.apply {
            layoutManager = GridLayoutManager(context, 5)
            adapter = tileAdapter
        }
    }

    override fun onResume() {
        super.onResume()
        gameReady()
    }

    override fun onPause() {
        super.onPause()
        timerTask.cancel()
        readyTime = 3
    }

    private fun gameReady() {
        val readyDialog = ReadyDialog(this)
        readyDialog.binding.readyCount.text = readyTime.toString()
        readyDialog.show()

        readyTimerTask = kotlin.concurrent.timer(initialDelay = 1000, period = 1000) {
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
    }

    private fun gameStart() {
        timerTask = kotlin.concurrent.timer(period = 10) {
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