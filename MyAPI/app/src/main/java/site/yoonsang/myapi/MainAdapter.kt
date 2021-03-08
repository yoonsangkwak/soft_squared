package site.yoonsang.myapi

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import site.yoonsang.myapi.databinding.FragmentMainBinding
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MainAdapter(
    val context: Context,
    private val list: ArrayList<LocationInfo>
) : RecyclerView.Adapter<MainAdapter.ViewHolder>() {

    private val inflater =
        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    lateinit var binding: FragmentMainBinding

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mainLocationHere = binding.mainLocationHere
        val mainLocation = binding.mainLocation
        val mainStatusImage = binding.mainStatusImage
        val mainStatusMessage = binding.mainStatusMessage
        val mainStatusText = binding.mainStatusText
        val mainUploadDate = binding.mainUploadDate
        val mainRootLayout = binding.mainRootLayout
        val mainViewpager2 = binding.mainViewpager2
        val mainRefresh = binding.mainSwipeRefresh
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = FragmentMainBinding.inflate(inflater, parent, false)
        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        getData(holder, position)
        holder.mainRefresh.setOnRefreshListener {
            getData(holder, position)
            notifyDataSetChanged()
            if (holder.mainRefresh.isRefreshing) holder.mainRefresh.isRefreshing = false
        }
    }

    override fun getItemCount(): Int = list.size

    private fun getData(holder: ViewHolder, position: Int) {
        val geoRetrofit = Retrofit.Builder()
            .baseUrl("https://dapi.kakao.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val geoService = geoRetrofit.create(RetrofitService::class.java)
        val restKey = context.getString(R.string.kakao_rest_key)

        geoService.getAddressName(list[position].lon, list[position].lat, restKey)
            .enqueue(object : Callback<AddressResponse> {
                override fun onResponse(
                    call: Call<AddressResponse>,
                    response: Response<AddressResponse>
                ) {
                    if (response.isSuccessful) {
                        val kakaoResponse = response.body()
                        if (kakaoResponse != null) {
                            if (kakaoResponse.documents.size > 0) {
                                val addressName = kakaoResponse.documents[0].addressName
                                if (position != 0) {
                                    holder.mainLocationHere.text = ""
                                }
                                holder.mainLocation.text = addressName
                            }
                        }
                    }
                }

                override fun onFailure(call: Call<AddressResponse>, t: Throwable) {
                }
            })


        val retrofit = Retrofit.Builder()
            .baseUrl("http://api.openweathermap.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val appid = context.getString(R.string.appid)
        val service = retrofit.create(RetrofitService::class.java)
        val dustConcentration = hashMapOf<String, Double>()

        service.getCurrentDustData(list[position].lat, list[position].lon, appid)
            .enqueue(object : Callback<DustResponse> {
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
                            if (dustConcentration["pm10"] != null) {
                                val sdf = SimpleDateFormat(
                                    "yyyy-MM-dd a h:mm",
                                    Locale.KOREA
                                ).format(System.currentTimeMillis())
                                holder.mainUploadDate.text = sdf
                                holder.mainViewpager2.adapter =
                                    DustFragmentAdapter(context, dustConcentration)
                                holder.mainViewpager2.currentItem = (Int.MAX_VALUE / 2) + 1
                                if (dustConcentration["pm10"]!! >= 0 && dustConcentration["pm10"]!! < 30) {
                                    holder.mainStatusText.text = "좋음"
                                    holder.mainStatusImage.setImageResource(R.drawable.ic_very_good)
                                    holder.mainRootLayout.setBackgroundResource(R.color.veryGood)
                                    holder.mainViewpager2.setBackgroundResource(R.drawable.detail_very_good_background)
                                    holder.mainStatusMessage.text =
                                        context.getString(R.string.veryGood)
                                } else if (dustConcentration["pm10"]!! >= 30 && dustConcentration["pm10"]!! < 50) {
                                    holder.mainStatusText.text = "보통"
                                    holder.mainStatusImage.setImageResource(R.drawable.ic_good)
                                    holder.mainRootLayout.setBackgroundResource(R.color.good)
                                    holder.mainViewpager2.setBackgroundResource(R.drawable.detail_good_background)
                                    holder.mainStatusMessage.text = context.getString(R.string.good)
                                } else if (dustConcentration["pm10"]!! >= 50 && dustConcentration["pm10"]!! < 100) {
                                    holder.mainStatusText.text = "나쁨"
                                    holder.mainStatusImage.setImageResource(R.drawable.ic_bad)
                                    holder.mainRootLayout.setBackgroundResource(R.color.bad)
                                    holder.mainViewpager2.setBackgroundResource(R.drawable.detail_bad_background)
                                    holder.mainStatusMessage.text = context.getString(R.string.bad)
                                } else if (dustConcentration["pm10"]!! >= 100) {
                                    holder.mainStatusText.text = "상당히 나쁨"
                                    holder.mainStatusImage.setImageResource(R.drawable.ic_very_bad)
                                    holder.mainRootLayout.setBackgroundResource(R.color.veryBad)
                                    holder.mainViewpager2.setBackgroundResource(R.drawable.detail_very_bad_background)
                                    holder.mainStatusMessage.text =
                                        context.getString(R.string.veryBad)
                                }
                            }

                        }
                    }
                }

                override fun onFailure(call: Call<DustResponse>, t: Throwable) {
                }
            })
    }
}