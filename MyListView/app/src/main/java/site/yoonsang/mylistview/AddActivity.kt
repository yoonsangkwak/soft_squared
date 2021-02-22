package site.yoonsang.mylistview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import site.yoonsang.mylistview.databinding.ActivityAddBinding

class AddActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val peopleActiveList = arrayListOf<PeopleActive>()

        for (i in 1..10) {
            peopleActiveList.add(PeopleActive("김덕배$i", R.drawable.user))
        }

        binding.addRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = PeopleActiveAdapter(context, peopleActiveList)
        }
    }
}