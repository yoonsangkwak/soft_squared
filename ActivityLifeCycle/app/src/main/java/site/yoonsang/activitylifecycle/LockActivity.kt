package site.yoonsang.activitylifecycle

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import site.yoonsang.activitylifecycle.databinding.ActivityLockBinding

class LockActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLockBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("checkkk", "lock onCreate")
        super.onCreate(savedInstanceState)
        binding = ActivityLockBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.numFinger.setOnClickListener {
            Log.d("checkkk", "lock finger")
            finish()
        }
    }

    override fun onStart() {
        Log.d("checkkk", "lock onStart")
        super.onStart()
    }

    override fun onResume() {
        Log.d("checkkk", "lock onResume")
        super.onResume()
    }

    override fun onPause() {
        Log.d("checkkk", "lock onPause")
        super.onPause()
    }

    override fun onStop() {
        Log.d("checkkk", "lock onStop")
        super.onStop()
    }

    override fun onDestroy() {
        Log.d("checkkk", "lock onDestroy")
        super.onDestroy()
    }

    override fun onRestart() {
        Log.d("checkkk", "lock onRestart")
        super.onRestart()
    }

    override fun onBackPressed() {
    }
}