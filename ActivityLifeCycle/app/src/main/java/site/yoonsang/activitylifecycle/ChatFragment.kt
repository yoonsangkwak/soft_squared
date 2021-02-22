package site.yoonsang.activitylifecycle

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import site.yoonsang.activitylifecycle.databinding.FragmentChatBinding
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.util.*
import kotlin.collections.ArrayList

class ChatFragment : Fragment() {

    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentChatBinding.inflate(inflater, container, false)

        val rooms = createRooms()
        binding.chatRoomsRecyclerView.apply {
            layoutManager = LinearLayoutManager(activity)
            isNestedScrollingEnabled = false
            adapter = RoomsAdapter(rooms, layoutInflater)
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun createRooms(number: Int = 10, rooms: Rooms = Rooms()): Rooms {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"))
        for (i in 1..number) {
            val time = System.currentTimeMillis()
            val dateFormat = SimpleDateFormat("a h:mm", Locale.KOREA)
            val curTime = dateFormat.format(time)
            rooms.addChatRoom(
                ChatRoom("홍길동"+i, "마지막 메세지"+i, curTime)
            )
        }
        return rooms
    }
}

class RoomsAdapter(
    private val itemList: Rooms,
    private val inflater: LayoutInflater
): RecyclerView.Adapter<RoomsAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val roomName: TextView
        val roomMessage: TextView
        val date: TextView
        init {
            roomName = itemView.findViewById(R.id.tv_chat_chat_name)
            roomMessage = itemView.findViewById(R.id.tv_chat_chat_message)
            date = itemView.findViewById(R.id.tv_chat_chat_date)
            itemView.setOnClickListener {
                val intent = Intent(itemView.context, TalkActivity::class.java)
                intent.putExtra("name", roomName.text)
                intent.putExtra("message", roomMessage.text)
                intent.putExtra("date", date.text)
                itemView.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = inflater.inflate(R.layout.item_chats, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.roomName.setText(itemList.roomList.get(position).name)
        holder.roomMessage.setText(itemList.roomList.get(position).message)
        holder.date.setText(itemList.roomList.get(position).timestamp)
    }

    override fun getItemCount(): Int {
        return itemList.roomList.size
    }
}

class Rooms() {
    val roomList = ArrayList<ChatRoom>()

    fun addChatRoom(chatRoom: ChatRoom) {
        roomList.add(chatRoom)
    }
}

class ChatRoom(val name: String, val message: String, val timestamp: String)