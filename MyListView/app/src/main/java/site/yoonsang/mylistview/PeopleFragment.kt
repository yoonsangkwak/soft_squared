package site.yoonsang.mylistview

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import site.yoonsang.mylistview.databinding.FragmentPeopleBinding

class PeopleFragment : Fragment() {

    private var _binding: FragmentPeopleBinding? = null
    private val binding get() = _binding!!
    private val fragmentPeopleActive by lazy { PeopleActiveFragment() }
    private val fragmentPeopleStory by lazy { PeopleStoryFragment() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPeopleBinding.inflate(layoutInflater, container, false)

        setActive()
        var isActive = false

        binding.peopleActive.setOnClickListener {
            if (isActive) {
                setActive()
                isActive = false
            }
        }
        binding.peopleStory.setOnClickListener {
            if (!isActive) {
                setStory()
                isActive = true
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun changeFragment(fragment: Fragment) {
        childFragmentManager
            .beginTransaction()
            .replace(binding.peopleFragmentContainer.id, fragment)
            .commit()
    }

    private fun setActive() {
        binding.peopleActive.setBackgroundResource(R.drawable.chatting_edittext_background)
        binding.peopleActive.setTextColor(ContextCompat.getColor(context!!, R.color.black))
        binding.peopleStory.setBackgroundResource(R.color.white)
        binding.peopleStory.setTextColor(ContextCompat.getColor(context!!, R.color.gray))
        changeFragment(fragmentPeopleActive)
    }

    private fun setStory() {
        binding.peopleStory.setBackgroundResource(R.drawable.chatting_edittext_background)
        binding.peopleStory.setTextColor(ContextCompat.getColor(context!!, R.color.black))
        binding.peopleActive.setBackgroundResource(R.color.white)
        binding.peopleActive.setTextColor(ContextCompat.getColor(context!!, R.color.gray))
        changeFragment(fragmentPeopleStory)
    }
}