package site.yoonsang.myapi

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import site.yoonsang.myapi.databinding.FragmentDustDetailBinding

class DustFragmentAdapter(
    context: Context,
    private val dustConcentration: HashMap<String, Double>
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
            holder.concFirst.text = "${dustConcentration["pm10"]?.toInt()} ㎍/㎥"
            if (dustConcentration["pm10"] != null) {
                if (dustConcentration["pm10"]!! >= 0 && dustConcentration["pm10"]!! < 30) {
                    holder.statusFirst.text = "좋음"
                    holder.imageFirst.setImageResource(R.drawable.ic_very_good)
                } else if (dustConcentration["pm10"]!! >= 30 && dustConcentration["pm10"]!! < 50) {
                    holder.statusFirst.text = "보통"
                    holder.imageFirst.setImageResource(R.drawable.ic_good)
                } else if (dustConcentration["pm10"]!! >= 50 && dustConcentration["pm10"]!! < 100) {
                    holder.statusFirst.text = "나쁨"
                    holder.imageFirst.setImageResource(R.drawable.ic_bad)
                } else if (dustConcentration["pm10"]!! >= 100) {
                    holder.statusFirst.text = "매우 나쁨"
                    holder.imageFirst.setImageResource(R.drawable.ic_very_bad)
                }
            }
            holder.nameSecond.text = "초미세먼지"
            holder.concSecond.text = "${dustConcentration["pm2_5"]?.toInt()} ㎍/㎥"
            if (dustConcentration["pm2_5"] != null) {
                if (dustConcentration["pm2_5"]!! >= 0 && dustConcentration["pm2_5"]!! < 15) {
                    holder.statusSecond.text = "좋음"
                    holder.imageSecond.setImageResource(R.drawable.ic_very_good)
                } else if (dustConcentration["pm2_5"]!! >= 15 && dustConcentration["pm2_5"]!! < 25) {
                    holder.statusSecond.text = "보통"
                    holder.imageSecond.setImageResource(R.drawable.ic_good)
                } else if (dustConcentration["pm2_5"]!! >= 25 && dustConcentration["pm2_5"]!! < 50) {
                    holder.statusSecond.text = "나쁨"
                    holder.imageSecond.setImageResource(R.drawable.ic_bad)
                } else if (dustConcentration["pm2_5"]!! >= 50) {
                    holder.statusSecond.text = "매우 나쁨"
                    holder.imageSecond.setImageResource(R.drawable.ic_very_bad)
                }
            }
            holder.nameThird.text = "이산화질소"
            holder.concThird.text = "${"%.3f".format(dustConcentration["no2"]?.div(1000))} ppm"
            if (dustConcentration["no2"] != null) {
                if (dustConcentration["no2"]!! >= 0 && dustConcentration["no2"]!! < 30) {
                    holder.statusThird.text = "좋음"
                    holder.imageThird.setImageResource(R.drawable.ic_very_good)
                } else if (dustConcentration["no2"]!! >= 30 && dustConcentration["no2"]!! < 60) {
                    holder.statusThird.text = "보통"
                    holder.imageThird.setImageResource(R.drawable.ic_good)
                } else if (dustConcentration["no2"]!! >= 60 && dustConcentration["no2"]!! < 200) {
                    holder.statusThird.text = "나쁨"
                    holder.imageThird.setImageResource(R.drawable.ic_bad)
                } else if (dustConcentration["no2"]!! >= 200) {
                    holder.statusThird.text = "매우 나쁨"
                    holder.imageThird.setImageResource(R.drawable.ic_very_bad)
                }
            }
        } else {
            holder.nameFirst.text = "오존"
            holder.concFirst.text = "${"%.3f".format(dustConcentration["o3"]?.div(1000))} ppm"
            if (dustConcentration["o3"] != null) {
                if (dustConcentration["o3"]!! >= 0 && dustConcentration["o3"]!! < 30) {
                    holder.statusFirst.text = "좋음"
                    holder.imageFirst.setImageResource(R.drawable.ic_very_good)
                } else if (dustConcentration["o3"]!! >= 30 && dustConcentration["o3"]!! < 90) {
                    holder.statusFirst.text = "보통"
                    holder.imageFirst.setImageResource(R.drawable.ic_good)
                } else if (dustConcentration["o3"]!! >= 90 && dustConcentration["o3"]!! < 150) {
                    holder.statusFirst.text = "나쁨"
                    holder.imageFirst.setImageResource(R.drawable.ic_bad)
                } else if (dustConcentration["o3"]!! >= 150) {
                    holder.statusFirst.text = "매우 나쁨"
                    holder.imageFirst.setImageResource(R.drawable.ic_very_bad)
                }
            }
            holder.nameSecond.text = "일산화탄소"
            holder.concSecond.text = "${"%.3f".format(dustConcentration["co"]?.div(1000))} ppm"
            if (dustConcentration["co"] != null) {
                if (dustConcentration["co"]!! >= 0 && dustConcentration["co"]!! < 2000) {
                    holder.statusSecond.text = "좋음"
                    holder.imageSecond.setImageResource(R.drawable.ic_very_good)
                } else if (dustConcentration["co"]!! >= 2000 && dustConcentration["co"]!! < 9000) {
                    holder.statusSecond.text = "보통"
                    holder.imageSecond.setImageResource(R.drawable.ic_good)
                } else if (dustConcentration["co"]!! >= 9000 && dustConcentration["co"]!! < 15000) {
                    holder.statusSecond.text = "나쁨"
                    holder.imageSecond.setImageResource(R.drawable.ic_bad)
                } else if (dustConcentration["co"]!! >= 15000) {
                    holder.statusSecond.text = "매우 나쁨"
                    holder.imageSecond.setImageResource(R.drawable.ic_very_bad)
                }
            }
            holder.nameThird.text = "아황산가스"
            holder.concThird.text = "${"%.3f".format(dustConcentration["so2"]?.div(1000))} ppm"
            if (dustConcentration["so2"] != null) {
                if (dustConcentration["so2"]!! >= 0 && dustConcentration["so2"]!! < 20) {
                    holder.statusThird.text = "좋음"
                    holder.imageThird.setImageResource(R.drawable.ic_very_good)
                } else if (dustConcentration["so2"]!! >= 20 && dustConcentration["so2"]!! < 50) {
                    holder.statusThird.text = "보통"
                    holder.imageThird.setImageResource(R.drawable.ic_good)
                } else if (dustConcentration["so2"]!! >= 50 && dustConcentration["so2"]!! < 150) {
                    holder.statusThird.text = "나쁨"
                    holder.imageThird.setImageResource(R.drawable.ic_bad)
                } else if (dustConcentration["so2"]!! >= 150) {
                    holder.statusThird.text = "매우 나쁨"
                    holder.imageThird.setImageResource(R.drawable.ic_very_bad)
                }
            }
        }
    }

    override fun getItemCount(): Int = Int.MAX_VALUE
}