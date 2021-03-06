package site.yoonsang.myapi

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import site.yoonsang.myapi.databinding.ActivityMainBinding
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val BASE_URL = "http://api.openweathermap.org/"
    private var getLatitude: Double? = null
    private var getLongitude: Double? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val APPID = getString(R.string.appid)

        val geocoder = Geocoder(this)

        val pocket = hashMapOf<String, Double>()
        val dustAdapter = DustFragmentAdapter(this, pocket)

        binding.mainViewpager2.apply {
            adapter = dustAdapter
            currentItem = (Int.MAX_VALUE / 2) + 1
        }

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
                        binding.mainLocation.text = "${address[0].locality} ${address[0].thoroughfare}"
                        Log.d("checkkk", "${address[0]}")
                    }
                }
                isGPSEnabled -> {
                    val location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                    getLongitude = location?.longitude
                    getLatitude = location?.latitude
                    if (getLongitude != null && getLatitude != null) {
                        val address = geocoder.getFromLocation(getLatitude!!, getLongitude!!, 1)
                        binding.mainLocation.text = "${address[0].locality} ${address[0].thoroughfare}"
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
                        pocket["pm10"] = pm10!!
                        pocket["pm2_5"] = pm2_5!!
                        pocket["no2"] = no2!!
                        pocket["o3"] = o3!!
                        pocket["co"] = co!!
                        pocket["so2"] = so2!!
                        val sdf = SimpleDateFormat(
                            "yyyy-MM-dd HH:mm",
                            Locale.KOREA
                        ).format(System.currentTimeMillis())
                        binding.mainUploadDate.text = sdf
                        if (pocket["pm10"] != null) {
                            if (pocket["pm10"]!! >= 0 && pocket["pm10"]!! < 30) {
                                binding.mainStatusText.text = "좋음"
                                binding.mainStatusImage.setImageResource(R.drawable.ic_very_good)
                                binding.mainRootLayout.setBackgroundResource(R.color.veryGood)
                                binding.mainViewpager2.setBackgroundResource(R.drawable.detail_very_good_background)
                                window.statusBarColor = getColor(R.color.statusVeryGood)
                                binding.mainStatusMessage.text = getString(R.string.veryGood)
                            } else if (pocket["pm10"]!! >= 39 && pocket["pm10"]!! < 50) {
                                binding.mainStatusText.text = "보통"
                                binding.mainStatusImage.setImageResource(R.drawable.ic_good)
                                binding.mainRootLayout.setBackgroundResource(R.color.good)
                                binding.mainViewpager2.setBackgroundResource(R.drawable.detail_good_background)
                                window.statusBarColor = getColor(R.color.statusGood)
                                binding.mainStatusMessage.text = getString(R.string.good)
                            } else if (pocket["pm10"]!! >= 50 && pocket["pm10"]!! < 100) {
                                binding.mainStatusText.text = "나쁨"
                                binding.mainStatusImage.setImageResource(R.drawable.ic_bad)
                                binding.mainRootLayout.setBackgroundResource(R.color.bad)
                                binding.mainViewpager2.setBackgroundResource(R.drawable.detail_bad_background)
                                window.statusBarColor = getColor(R.color.statusBad)
                                binding.mainStatusMessage.text = getString(R.string.bad)
                            } else if (pocket["pm10"]!! >= 100) {
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

        binding.mainAdd.setOnClickListener {
            val intent = Intent(this, FavoriteActivity::class.java)
            intent.putExtra("here", pocket["pm10"]!!)
            startActivity(intent)
        }
    }
}
