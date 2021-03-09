package site.yoonsang.myapi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import site.yoonsang.myapi.databinding.ActivitySearchBinding

class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding
    private val KAKAO_BASE_URL = "https://dapi.kakao.com/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.searchClose.setOnClickListener {
            binding.searchEditText.setText("")
        }

        binding.searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                getLocation(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
    }

    private fun getLocation(address: String) {
        val retrofit = Retrofit.Builder()
            .baseUrl(KAKAO_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(RetrofitService::class.java)
        val restAPIKey = getString(R.string.kakao_rest_key)

        service.getAddressData(address, restAPIKey).enqueue(object : Callback<AddressResponse> {
            override fun onResponse(
                call: Call<AddressResponse>,
                response: Response<AddressResponse>
            ) {
                if (response.isSuccessful) {
                    val kakaoResponse = response.body()
                    if (kakaoResponse != null) {
                        if (kakaoResponse.documents.size > 0) {
                            val locationAdapter =
                                LocationAdapter(this@SearchActivity, kakaoResponse.documents)
                            binding.searchLocationRecyclerView.apply {
                                layoutManager = LinearLayoutManager(context)
                                adapter = locationAdapter
                            }
                            binding.searchLocationRecyclerView.adapter?.notifyDataSetChanged()
                        }
                    }
                }
            }

            override fun onFailure(call: Call<AddressResponse>, t: Throwable) {
                Log.d("checkkk", "fail kakao ${t.message}")
            }
        })
    }
}