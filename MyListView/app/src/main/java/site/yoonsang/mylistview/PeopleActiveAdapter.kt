package site.yoonsang.mylistview

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import site.yoonsang.mylistview.databinding.ItemPeopleActiveBinding

data class PeopleActive(val name: String, val profileImage: Int)

class PeopleActiveAdapter(
    context: Context,
    private val peopleActiveList: ArrayList<PeopleActive>
) : RecyclerView.Adapter<PeopleActiveAdapter.ViewHolder>() {

    private val inflater =
        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    lateinit var binding: ItemPeopleActiveBinding

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val peopleActiveProfileImage: ImageView = binding.peopleActiveProfileImage
        val peopleActiveName: TextView = binding.peopleActiveName
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ItemPeopleActiveBinding.inflate(inflater, parent, false)
        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.peopleActiveProfileImage.setImageResource(peopleActiveList[position].profileImage)
        holder.peopleActiveName.text = peopleActiveList[position].name
    }

    override fun getItemCount(): Int = peopleActiveList.size
}