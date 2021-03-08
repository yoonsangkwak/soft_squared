package site.yoonsang.myapi

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.UserManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.navigation.NavigationView
import com.kakao.sdk.user.UserApiClient
import com.nhn.android.naverlogin.OAuthLogin
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import site.yoonsang.myapi.databinding.ActivityMainBinding
import site.yoonsang.myapi.databinding.NaviHeaderBinding
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val NAVER_BASE_URL = "https://openapi.naver.com/"
    private val helper = DBHelper(this, DB_NAME, DB_VERSION)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getLatLng()

        setSupportActionBar(binding.mainToolBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_menu)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        val mainAdapter = MainAdapter(this, helper.selectData(), binding.viewpager)
        binding.viewpager.adapter = mainAdapter

        val headerView = binding.navView.getHeaderView(0)
        val headerBinding = NaviHeaderBinding.bind(headerView)

        binding.navView.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_logout -> {
                    if (intent.getStringExtra("sort") == "naver") {
                        val mOAuthLoginModule = OAuthLogin.getInstance()
                        mOAuthLoginModule.logout(this)
                        val intent = Intent(this, LoginActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else if (intent.getStringExtra("sort") == "kakao") {
                        UserApiClient.instance.logout { error ->
                            if (error != null) {
                                Log.d("checkkk", "로그아웃 실패. SDK에서 토큰 삭제됨", error)
                            } else {
                                Log.d("checkkk", "로그아웃 성공. SDK에서 토큰 삭제됨")
                                val intent = Intent(this, LoginActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
                        }
                    }
                }
            }
            false
        }

        if (intent.getStringExtra("sort") == "naver") {
            val naverRetrofit = Retrofit.Builder()
                .baseUrl(NAVER_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val naverService = naverRetrofit.create(RetrofitService::class.java)

            naverService.getNaverUserInfo(intent.getStringExtra("accessToken")!!)
                .enqueue(object : Callback<NaverUserResponse> {
                    override fun onResponse(
                        call: Call<NaverUserResponse>,
                        response: Response<NaverUserResponse>
                    ) {
                        if (response.isSuccessful) {
                            val naverResponse = response.body()
                            if (naverResponse != null) {
                                val email = naverResponse.response?.email
                                val name = naverResponse.response?.name
                                headerBinding.navName.text = name
                                headerBinding.navEmail.text = email
                                headerBinding.navSort.text = "네이버"
                            }
                        }
                    }

                    override fun onFailure(call: Call<NaverUserResponse>, t: Throwable) {
                        Log.d("checkkk", "fail naver ${t.message}")
                    }
                })
        }

        if (intent.getStringExtra("sort") == "kakao") {
            UserApiClient.instance.me { user, error ->
                headerBinding.navName.text = user?.kakaoAccount?.profile?.nickname
                headerBinding.navEmail.text = user?.kakaoAccount?.email
                headerBinding.navSort.text = "카카오"
            }
        }
    }

    override fun onResume() {
        super.onResume()
        binding.viewpager.adapter?.notifyDataSetChanged()
    }

    override fun onBackPressed() {
        if (binding.mainDrawer.isDrawerOpen(GravityCompat.START)) {
            binding.mainDrawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                binding.mainDrawer.openDrawer(GravityCompat.START)
            }
            R.id.menu_add -> {
                val intent = Intent(this, FavoriteActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    private fun getLatLng() {
        val lm = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val isGPSEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
        val isNetworkEnabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        var getLatitude: Double? = null
        var getLongitude: Double? = null

        if (ActivityCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                0
            )
            getLatLng()
        } else {
            when {
                isNetworkEnabled -> {
                    val location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                    getLongitude = location?.longitude
                    getLatitude = location?.latitude
                }
                isGPSEnabled -> {
                    val location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                    getLongitude = location?.longitude
                    getLatitude = location?.latitude
                }
            }
            if (helper.selectData().size == 0) {
                helper.insertData(LocationInfo(getLatitude.toString(), getLongitude.toString()))
            }
        }
    }
}