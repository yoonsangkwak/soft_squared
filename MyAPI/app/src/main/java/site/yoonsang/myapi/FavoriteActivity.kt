package site.yoonsang.myapi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import site.yoonsang.myapi.databinding.ActivityFavoriteBinding
import site.yoonsang.myapi.databinding.ActivitySearchBinding

class FavoriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.searchBack.setOnClickListener {
            finish()
        }

        val pm10 = intent.getDoubleExtra("here", 0.toDouble())

        if (pm10 >= 0 && pm10 < 30) {
            binding.searchHereLayout.setBackgroundResource(R.color.veryGood)
            binding.searchHereImage.setImageResource(R.drawable.ic_very_good)
            binding.searchHereStatus.text = "좋음"
        } else if (pm10 >= 39 && pm10 < 50) {
            binding.searchHereLayout.setBackgroundResource(R.color.good)
            binding.searchHereImage.setImageResource(R.drawable.ic_good)
            binding.searchHereStatus.text = "보통"
        } else if (pm10 >= 50 && pm10 < 100) {
            binding.searchHereLayout.setBackgroundResource(R.color.bad)
            binding.searchHereImage.setImageResource(R.drawable.ic_bad)
            binding.searchHereStatus.text = "나쁨"
        } else if (pm10 >= 100) {
            binding.searchHereLayout.setBackgroundResource(R.color.veryBad)
            binding.searchHereImage.setImageResource(R.drawable.ic_very_bad)
            binding.searchHereStatus.text = "상당히 나쁨"
        }
    }
}