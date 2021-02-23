package site.yoonsang.mylistview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.recyclerview.widget.LinearLayoutManager
import site.yoonsang.mylistview.databinding.ActivityAddBinding

class AddActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val peopleList = arrayListOf<PeopleList>()

        for (i in 1..4) {
            peopleList.add(PeopleList("김덕배$i", R.drawable.user))
            peopleList.add(PeopleList("둘리$i", R.drawable.doollee))
            peopleList.add(PeopleList("도우너$i", R.drawable.douner))
            peopleList.add(PeopleList("마이콜$i", R.drawable.michol))
            peopleList.add(PeopleList("고길동$i", R.drawable.gogildong))
        }

        binding.addRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = PeopleListAdapter(context, peopleList)
        }

        binding.addCancel.setOnClickListener {
            finish()
        }
    }
}