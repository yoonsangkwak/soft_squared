package site.yoonsang.mylistview

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import site.yoonsang.mylistview.databinding.ItemPeopleListBinding

data class PeopleList(val name: String, val profileImage: Int)

class PeopleListAdapter(
    context: Context,
    private val peopleList: ArrayList<PeopleList>
): RecyclerView.Adapter<PeopleListAdapter.ViewHolder>(), Filterable {

    private val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    lateinit var binding: ItemPeopleListBinding
    var peopleSearchList: ArrayList<PeopleList> = peopleList.clone() as ArrayList<PeopleList>

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val peopleListProfileImage: ImageView = binding.peopleListProfileImage
        val peopleListName: TextView = binding.peopleListName
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ItemPeopleListBinding.inflate(inflater, parent, false)
        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.peopleListProfileImage.setImageResource(peopleList[position].profileImage)
        holder.peopleListName.text = peopleList[position].name

        holder.itemView.setOnClickListener {
            val intent = Intent(it.context, MessengerActivity::class.java)
            val chatHelper = DBChatHelper(holder.itemView.context, DB_CHAT, DB_VERSION)
            intent.putExtra("name", peopleList[position].name)
            intent.putExtra("image", peopleList[position].profileImage)
            chatHelper.insertChatList(Chat(peopleList[position].name, "이제 Messenger에서 친구와 메시지를 주고받을 수 있습니다.", System.currentTimeMillis(), peopleList[position].profileImage))
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = peopleList.size

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val charString = charSequence.toString()
                peopleSearchList = if (charString.isEmpty()) {
                    peopleList
                } else {
                    val filteredList = ArrayList<PeopleList>()
                    for (item in peopleList) {
                        if (item.name.contains(charString)) {
                            filteredList.add(item)
                        }
                    }
                    filteredList
                }
                val filterResults = FilterResults()
                filterResults.values = peopleSearchList
                return filterResults
            }

            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
                peopleSearchList = filterResults.values as ArrayList<PeopleList>
                notifyDataSetChanged()
            }
        }
    }
}