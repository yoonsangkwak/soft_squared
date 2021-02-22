package site.yoonsang.mylistview

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import site.yoonsang.mylistview.databinding.FragmentChatBinding

class ChatFragment : Fragment() {

    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!
    private val fragmentChatSearch by lazy { ChatSearchFragment() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatBinding.inflate(layoutInflater, container, false)

        val videoCallList = arrayListOf<VideoCall>()
        for (i in 0..8) {
            videoCallList.add(VideoCall("김덕배", R.drawable.user))
        }
        binding.chatVideoCallRecyclerView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = VideoCallAdapter(context, videoCallList)
        }

        binding.chatEdittext.setOnClickListener {
            fragmentManager!!
                .beginTransaction()
                .addToBackStack(null)
                .replace(R.id.main_fragment_container, fragmentChatSearch)
                .commit()
        }

        return binding.root
    }

    override fun onResume() {
        val chatList = arrayListOf<Chat>()
        for (i in 0 until 8 step 4) {
            chatList.add(Chat(i, "둘리$i", "", MyApplication.prefs.getLong("${i}time"), R.drawable.doollee))
            chatList.add(Chat(i+1, "도우너${i+1}", "", MyApplication.prefs.getLong("${i+1}time"), R.drawable.douner))
            chatList.add(Chat(i+2, "마이콜${i+2}", "", MyApplication.prefs.getLong("${i+2}time"), R.drawable.michol))
            chatList.add(Chat(i+3, "고길동${i+3}", "", MyApplication.prefs.getLong("${i+3}time"), R.drawable.gogildong))
        }
        chatList.sortByDescending { it.uploadDate }
        binding.chatRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = ChatAdapter(context, chatList)
            isNestedScrollingEnabled = false
        }
        binding.chatRecyclerView.adapter?.notifyDataSetChanged()
        super.onResume()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}