package site.yoonsang.myapi

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import site.yoonsang.myapi.databinding.ItemSearchLocationBinding

class LocationAdapter(
    val context: Context,
    private val list: ArrayList<Documents>
) : RecyclerView.Adapter<LocationAdapter.ViewHolder>() {

    private val inflater =
        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    lateinit var binding: ItemSearchLocationBinding

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val addressName: TextView = binding.itemSearchLocationName
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ItemSearchLocationBinding.inflate(inflater, parent, false)
        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.addressName.text = list[position].addressName
        holder.itemView.setOnClickListener {
            val helper = DBHelper(context, DB_NAME, DB_VERSION)
            helper.insertData(LocationInfo(position, list[0].y!!, list[0].x!!))
            (context as Activity).finish()
        }
    }

    override fun getItemCount(): Int = list.size
}