package site.yoonsang.listviewswitch

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import site.yoonsang.listviewswitch.databinding.ActivityMainBinding
import site.yoonsang.listviewswitch.databinding.ItemPersonBinding

data class Person(val name: String, val status: String, val profileImage: Int, var isSwitched: Boolean = false)

class MainActivity : AppCompatActivity() {

    var personList = arrayListOf<Person>()

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        for (i in 1..5) {
            personList.add(Person("둘리", "하이", R.drawable.doollee))
            personList.add(Person("도우너", "하이하이", R.drawable.douner))
            personList.add(Person("마이콜", "하이하이하이", R.drawable.michol))
            personList.add(Person("고길동", "하하", R.drawable.gogildong))
        }

        binding.mainListView.adapter = PersonAdapter(this, personList)

        binding.btnMain.setOnClickListener {
            val text = binding.etMain.text.toString()
            personList.add(Person(text, "기본 상태메세지", R.drawable.doollee))
            binding.etMain.setText("")
        }
    }
}

class PersonAdapter(
        context: Context,
        private val personList: ArrayList<Person>
) : BaseAdapter() {

    private val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    private lateinit var binding: ItemPersonBinding

    override fun getCount(): Int = personList.size

    override fun getItem(position: Int): Any = personList[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        binding = ItemPersonBinding.inflate(inflater, parent, false)

        binding.itemSwitch.setOnClickListener {
            personList[position].isSwitched = !personList[position].isSwitched
        }

        binding.itemImage.setImageResource(personList[position].profileImage)
        binding.itemName.text = personList[position].name
        binding.itemMessage.text = personList[position].status
        binding.itemSwitch.isChecked = personList[position].isSwitched

        return binding.root
    }
}