package site.yoonsang.myapi

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
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
                }
                isGPSEnabled -> {
                    val location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                    getLongitude = location?.longitude
                    getLatitude = location?.latitude
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
