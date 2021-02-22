package site.yoonsang.mylistview

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import site.yoonsang.mylistview.databinding.ItemMyMessageBinding

data class Message(
    val message: String
)

class MyMessageAdapter(
    context: Context,
): RecyclerView.Adapter<MyMessageAdapter.ViewHolder>() {

    private val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    lateinit var binding: ItemMyMessageBinding
    val messageList = mutableListOf<Message>()

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val message: TextView = binding.myMessage
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ItemMyMessageBinding.inflate(inflater, parent, false)
        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.message.text = messageList[position].message
    }

    override fun getItemCount(): Int = messageList.size
}