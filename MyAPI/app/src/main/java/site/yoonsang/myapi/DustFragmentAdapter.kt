package site.yoonsang.myapi

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import site.yoonsang.myapi.databinding.FragmentDustDetailBinding

class DustFragmentAdapter(context: Context, private val list: ArrayList<Fragment>) : RecyclerView.Adapter<DustFragmentAdapter.ViewHolder>() {

    private val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    lateinit var binding: FragmentDustDetailBinding

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = FragmentDustDetailBinding.inflate(inflater, parent, false)
        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val index = position % list.size
        val item = list[index]
        if (position % 2 == 0) {
        } else {
        }
    }

    override fun getItemCount(): Int = Int.MAX_VALUE
}