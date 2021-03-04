package site.yoonsang.myapi

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import site.yoonsang.myapi.databinding.FragmentDustDetailBinding

class DustFragmentAdapter(
    context: Context,
    private val list: ArrayList<Fragment>,
    private val pocket: HashMap<String, Double>
    ) :
    RecyclerView.Adapter<DustFragmentAdapter.ViewHolder>() {

    private val inflater =
        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    lateinit var binding: FragmentDustDetailBinding

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameFirst = binding.detailFirstName
        val imageFirst = binding.detailFirstStatusImage
        val statusFirst = binding.detailFirstStatusText
        val concFirst = binding.detailFirstConc
        val nameSecond = binding.detailSecondName
        val imageSecond = binding.detailSecondStatusImage
        val statusSecond = binding.detailSecondStatusText
        val concSecond = binding.detailSecondConc
        val nameThird = binding.detailThirdName
        val imageThird = binding.detailThirdStatusImage
        val statusThird = binding.detailThirdStatusText
        val concThird = binding.detailThirdConc
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = FragmentDustDetailBinding.inflate(inflater, parent, false)
        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (position % 2 == 0) {
            holder.nameFirst.text = "미세먼지"
            holder.concFirst.text = "${pocket["pm2_5"]} ㎍/㎥"
            holder.nameSecond.text = "초미세먼지"
            holder.concSecond.text = "${pocket["pm10"]} ㎍/㎥"
            holder.nameThird.text = "이산화질소"
            holder.concThird.text = "${"%.3f".format(pocket["no2"]?.div(1000))} ppm"
        } else {
            holder.nameFirst.text = "오존"
            holder.concFirst.text = "${"%.3f".format(pocket["o3"]?.div(1000))} ppm"
            holder.nameSecond.text = "일산화탄소"
            holder.concSecond.text = "${"%.3f".format(pocket["co"]?.div(1000))} ppm"
            holder.nameThird.text = "아황산가스"
            holder.concThird.text = "${"%.3f".format(pocket["so2"]?.div(1000))} ppm"
        }
    }

    override fun getItemCount(): Int = Int.MAX_VALUE
}