package site.yoonsang.mylistview

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import site.yoonsang.mylistview.databinding.ItemVideoCallPersonBinding

data class VideoCall(
    val name: String,
    val profileImage: Int
)

class VideoCallAdapter(
    context: Context,
    private val videoCallList: ArrayList<VideoCall>
): RecyclerView.Adapter<VideoCallAdapter.ViewHolder>() {

    private val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    lateinit var binding: ItemVideoCallPersonBinding

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val videoCallProfileImage: ImageView = binding.videoCallProfileImage
        val videoCallUserName: TextView = binding.videoCallUserName
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ItemVideoCallPersonBinding.inflate(inflater, parent, false)
        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.videoCallProfileImage.setImageResource(videoCallList[position].profileImage)
        holder.videoCallUserName.text = videoCallList[position].name
    }

    override fun getItemCount(): Int = videoCallList.size
}