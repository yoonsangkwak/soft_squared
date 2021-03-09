package site.yoonsang.myapi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import site.yoonsang.myapi.databinding.ActivityFavoriteBinding
import site.yoonsang.myapi.databinding.ActivitySearchBinding

class FavoriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteBinding
    private val helper = DBHelper(this, DB_NAME, DB_VERSION)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.favoriteBack.setOnClickListener {
            finish()
        }

        binding.favoriteAddFavorite.setOnClickListener {
            if (helper.selectData().size == 7) {
                Toast.makeText(this, "즐겨찾기는 6개까지만 저장 가능합니다.", Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent(this, SearchActivity::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        binding.favoriteLocationRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = FavoriteAdapter(context, helper.selectData())
        }
        binding.favoriteLocationRecyclerView.adapter?.notifyDataSetChanged()
    }
}