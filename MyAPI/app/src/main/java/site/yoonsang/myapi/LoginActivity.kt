package site.yoonsang.myapi

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.kakao.sdk.auth.model.OAuthToken
import com.nhn.android.naverlogin.OAuthLogin
import com.nhn.android.naverlogin.OAuthLoginHandler
import site.yoonsang.myapi.databinding.ActivityLoginBinding
import com.kakao.sdk.common.model.AuthErrorCause.*
import com.kakao.sdk.user.UserApiClient

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var mOAuthLoginModule: OAuthLogin

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent = Intent(this, MainActivity::class.java)

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
                        Log.d("checkkk", accessToken)
                        Log.d("checkkk", refreshToken)
                        Log.d("checkkk", "$expiresAt")
                        Log.d("checkkk", tokenType)
                        intent.putExtra("sort", "naver")
                        intent.putExtra("accessToken", "Bearer $accessToken")
                        startActivity(intent)
                    } else {
                        val errorCode = mOAuthLoginModule.getLastErrorCode(this@LoginActivity).code
                        val errorDesc = mOAuthLoginModule.getLastErrorDesc(this@LoginActivity)
                        Log.d("checkkk", errorCode)
                        Log.d("checkkk", errorDesc)
                    }
                }

            }
            mOAuthLoginModule.startOauthLoginActivity(this, mOAuthLoginHandler)
        }

        binding.loginKakao.setOnClickListener {
            val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
                if (error != null) {
                    Log.d("checkkk", "${error.message}")
                } else if (token != null) {
                    intent.putExtra("sort", "kakao")
                    startActivity(intent)
                }
            }

            UserApiClient.instance.accessTokenInfo { tokenInfo, error ->
                if (error != null) {
                    Log.d("checkkk", "${error.message}")
                } else if (tokenInfo != null) {
                    intent.putExtra("sort", "kakao")
                    startActivity(intent)
                } else {
                    if (UserApiClient.instance.isKakaoTalkLoginAvailable(this)) {
                        UserApiClient.instance.loginWithKakaoTalk(this, callback = callback)
                    } else {
                        UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
                    }
                }
            }
        }

    }
}