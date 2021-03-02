package site.yoonsang.mythread

import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import site.yoonsang.mythread.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var mediaPlayer: MediaPlayer? = null
    private var playbackPosition: Int = 0
    private var bgmIsOn = MyApplication.prefs.getBoolean("bgm")

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.MyThread)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.mainMenuStart.setOnClickListener {
            val intent = Intent(this, GameActivity::class.java)
            startActivity(intent)
        }

        binding.mainMenuSound.setOnClickListener {
            val soundDialog = SoundDialog(this)
            soundDialog.setOnOKClickedListener {
                bgmIsOn = it
                if (bgmIsOn) initializePlayer()
                else releasePlayer()
            }
            soundDialog.start()
        }

        binding.mainMenuHighScores.setOnClickListener {
            val highScoreDialog = HighScoreDialog(this)
            highScoreDialog.start()
        }
    }

    override fun onResume() {
        super.onResume()
        if (bgmIsOn) initializePlayer()
    }

    override fun onPause() {
        super.onPause()
        if (bgmIsOn) releasePlayer()
    }

    private fun initializePlayer() {
        if (mediaPlayer == null) mediaPlayer = MediaPlayer.create(this, R.raw.newbarktown)
        if (mediaPlayer != null) {
            if (!mediaPlayer!!.isPlaying) {
                mediaPlayer?.isLooping = true
                mediaPlayer?.seekTo(playbackPosition)
                mediaPlayer?.start()
            }
        }
    }

    private fun releasePlayer() {
        if (mediaPlayer != null) {
            if (mediaPlayer!!.isPlaying) {
                playbackPosition = mediaPlayer!!.currentPosition
                mediaPlayer!!.stop()
                mediaPlayer!!.release()
                mediaPlayer = null
            }
        }
    }
}