package site.yoonsang.mylistview

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat.getSystemService
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

        val chatHelper = DBChatHelper(context!!, DB_CHAT, DB_VERSION)
        val chatList = chatHelper.selectChatList()
        val chatAdapter = ChatAdapter(context!!, chatList)

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