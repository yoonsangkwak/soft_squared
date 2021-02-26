package site.yoonsang.mythread

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import site.yoonsang.mythread.databinding.ItemTileBinding
import java.util.*

class TileAdapter(
    context: Context,
) : RecyclerView.Adapter<TileAdapter.ViewHolder>() {

    val _1to50 = Vector<Int>()
    val _1to25 = Vector<Int>()
    val _26to50 = Vector<Int>()
    val visible = Vector<Int>()
    var now = 1
    private val inflater =
        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    lateinit var binding: ItemTileBinding

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tileNumber = binding.tileNumber
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ItemTileBinding.inflate(inflater, parent, false)
        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val number = _1to50[position]
        holder.tileNumber.text = number.toString()
        holder.tileNumber.visibility = visible[position]
    }

    override fun getItemCount(): Int = _1to50.size

    fun init1to25(number: Int) {
        _1to50.add(number)
    }

    fun update26to50(position: Int, number: Int) {
        _1to50.removeAt(position)
        _1to50.add(position, number)
    }

    fun setUpVisible(position: Int) {
        visible.removeAt(position)
        visible.add(position, View.INVISIBLE)
    }
}