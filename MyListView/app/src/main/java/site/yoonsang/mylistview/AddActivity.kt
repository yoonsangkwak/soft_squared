package site.yoonsang.mylistview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
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

        val peopleListAdapter = PeopleListAdapter(this, peopleList)

        binding.addRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = peopleListAdapter
        }

        binding.addEdittext.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                peopleListAdapter.filter.filter(s.toString())
                peopleListAdapter.notifyDataSetChanged()
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        binding.addCancel.setOnClickListener {
            finish()
        }
    }
}