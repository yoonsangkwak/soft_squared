package site.yoonsang.myactivitylifecycle

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import site.yoonsang.myactivitylifecycle.databinding.FragmentPlayerCommentReplyBinding

class PlayerCommentReplyFragment : Fragment() {

    private var _binding: FragmentPlayerCommentReplyBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlayerCommentReplyBinding.inflate(layoutInflater, container, false)

        arguments?.let {
            binding.replyChannelName.text = it.getString("name")
            binding.replyCommentContent.text = it.getString("content")
            binding.replyUploadDate.text = it.getString("uploadDate")
        }

        binding.replyBack.setOnClickListener {
            fragmentManager?.popBackStack()
        }

        binding.replyFragmentClose.setOnClickListener {
            fragmentManager?.popBackStack()
            fragmentManager?.popBackStack()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}