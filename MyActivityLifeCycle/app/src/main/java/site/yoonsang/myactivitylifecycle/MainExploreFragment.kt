package site.yoonsang.myactivitylifecycle

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import site.yoonsang.myactivitylifecycle.databinding.FragmentMainExploreBinding

class MainExploreFragment : Fragment() {

    private var _binding: FragmentMainExploreBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainExploreBinding.inflate(layoutInflater, container, false)

        val videos = createVideos()

        binding.mainExploreRecyclerView.apply {
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