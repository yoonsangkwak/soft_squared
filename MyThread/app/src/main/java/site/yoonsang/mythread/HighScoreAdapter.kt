package site.yoonsang.mythread

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import site.yoonsang.mythread.databinding.ItemHighScoreBinding

class HighScoreAdapter(
    context: Context,
    private val recordList: ArrayList<Record>
): RecyclerView.Adapter<HighScoreAdapter.ViewHolder>() {

    private val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    lateinit var binding: ItemHighScoreBinding

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val recordRank: TextView = binding.highScoreItemRank
        val recordName: TextView = binding.highScoreItemName
        val recordScore: TextView = binding.highScoreItemScore
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ItemHighScoreBinding.inflate(inflater, parent, false)
        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.recordRank.text = (position + 1).toString()
        holder.recordName.text = recordList[position].name
        holder.recordScore.text = recordList[position].score
    }

    override fun getItemCount(): Int = recordList.size
}