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
            holder.concFirst.text = "${pocket["pm10"]} ㎍/㎥"
            if (pocket["pm10"] != null) {
                if (pocket["pm10"]!! >= 0 && pocket["pm10"]!! < 30) {
                    holder.statusFirst.text = "좋음"
                    holder.imageFirst.setImageResource(R.drawable.ic_very_good)
                } else if (pocket["pm10"]!! >= 39 && pocket["pm10"]!! < 50) {
                    holder.statusFirst.text = "양호"
                    holder.imageFirst.setImageResource(R.drawable.ic_good)
                } else if (pocket["pm10"]!! >= 50 && pocket["pm10"]!! < 100) {
                    holder.statusFirst.text = "나쁨"
                    holder.imageFirst.setImageResource(R.drawable.ic_bad)
                } else if (pocket["pm10"]!! >= 100) {
                    holder.statusFirst.text = "상당히 나쁨"
                    holder.imageFirst.setImageResource(R.drawable.ic_very_bad)
                }
            }
            holder.nameSecond.text = "초미세먼지"
            holder.concSecond.text = "${pocket["pm2_5"]} ㎍/㎥"
            if (pocket["pm2_5"] != null) {
                if (pocket["pm2_5"]!! >= 0 && pocket["pm2_5"]!! < 15) {
                    holder.statusSecond.text = "좋음"
                    holder.imageSecond.setImageResource(R.drawable.ic_very_good)
                } else if (pocket["pm2_5"]!! >= 15 && pocket["pm2_5"]!! < 25) {
                    holder.statusSecond.text = "양호"
                    holder.imageSecond.setImageResource(R.drawable.ic_good)
                } else if (pocket["pm2_5"]!! >= 25 && pocket["pm2_5"]!! < 50) {
                    holder.statusSecond.text = "나쁨"
                    holder.imageSecond.setImageResource(R.drawable.ic_bad)
                } else if (pocket["pm2_5"]!! >= 50) {
                    holder.statusSecond.text = "상당히 나쁨"
                    holder.imageSecond.setImageResource(R.drawable.ic_very_bad)
                }
            }
            holder.nameThird.text = "이산화질소"
            holder.concThird.text = "${"%.3f".format(pocket["no2"]?.div(1000))} ppm"
            if (pocket["no2"] != null) {
                if (pocket["no2"]!! >= 0 && pocket["no2"]!! < 30) {
                    holder.statusThird.text = "좋음"
                    holder.imageThird.setImageResource(R.drawable.ic_very_good)
                } else if (pocket["no2"]!! >= 30 && pocket["no2"]!! < 60) {
                    holder.statusThird.text = "양호"
                    holder.imageThird.setImageResource(R.drawable.ic_good)
                } else if (pocket["no2"]!! >= 60 && pocket["no2"]!! < 200) {
                    holder.statusThird.text = "나쁨"
                    holder.imageThird.setImageResource(R.drawable.ic_bad)
                } else if (pocket["no2"]!! >= 200) {
                    holder.statusThird.text = "상당히 나쁨"
                    holder.imageThird.setImageResource(R.drawable.ic_very_bad)
                }
            }
        } else {
            holder.nameFirst.text = "오존"
            holder.concFirst.text = "${"%.3f".format(pocket["o3"]?.div(1000))} ppm"
            if (pocket["o3"] != null) {
                if (pocket["o3"]!! >= 0 && pocket["o3"]!! < 30) {
                    holder.statusFirst.text = "좋음"
                    holder.imageFirst.setImageResource(R.drawable.ic_very_good)
                } else if (pocket["o3"]!! >= 30 && pocket["o3"]!! < 90) {
                    holder.statusFirst.text = "양호"
                    holder.imageFirst.setImageResource(R.drawable.ic_good)
                } else if (pocket["o3"]!! >= 90 && pocket["o3"]!! < 150) {
                    holder.statusFirst.text = "나쁨"
                    holder.imageFirst.setImageResource(R.drawable.ic_bad)
                } else if (pocket["o3"]!! >= 150) {
                    holder.statusFirst.text = "상당히 나쁨"
                    holder.imageFirst.setImageResource(R.drawable.ic_very_bad)
                }
            }
            holder.nameSecond.text = "일산화탄소"
            holder.concSecond.text = "${"%.3f".format(pocket["co"]?.div(1000))} ppm"
            if (pocket["co"] != null) {
                if (pocket["co"]!! >= 0 && pocket["co"]!! < 2000) {
                    holder.statusSecond.text = "좋음"
                    holder.imageSecond.setImageResource(R.drawable.ic_very_good)
                } else if (pocket["co"]!! >= 2000 && pocket["co"]!! < 9000) {
                    holder.statusSecond.text = "양호"
                    holder.imageSecond.setImageResource(R.drawable.ic_good)
                } else if (pocket["co"]!! >= 9000 && pocket["co"]!! < 15000) {
                    holder.statusSecond.text = "나쁨"
                    holder.imageSecond.setImageResource(R.drawable.ic_bad)
                } else if (pocket["co"]!! >= 15000) {
                    holder.statusSecond.text = "상당히 나쁨"
                    holder.imageSecond.setImageResource(R.drawable.ic_very_bad)
                }
            }
            holder.nameThird.text = "아황산가스"
            holder.concThird.text = "${"%.3f".format(pocket["so2"]?.div(1000))} ppm"
            if (pocket["so2"] != null) {
                if (pocket["so2"]!! >= 0 && pocket["so2"]!! < 20) {
                    holder.statusThird.text = "좋음"
                    holder.imageThird.setImageResource(R.drawable.ic_very_good)
                } else if (pocket["so2"]!! >= 20 && pocket["so2"]!! < 50) {
                    holder.statusThird.text = "양호"
                    holder.imageThird.setImageResource(R.drawable.ic_good)
                } else if (pocket["so2"]!! >= 50 && pocket["so2"]!! < 150) {
                    holder.statusThird.text = "나쁨"
                    holder.imageThird.setImageResource(R.drawable.ic_bad)
                } else if (pocket["so2"]!! >= 150) {
                    holder.statusThird.text = "상당히 나쁨"
                    holder.imageThird.setImageResource(R.drawable.ic_very_bad)
                }
            }
        }
    }

    override fun getItemCount(): Int = Int.MAX_VALUE
}