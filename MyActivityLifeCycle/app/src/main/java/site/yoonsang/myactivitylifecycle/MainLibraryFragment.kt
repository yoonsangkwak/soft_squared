package site.yoonsang.myactivitylifecycle

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import site.yoonsang.myactivitylifecycle.databinding.FragmentMainLibraryBinding

class MainLibraryFragment : Fragment() {

    private var _binding: FragmentMainLibraryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainLibraryBinding.inflate(layoutInflater, container, false)

        ArrayAdapter.createFromResource(
            context!!,
            R.array.playlist_array,
            android.R.layout.simple_spinner_dropdown_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.libraryOrderSpinner.adapter = adapter
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}