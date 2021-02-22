package site.yoonsang.myactivitylifecycle

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import site.yoonsang.myactivitylifecycle.databinding.FragmentMainSubscribeBinding

class MainSubscribeFragment : Fragment() {

    private var _binding: FragmentMainSubscribeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainSubscribeBinding.inflate(layoutInflater, container, false)

        val icons = createChannelIcons()
        val videos = createVideos()

        binding.subscribeVideoRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = VideoAdapter(videos, layoutInflater)
        }

        binding.subscribeChannelIconRecyclerView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = ChannelIconAdapter(icons, layoutInflater)
        }

        binding.subscribeChipAll.isChecked = true

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

fun createChannelIcons(number: Int = 10, icons: ChannelIcons = ChannelIcons()): ChannelIcons {
    for (i in 1.. number) {
        icons.addIcon(
            ChannelIcon("홍튜브")
        )
    }
    return icons
}

class ChannelIconAdapter(
    private val itemList: ChannelIcons,
    private val inflater: LayoutInflater
): RecyclerView.Adapter<ChannelIconAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val channelName: TextView = itemView.findViewById(R.id.channel_icon_name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = inflater.inflate(R.layout.item_channel, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.channelName.text = itemList.iconList[position].name
    }

    override fun getItemCount(): Int {
        return itemList.iconList.size
    }
}

class ChannelIcons {
    val iconList = ArrayList<ChannelIcon>()
    fun addIcon(icon: ChannelIcon) {
        iconList.add(icon)
    }
}

class ChannelIcon(val name: String)