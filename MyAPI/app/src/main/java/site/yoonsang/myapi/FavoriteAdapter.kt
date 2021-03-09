package site.yoonsang.myapi

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chauthai.swipereveallayout.ViewBinderHelper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import site.yoonsang.myapi.databinding.ItemLocationBinding

class FavoriteAdapter(
    val context: Context,
    private val list: ArrayList<LocationInfo>
) : RecyclerView.Adapter<FavoriteAdapter.ViewHolder>() {

    private val inflater =
        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    private lateinit var binding: ItemLocationBinding
    private val binderHelper = ViewBinderHelper()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val locationName = binding.locationName
        val locationStatusImage = binding.locationStatusImage
        val locationStatusText = binding.locationStatusText
        val locationContainer = binding.locationContainer
        val favoriteDelete = binding.favoriteDelete
        val swipeReveal = binding.swipeRevealLayout
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ItemLocationBinding.inflate(inflater, parent, false)
        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
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
                                if (position == 0) {
                                    holder.locationName.text = "GPS 현재 위치"
                                } else {
                                    holder.locationName.text = addressName
                                }
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

        service.getCurrentDustData(list[position].lat, list[position].lon, appid).enqueue(object :
            Callback<DustResponse> {
            override fun onResponse(call: Call<DustResponse>, response: Response<DustResponse>) {
                if (response.isSuccessful) {
                    val dustResponse = response.body()
                    if (dustResponse != null) {
                        val pm10 = dustResponse.dataList[0].components?.pm10
                        if (pm10 != null) {
                            if (pm10 >= 0 && pm10 < 30) {
                                holder.locationStatusText.text = "좋음"
                                holder.locationStatusImage.setImageResource(R.drawable.ic_very_good)
                                holder.locationContainer.setBackgroundResource(R.drawable.favorite_very_good)
                            } else if (pm10 >= 30 && pm10 < 50) {
                                holder.locationStatusText.text = "보통"
                                holder.locationStatusImage.setImageResource(R.drawable.ic_good)
                                holder.locationContainer.setBackgroundResource(R.drawable.favorite_good)
                            } else if (pm10 >= 50 && pm10 < 100) {
                                holder.locationStatusText.text = "나쁨"
                                holder.locationStatusImage.setImageResource(R.drawable.ic_bad)
                                holder.locationContainer.setBackgroundResource(R.drawable.favorite_bad)
                            } else if (pm10 >= 100) {
                                holder.locationStatusText.text = "매우 나쁨"
                                holder.locationStatusImage.setImageResource(R.drawable.ic_very_bad)
                                holder.locationContainer.setBackgroundResource(R.drawable.favorite_very_bad)
                            }
                        }
                    }
                }
            }

            override fun onFailure(call: Call<DustResponse>, t: Throwable) {
            }
        })
        val helper = DBHelper(context, DB_NAME, DB_VERSION)
        binderHelper.bind(holder.swipeReveal, "${holder.adapterPosition}")
        binderHelper.setOpenOnlyOne(true)
        binderHelper.lockSwipe("0")

        holder.favoriteDelete.setOnClickListener {
            if (position != 0) {
                if (holder.swipeReveal.isOpened) {
                    list.removeAt(position)
                    notifyItemRemoved(position)
                    val item = helper.searchData(position)
                    helper.deleteData(item)
                }
            }
        }
    }

    override fun getItemCount(): Int = list.size
}