package site.yoonsang.myactivitylifecycle

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.util.Util
import site.yoonsang.myactivitylifecycle.databinding.ActivityPlayerBinding

class PlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlayerBinding
    private val fragmentBasic by lazy { PlayerBasicFragment() }
    private var player: SimpleExoPlayer? = null
    private var playWhenReady = true
    private var playbackPosition: Long = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.player_fragment_container, fragmentBasic)
            .commit()

        val bundle = Bundle()
        bundle.putString("title", intent.getStringExtra("title"))
        bundle.putString("viewCount", intent.getStringExtra("viewCount"))
        bundle.putString("date", intent.getStringExtra("date"))
        fragmentBasic.arguments = bundle
    }

    override fun onStart() {
        super.onStart()
        if (Util.SDK_INT >= 24) initializePlayer()
    }

    override fun onResume() {
        super.onResume()
        if (Util.SDK_INT < 24 || player == null) initializePlayer()
    }

    override fun onPause() {
        super.onPause()
        if (Util.SDK_INT < 24) releasePlayer()
    }

    override fun onStop() {
        super.onStop()
        if (Util.SDK_INT >= 24) releasePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        playbackPosition = 0L
        MyApplication.prefs.setLong(intent.getStringExtra("title").toString(), playbackPosition)
    }

    override fun onRestart() {
        super.onRestart()
        if (!playWhenReady) playWhenReady = true
        Toast.makeText(this, "마지막 시청 시점부터 이어보기 됩니다.", Toast.LENGTH_SHORT).show()
    }

    private fun initializePlayer() {
        player = SimpleExoPlayer.Builder(this).build()
        binding.exoplayerView.player = player
        val mediaItem: MediaItem = MediaItem.fromUri(getString(R.string.other_url_mp3))
        playbackPosition = MyApplication.prefs.getLong(intent.getStringExtra("title").toString())
        player?.let {
            it.setMediaItem(mediaItem)
            it.playWhenReady = playWhenReady
            it.seekTo(playbackPosition)
            it.prepare()
        }
    }

    private fun releasePlayer() {
        player?.let {
            playWhenReady = it.playWhenReady
            playbackPosition = it.currentPosition
            MyApplication.prefs.setLong(intent.getStringExtra("title").toString(), playbackPosition)
            it.release()
            player = null
        }
    }
}