package site.yoonsang.myapi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import site.yoonsang.myapi.databinding.ActivityFavoriteBinding
import site.yoonsang.myapi.databinding.ActivitySearchBinding

class FavoriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val helper = DBHelper(this, DB_NAME, DB_VERSION)

        binding.favoriteLocationRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = FavoriteAdapter(context, helper.selectData())
        }

        binding.favoriteBack.setOnClickListener {
            finish()
        }

        binding.favoriteAddFavorite.setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
        }

        val pm10 = intent.getDoubleExtra("here", 0.toDouble())

        if (pm10 >= 0 && pm10 < 30) {
            binding.favoriteHereLayout.setBackgroundResource(R.color.veryGood)
            binding.favoriteHereImage.setImageResource(R.drawable.ic_very_good)
            binding.favoriteHereStatus.text = "좋음"
        } else if (pm10 >= 39 && pm10 < 50) {
            binding.favoriteHereLayout.setBackgroundResource(R.color.good)
            binding.favoriteHereImage.setImageResource(R.drawable.ic_good)
            binding.favoriteHereStatus.text = "보통"
        } else if (pm10 >= 50 && pm10 < 100) {
            binding.favoriteHereLayout.setBackgroundResource(R.color.bad)
            binding.favoriteHereImage.setImageResource(R.drawable.ic_bad)
            binding.favoriteHereStatus.text = "나쁨"
        } else if (pm10 >= 100) {
            binding.favoriteHereLayout.setBackgroundResource(R.color.veryBad)
            binding.favoriteHereImage.setImageResource(R.drawable.ic_very_bad)
            binding.favoriteHereStatus.text = "상당히 나쁨"
        }
    }
}