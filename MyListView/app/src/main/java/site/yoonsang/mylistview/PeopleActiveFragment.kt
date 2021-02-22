package site.yoonsang.mylistview

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import site.yoonsang.mylistview.databinding.FragmentPeopleActiveBinding

class PeopleActiveFragment : Fragment() {

    private var _binding: FragmentPeopleActiveBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPeopleActiveBinding.inflate(layoutInflater, container, false)

        val peopleActiveList = arrayListOf<PeopleActive>()

        for (i in 1..10) {
            peopleActiveList.add(PeopleActive("김덕배$i", R.drawable.user))
        }

        binding.peopleActiveRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = PeopleActiveAdapter(context, peopleActiveList)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}