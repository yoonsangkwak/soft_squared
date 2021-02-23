package site.yoonsang.mylistview

import android.content.Intent
import android.os.Bundle
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

        binding.chatAdd.setOnClickListener {
            val intent = Intent(context, AddActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
            startActivity(intent)
        }

//        val chatHelper = DBChatHelper(context!!, DB_CHAT, DB_VERSION)
//
//        binding.chatRecyclerView.apply {
//            layoutManager = LinearLayoutManager(context)
//            adapter = ChatAdapter(context!!, chatHelper.selectChatList())
//        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        val chatHelper = DBChatHelper(context!!, DB_CHAT, DB_VERSION)

        binding.chatRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = ChatAdapter(context!!, chatHelper.selectChatList())
        }
        binding.chatRecyclerView.adapter?.notifyDataSetChanged()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}