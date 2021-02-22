package site.yoonsang.activitylifecycle

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import site.yoonsang.activitylifecycle.databinding.FragmentFriendsBinding

class FriendsFragment : Fragment() {

    private var _binding: FragmentFriendsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFriendsBinding.inflate(inflater, container, false)

        val friends = createFriends()
        binding.friendsRecyclerView.apply {
            layoutManager = LinearLayoutManager(activity)
            isNestedScrollingEnabled = false
            adapter = FriendsAdapter(friends, layoutInflater)
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun createFriends(number: Int = 10, friends: Friends = Friends()): Friends {
        for (i in 1..number) {
            friends.addPerson(
                Person("홍길동"+i)
            )
        }
        return friends
    }
}

class FriendsAdapter(
    val itemList: Friends,
    val inflater: LayoutInflater
): RecyclerView.Adapter<FriendsAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val personName: TextView
        init {
            personName = itemView.findViewById(R.id.friend_name)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = inflater.inflate(R.layout.item_friends, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.personName.setText(itemList.friendsList.get(position).name)
    }

    override fun getItemCount(): Int {
        return itemList.friendsList.size
    }
}

class Friends() {
    val friendsList = ArrayList<Person>()

    fun addPerson(person: Person) {
        friendsList.add(person)
    }
}

class Person(val name: String)