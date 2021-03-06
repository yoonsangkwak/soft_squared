package site.yoonsang.myapi

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.util.Log
import com.nhn.android.naverlogin.OAuthLogin
import com.nhn.android.naverlogin.OAuthLogin.mOAuthLoginHandler
import com.nhn.android.naverlogin.OAuthLoginHandler
import site.yoonsang.myapi.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var mOAuthLoginModule: OAuthLogin

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginNaver.setOnClickListener {
            mOAuthLoginModule = OAuthLogin.getInstance()
            mOAuthLoginModule.init(
                this,
                getString(R.string.naver_client_id),
                getString(R.string.naver_client_secret),
                getString(R.string.naver_client_name)
            )

            @SuppressLint("HandlerLeak")
            val mOAuthLoginHandler: OAuthLoginHandler = object : OAuthLoginHandler() {
                override fun run(success: Boolean) {
                    if (success) {
                        val accessToken = mOAuthLoginModule.getAccessToken(this@LoginActivity)
                        val refreshToken = mOAuthLoginModule.getRefreshToken(this@LoginActivity)
                        val expiresAt = mOAuthLoginModule.getExpiresAt(this@LoginActivity)
                        val tokenType = mOAuthLoginModule.getTokenType(this@LoginActivity)
                        Log.d("checkkk", "$accessToken")
                        Log.d("checkkk", "$refreshToken")
                        Log.d("checkkk", "$expiresAt")
                        Log.d("checkkk", "$tokenType")
                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        intent.putExtra("accessToken", "Bearer $accessToken")
                        startActivity(intent)
                    } else {
                        val errorCode = mOAuthLoginModule.getLastErrorCode(this@LoginActivity).code
                        val errorDesc = mOAuthLoginModule.getLastErrorDesc(this@LoginActivity)
                        Log.d("checkkk", "$errorCode")
                        Log.d("checkkk", "$errorDesc")
                    }
                }

            }
            mOAuthLoginModule.startOauthLoginActivity(this, mOAuthLoginHandler)
        }
    }
}