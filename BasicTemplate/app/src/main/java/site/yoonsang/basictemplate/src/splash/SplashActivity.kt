package site.yoonsang.basictemplate.src.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import site.yoonsang.basictemplate.config.BaseActivity
import site.yoonsang.basictemplate.databinding.ActivitySplashBinding
import site.yoonsang.basictemplate.src.main.MainActivity

class SplashActivity: BaseActivity<ActivitySplashBinding>(ActivitySplashBinding::inflate) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }, 1500)
    }
}