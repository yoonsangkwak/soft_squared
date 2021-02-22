package site.yoonsang.mylistview

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import site.yoonsang.mylistview.databinding.FragmentChatSearchBinding

class ChatSearchFragment : Fragment() {

    private var _binding: FragmentChatSearchBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatSearchBinding.inflate(layoutInflater, container, false)

        val chatList = arrayListOf<Chat>()
        for (i in 0 until 8 step 4) {
            chatList.add(Chat(i, "둘리$i", "", MyApplication.prefs.getLong("${i}time"), R.drawable.doollee))
            chatList.add(Chat(i+1, "도우너${i+1}", "", MyApplication.prefs.getLong("${i+1}time"), R.drawable.douner))
            chatList.add(Chat(i+2, "마이콜${i+2}", "", MyApplication.prefs.getLong("${i+2}time"), R.drawable.michol))
            chatList.add(Chat(i+3, "고길동${i+3}", "", MyApplication.prefs.getLong("${i+3}time"), R.drawable.gogildong))
        }
        chatList.sortByDescending { it.uploadDate }
        val chatAdapter = ChatAdapter(context!!, chatList)

        binding.chatEdittext.requestFocus()

        binding.searchCancel.setOnClickListener {
            fragmentManager?.popBackStack()
        }

        binding.searchRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = chatAdapter
        }

        binding.chatEdittext.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                chatAdapter.filter.filter(s)
                chatAdapter.notifyDataSetChanged()
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}