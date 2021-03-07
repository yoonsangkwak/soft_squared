package site.yoonsang.myapi

import android.app.Activity
import android.content.Context
import android.location.Geocoder
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
import site.yoonsang.myapi.databinding.ItemMainLayoutBinding
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MainAdapter(
    val context: Context,
    private val list: ArrayList<LocationInfo>
): RecyclerView.Adapter<MainAdapter.ViewHolder>() {

    private val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    lateinit var binding: FragmentMainBinding

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val mainLocation = binding.mainLocation
        val mainStatusImage = binding.mainStatusImage
        val mainStatusMessage = binding.mainStatusMessage
        val mainStatusText = binding.mainStatusText
        val mainUploadDate = binding.mainUploadDate
        val mainRootLayout = binding.mainRootLayout
        val mainViewpager2 = binding.mainViewpager2
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = FragmentMainBinding.inflate(inflater, parent, false)
        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val pm10 = getData(position)
        if (pm10 != null) {
            val sdf = SimpleDateFormat(
                "yyyy-MM-dd a h:mm",
                Locale.KOREA
            ).format(System.currentTimeMillis())
            holder.mainUploadDate.text = sdf
            holder.mainLocation.text = getGeo(position).toString()
            if (pm10 >= 0 && pm10 < 30) {
                holder.mainStatusText.text = "좋음"
                holder.mainStatusImage.setImageResource(R.drawable.ic_very_good)
                holder.mainRootLayout.setBackgroundResource(R.color.veryGood)
                holder.mainViewpager2.setBackgroundResource(R.drawable.detail_very_good_background)
                (context as Activity).window.statusBarColor = context.getColor(R.color.statusVeryGood)
                holder.mainStatusMessage.text = context.getString(R.string.veryGood)
            } else if (pm10 >= 39 && pm10 < 50) {
                holder.mainStatusText.text = "보통"
                holder.mainStatusImage.setImageResource(R.drawable.ic_good)
                holder.mainRootLayout.setBackgroundResource(R.color.good)
                holder.mainViewpager2.setBackgroundResource(R.drawable.detail_good_background)
                (context as Activity).window.statusBarColor = context.getColor(R.color.statusGood)
                holder.mainStatusMessage.text = context.getString(R.string.good)
            } else if (pm10 >= 50 && pm10 < 100) {
                holder.mainStatusText.text = "나쁨"
                holder.mainStatusImage.setImageResource(R.drawable.ic_bad)
                holder.mainRootLayout.setBackgroundResource(R.color.bad)
                holder.mainViewpager2.setBackgroundResource(R.drawable.detail_bad_background)
                (context as Activity).window.statusBarColor = context.getColor(R.color.statusBad)
                holder.mainStatusMessage.text = context.getString(R.string.bad)
            } else if (pm10 >= 100) {
                holder.mainStatusText.text = "상당히 나쁨"
                holder.mainStatusImage.setImageResource(R.drawable.ic_very_bad)
                holder.mainRootLayout.setBackgroundResource(R.color.veryBad)
                holder.mainViewpager2.setBackgroundResource(R.drawable.detail_very_bad_background)
                (context as Activity).window.statusBarColor = context.getColor(R.color.statusVeryBad)
                holder.mainStatusMessage.text = context.getString(R.string.veryBad)
            }
        }
    }

    override fun getItemCount(): Int = list.size

    fun getData(position: Int): Double? {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://api.openweathermap.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val appid = context.getString(R.string.appid)
        val service = retrofit.create(RetrofitService::class.java)
        var pm10: Double? = null

        service.getCurrentDustData(list[position].lat, list[position].lon, appid).enqueue(object : Callback<DustResponse> {
            override fun onResponse(call: Call<DustResponse>, response: Response<DustResponse>) {
                if (response.isSuccessful) {
                    val dustResponse = response.body()
                    if (dustResponse != null) {
                        pm10 = dustResponse.dataList[0].components?.pm10
                    }
                }
            }

            override fun onFailure(call: Call<DustResponse>, t: Throwable) {
            }
        })
        return pm10
    }

    fun getGeo(position: Int): String? {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://dapi.kakao.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(RetrofitService::class.java)
        val restKey = context.getString(R.string.kakao_rest_key)
        var addressName: String? = null

        service.getAddressName(list[position].lat, list[position].lon, restKey).enqueue(object : Callback<AddressResponse> {
            override fun onResponse(
                call: Call<AddressResponse>,
                response: Response<AddressResponse>
            ) {
                if (response.isSuccessful) {
                    val kakaoResponse = response.body()
                    if (kakaoResponse != null) {
                        if (kakaoResponse.documents.size > 0) {
                            addressName = kakaoResponse.documents[0].addressName.toString()
                        }
                    }
                }
            }

            override fun onFailure(call: Call<AddressResponse>, t: Throwable) {
            }
        })

        return addressName
    }
}