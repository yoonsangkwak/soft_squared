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

    private val _1to50 = Vector<Int>()
    private val _1to25 = Vector<Int>()
    private val _26to50 = Vector<Int>()
    private val visible = Vector<Int>()
    private var now = 1
    private val inflater =
        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    lateinit var binding: ItemTileBinding

    init {
        for (i in 1..25) {
            _1to25.add(i)
            _26to50.add(i + 25)
        }

        for (i in 0..24) {
            visible.add(i, View.VISIBLE)
        }

        while (_1to25.size > 0) {
            val rand: Int = Random().nextInt(_1to25.size)
            if (_1to25[rand] !in _1to50) {
                init1to25(_1to25[rand])
                _1to25.removeElement(_1to25[rand])
                notifyDataSetChanged()
            }
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
        holder.itemView.setOnClickListener {
            val selected = Integer.parseInt(holder.tileNumber.text.toString())
            if (selected == now) {
                if (selected >= 26 && selected == now) setUpVisible(position)
                now++
//                val rand: Int = (Math.random() * _26to50.size).toInt()
                val rand: Int = Random().nextInt(_26to50.size)
                update26to50(position, _26to50[rand])
                _26to50.remove(rand)
                notifyItemChanged(position)
            }
        }
    }

    override fun getItemCount(): Int = _1to50.size

    private fun init1to25(number: Int) {
        _1to50.add(number)
    }

    private fun update26to50(position: Int, number: Int) {
        _1to50.removeAt(position)
        _1to50.add(position, number)
    }

    private fun setUpVisible(position: Int) {
        visible.removeAt(position)
        visible.add(position, View.INVISIBLE)
    }
}