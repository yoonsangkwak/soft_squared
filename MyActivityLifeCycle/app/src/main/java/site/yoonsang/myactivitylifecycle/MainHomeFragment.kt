package site.yoonsang.myactivitylifecycle

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import site.yoonsang.myactivitylifecycle.databinding.FragmentMainHomeBinding

class MainHomeFragment : Fragment() {

    private var _binding: FragmentMainHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainHomeBinding.inflate(layoutInflater, container, false)

        val videos = createVideos()

        binding.mainRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = VideoAdapter(videos, layoutInflater)
            isNestedScrollingEnabled = false
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

fun createVideos(number: Int = 5, videos: Videos = Videos()): Videos {
    for (i in 1..number) {
        videos.addVideo(
            Video("홍튜브 " + i + "번째 영상", "조회수 1" + i + "만회", "1개월 전")
        )
    }
    return videos
}

class VideoAdapter(
    private val itemList: Videos,
    private val inflater: LayoutInflater
) : RecyclerView.Adapter<VideoAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val videoTitle: TextView = itemView.findViewById(R.id.tv_video_title)
        val videoViewCount: TextView = itemView.findViewById(R.id.tv_view_count)
        val videoUploadDate: TextView = itemView.findViewById(R.id.tv_upload_date)

        init {
            itemView.setOnClickListener {
                val intent = Intent(itemView.context, PlayerActivity::class.java)
                intent.putExtra("title", videoTitle.text)
                intent.putExtra("viewCount", videoViewCount.text)
                intent.putExtra("date", videoUploadDate.text)
                itemView.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = inflater.inflate(R.layout.item_video, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.videoTitle.text = itemList.videoList[position].title
        holder.videoViewCount.text = itemList.videoList[position].viewCount
        holder.videoUploadDate.text = itemList.videoList[position].date
    }

    override fun getItemCount(): Int {
        return itemList.videoList.size
    }
}

class Videos {
    val videoList = ArrayList<Video>()
    fun addVideo(video: Video) {
        videoList.add(video)
    }
}

class Video(val title: String, val viewCount: String, val date: String)