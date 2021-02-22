package site.yoonsang.activitylifecycle

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import site.yoonsang.activitylifecycle.databinding.ActivityTalkBinding

class TalkActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTalkBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("checkkk", "Talk onCreate")
        super.onCreate(savedInstanceState)
        binding = ActivityTalkBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val name = intent.getStringExtra("name")
        val message = intent.getStringExtra("message")
        val date = intent.getStringExtra("date")

        binding.tvTalkTitleName.text = name
        binding.tvTalkFriendName.text = name
        binding.tvTalkMyChat.text = message
        binding.tvTalkFriendTime.text = date
        binding.tvTalkMyTime.text = date
    }

    override fun onStart() {
        Log.d("checkkk", "Talk onStart")
        super.onStart()
    }

    override fun onResume() {
        Log.d("checkkk", "Talk onResume")
        super.onResume()
    }

    override fun onPause() {
        Log.d("checkkk", "Talk onPause")
        super.onPause()
    }

    override fun onStop() {
        Log.d("checkkk", "Talk onStop")
        super.onStop()
    }

    override fun onDestroy() {
        Log.d("checkkk", "Talk onDestroy")
        super.onDestroy()
    }

    override fun onRestart() {
        Log.d("checkkk", "Talk onRestart")
        super.onRestart()
    }
}