package site.yoonsang.mythread

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import site.yoonsang.mythread.databinding.ItemTileBinding
import java.util.*

class TileAdapter(
    context: Context,
) : RecyclerView.Adapter<TileAdapter.ViewHolder>() {

    private val _1to50 = Vector<Int>()
    private val visible = Vector<Int>()
    var now = 1
    private var width: Int = 0
    private var height: Int = 0
    private val inflater =
        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    lateinit var binding: ItemTileBinding

    init {
        for (i in 0..24) {
            visible.add(i, View.VISIBLE)
        }
    }

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
        if (width != 0 && height != 0) {
            binding.tileNumber.layoutParams.width = width
            binding.tileNumber.layoutParams.height = height
        }
        holder.itemView.setOnClickListener {
            val selected = Integer.parseInt(_1to50[position].toString())
            if (selected == now) {
                if (selected >= 26 && selected == now) setUpVisible(position)
                now++
                val rand: Int = (Math.random() * _1to50.size).toInt()
                update26to50(position)
            }
        }
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

    fun setLength(width: Int, height: Int) {
        this.width = width
        this.height = height
    }
}