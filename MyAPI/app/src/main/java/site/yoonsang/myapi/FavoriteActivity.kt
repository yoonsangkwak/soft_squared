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
    }

    override fun onResume() {
        super.onResume()
        binding.favoriteLocationRecyclerView.adapter?.notifyDataSetChanged()
    }
}