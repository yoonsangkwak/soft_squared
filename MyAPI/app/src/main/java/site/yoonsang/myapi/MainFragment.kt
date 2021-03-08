package site.yoonsang.myapi

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import site.yoonsang.myapi.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private val BASE_URL = "http://api.openweathermap.org/"
    private val NAVER_BASE_URL = "https://openapi.naver.com/"
    private var getLatitude: Double? = null
    private var getLongitude: Double? = null
    private lateinit var APPID: String
    private lateinit var dustConcentration: HashMap<String, Double>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(layoutInflater, container, false)

        dustConcentration = hashMapOf()
        val dustAdapter = DustFragmentAdapter(context!!, dustConcentration)
        refreshData(dustAdapter)
        Log.d("checkkk", "${dustConcentration.size}")

        binding.mainViewpager2.apply {
            adapter = dustAdapter
            currentItem = (Int.MAX_VALUE / 2) + 1
        }

        binding.mainSwipeRefresh.setOnRefreshListener {
            refreshData(dustAdapter)
            binding.mainSwipeRefresh.isRefreshing = false
        }


        return binding.root
    }

    override fun onResume() {
        super.onResume()
        binding.mainViewpager2.adapter?.notifyDataSetChanged()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun refreshData(dustAdapter: DustFragmentAdapter) {
        val lm = context?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val isGPSEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
        val isNetworkEnabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

        if (ContextCompat.checkSelfPermission(
                context!!.applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
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
            }
        }

        val helper = DBHelper(context!!, DB_NAME, DB_VERSION)
        if (helper.selectData().size == 0) {
            helper.insertData(LocationInfo(getLatitude.toString(), getLongitude.toString()))
        }

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(RetrofitService::class.java)

        service.getCurrentDustData(
            getLatitude.toString(), getLongitude.toString(),
            APPID
        ).enqueue(object : Callback<DustResponse> {
            override fun onResponse(call: Call<DustResponse>, response: Response<DustResponse>) {
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