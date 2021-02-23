package site.yoonsang.mylistview

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import site.yoonsang.mylistview.databinding.ItemChatBinding
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

data class Chat(
    val name: String,
    val message: String = "",
    val uploadDate: Long = System.currentTimeMillis(),
    val profileImage: Int
)

class ChatAdapter(
    context: Context,
    private val chatList: ArrayList<Chat>
) : RecyclerView.Adapter<ChatAdapter.ViewHolder>(), Filterable {

    private val inflater =
        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    lateinit var binding: ItemChatBinding
    var chatSearchList: ArrayList<Chat> = chatList.clone() as ArrayList<Chat>

    init {
        Log.d("checkkk", "g " + chatSearchList)
        Log.d("checkkk", "g " + chatList)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val chatUserProfileImage: ImageView = binding.chatUserProfileImage
        val chatUserName: TextView = binding.chatUserName
        val chatLastMessage: TextView = binding.chatLastMessage
        val chatUploadDate: TextView = binding.chatUploadDate
        val chatDelete = binding.chatDelete
        val chatMainLayout = binding.chatMainLayout
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ItemChatBinding.inflate(inflater, parent, false)
        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val chatHelper = DBChatHelper(holder.itemView.context, DB_CHAT, DB_VERSION)
        val chat = chatHelper.searchChat(chatSearchList[position].name)

        if (chat != null) {
            holder.chatUserProfileImage.setImageResource(chat.profileImage)
            holder.chatUserName.text = chat.name
            holder.chatLastMessage.text = chat.message
            val time = chat.uploadDate
            val sdf = SimpleDateFormat(" · M월 d일", Locale.KOREA).format(time)
            holder.chatUploadDate.text = sdf

            holder.chatMainLayout.setOnClickListener {
                val intent = Intent(it.context, MessengerActivity::class.java)
                intent.putExtra("image", chat.profileImage)
                intent.putExtra("name", chat.name)
                holder.itemView.context.startActivity(intent)
            }

            holder.chatDelete.setOnClickListener {
                chatSearchList.removeAt(holder.adapterPosition)
                notifyItemRemoved(position)
                chatHelper.deleteChatList(chat)
            }
        }
    }

    override fun getItemCount(): Int = chatSearchList.size

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val charString = charSequence.toString()
                chatSearchList = if (charString.isEmpty()) {
                    chatList
                } else {
                    val filteredList = ArrayList<Chat>()
                    for (item in chatList) {
                        if (item.name.contains(charString)) {
                            filteredList.add(item)
                        }
                    }
                    filteredList
                }
                val filterResults = FilterResults()
                filterResults.values = chatSearchList
                return filterResults
            }

            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
                chatSearchList = filterResults.values as ArrayList<Chat>
                notifyDataSetChanged()
            }
        }
    }
}
