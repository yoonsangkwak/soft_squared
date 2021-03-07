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
import site.yoonsang.myapi.databinding.ItemLocationBinding

class FavoriteAdapter(
    val context: Context,
    val list: ArrayList<LocationInfo>
): RecyclerView.Adapter<FavoriteAdapter.ViewHolder>() {

    private val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    lateinit var binding: ItemLocationBinding

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val locationName = binding.locationName
        val locationStatusImage = binding.locationStatusImage
        val locationStatusText = binding.locationStatusText
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ItemLocationBinding.inflate(inflater, parent, false)
        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.locationName.text = getGeo(position).toString()
        val pm10 = getData(position)
        if (pm10 != null) {
            if (pm10 >= 0 && pm10 < 30) {
                holder.locationStatusText.text = "좋음"
                holder.locationStatusImage.setImageResource(R.drawable.ic_very_good)
            } else if (pm10 >= 39 && pm10 < 50) {
                holder.locationStatusText.text = "보통"
                holder.locationStatusImage.setImageResource(R.drawable.ic_good)
            } else if (pm10 >= 50 && pm10 < 100) {
                holder.locationStatusText.text = "나쁨"
                holder.locationStatusImage.setImageResource(R.drawable.ic_bad)
            } else if (pm10 >= 100) {
                holder.locationStatusText.text = "상당히 나쁨"
                holder.locationStatusImage.setImageResource(R.drawable.ic_very_bad)
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

        service.getCurrentDustData(list[position].lat, list[position].lon, appid).enqueue(object :
            Callback<DustResponse> {
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