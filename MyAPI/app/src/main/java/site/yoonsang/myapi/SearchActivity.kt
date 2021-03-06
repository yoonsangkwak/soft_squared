package site.yoonsang.myapi

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import site.yoonsang.myapi.databinding.ActivitySearchBinding
import java.io.IOException

class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.searchClose.setOnClickListener {
            val searchLocation = binding.searchEditText.text.toString()
            getLocation(this, searchLocation)
        }
    }

    private fun getLocation(context: Context, str: String): Location {
        val location = Location("")
        val geocoder = Geocoder(context)
        var addresses: List<Address>? = null

        try {
            addresses = geocoder.getFromLocationName(str, 10)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        if (addresses != null) {
            val address: Address = addresses[0]
            location.latitude = address.latitude
            location.longitude = address.longitude
            Log.d("checkkk", "${addresses.size}")
            Log.d("checkkk", "${address.locality}")
            Log.d("checkkk", "${address.thoroughfare}")
            Log.d("checkkk", "${location.latitude}")
            Log.d("checkkk", "${location.longitude}")
        }

        return location
    }
}