package site.yoonsang.myapi

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.UserManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
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
    private val BASE_URL = "http://api.openweathermap.org/"
    private val NAVER_BASE_URL = "https://openapi.naver.com/"
    private var getLatitude: Double? = null
    private var getLongitude: Double? = null
    private lateinit var APPID: String
    private lateinit var dustConcentration: HashMap<String, Double>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        APPID = getString(R.string.appid)

        setSupportActionBar(binding.mainToolBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_menu)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        val headerView = binding.navView.getHeaderView(0)
        val headerBinding = NaviHeaderBinding.bind(headerView)

        dustConcentration = hashMapOf()
        val dustAdapter = DustFragmentAdapter(this, dustConcentration)

        binding.mainViewpager2.apply {
            adapter = dustAdapter
            currentItem = (Int.MAX_VALUE / 2) + 1
        }

        binding.mainSwipeRefresh.setOnRefreshListener {
            refreshData(dustAdapter)
            binding.mainSwipeRefresh.isRefreshing = false
        }

        refreshData(dustAdapter)

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
                intent.putExtra("here", dustConcentration["pm10"])
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    private fun refreshData(dustAdapter: DustFragmentAdapter) {
        val geocoder = Geocoder(this)
        val lm = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val isGPSEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
        val isNetworkEnabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

        if (ContextCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                0
            )
        } else {
            when {
                isNetworkEnabled -> {
                    val location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                    getLongitude = location?.longitude
                    getLatitude = location?.latitude
                    if (getLongitude != null && getLatitude != null) {
                        val address = geocoder.getFromLocation(getLatitude!!, getLongitude!!, 1)
                        binding.mainLocation.text =
                            "${address[0].locality} ${address[0].thoroughfare}"
                    }
                }
                isGPSEnabled -> {
                    val location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                    getLongitude = location?.longitude
                    getLatitude = location?.latitude
                    if (getLongitude != null && getLatitude != null) {
                        val address = geocoder.getFromLocation(getLatitude!!, getLongitude!!, 1)
                        binding.mainLocation.text =
                            "${address[0].locality} ${address[0].thoroughfare}"
                    }
                }
                else -> {
                }
            }
        }

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(RetrofitService::class.java)

        service.getCurrentDustData(
            getLatitude!!, getLongitude!!,
            APPID
        ).enqueue(object : Callback<DustResponse> {
            override fun onResponse(
                call: Call<DustResponse>,
                response: Response<DustResponse>
            ) {
                if (response.isSuccessful) {
                    val dustResponse = response.body()
                    if (dustResponse != null) {
                        val pm10 = dustResponse.dataList[0].components?.pm10
                        val pm2_5 = dustResponse.dataList[0].components?.pm2_5
                        val no2 = dustResponse.dataList[0].components?.no2
                        val o3 = dustResponse.dataList[0].components?.o3
                        val co = dustResponse.dataList[0].components?.co
                        val so2 = dustResponse.dataList[0].components?.so2
                        dustConcentration["pm10"] = pm10!!
                        dustConcentration["pm2_5"] = pm2_5!!
                        dustConcentration["no2"] = no2!!
                        dustConcentration["o3"] = o3!!
                        dustConcentration["co"] = co!!
                        dustConcentration["so2"] = so2!!
                        val sdf = SimpleDateFormat(
                            "yyyy-MM-dd a h:mm",
                            Locale.KOREA
                        ).format(System.currentTimeMillis())
                        binding.mainUploadDate.text = sdf
                        if (dustConcentration["pm10"] != null) {
                            if (dustConcentration["pm10"]!! >= 0 && dustConcentration["pm10"]!! < 30) {
                                binding.mainStatusText.text = "좋음"
                                binding.mainStatusImage.setImageResource(R.drawable.ic_very_good)
                                binding.mainRootLayout.setBackgroundResource(R.color.veryGood)
                                binding.mainViewpager2.setBackgroundResource(R.drawable.detail_very_good_background)
                                window.statusBarColor = getColor(R.color.statusVeryGood)
                                binding.mainStatusMessage.text = getString(R.string.veryGood)
                            } else if (dustConcentration["pm10"]!! >= 39 && dustConcentration["pm10"]!! < 50) {
                                binding.mainStatusText.text = "보통"
                                binding.mainStatusImage.setImageResource(R.drawable.ic_good)
                                binding.mainRootLayout.setBackgroundResource(R.color.good)
                                binding.mainViewpager2.setBackgroundResource(R.drawable.detail_good_background)
                                window.statusBarColor = getColor(R.color.statusGood)
                                binding.mainStatusMessage.text = getString(R.string.good)
                            } else if (dustConcentration["pm10"]!! >= 50 && dustConcentration["pm10"]!! < 100) {
                                binding.mainStatusText.text = "나쁨"
                                binding.mainStatusImage.setImageResource(R.drawable.ic_bad)
                                binding.mainRootLayout.setBackgroundResource(R.color.bad)
                                binding.mainViewpager2.setBackgroundResource(R.drawable.detail_bad_background)
                                window.statusBarColor = getColor(R.color.statusBad)
                                binding.mainStatusMessage.text = getString(R.string.bad)
                            } else if (dustConcentration["pm10"]!! >= 100) {
                                binding.mainStatusText.text = "상당히 나쁨"
                                binding.mainStatusImage.setImageResource(R.drawable.ic_very_bad)
                                binding.mainRootLayout.setBackgroundResource(R.color.veryBad)
                                binding.mainViewpager2.setBackgroundResource(R.drawable.detail_very_bad_background)
                                window.statusBarColor = getColor(R.color.statusVeryBad)
                                binding.mainStatusMessage.text = getString(R.string.veryBad)
                            }
                        }
                        dustAdapter.notifyDataSetChanged()
                    }
                }
            }

            override fun onFailure(call: Call<DustResponse>, t: Throwable) {
                Log.d("checkkk", "fail ${t.message}")
            }
        })
    }
}