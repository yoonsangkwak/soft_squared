package site.yoonsang.mylistview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import site.yoonsang.mylistview.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val fragmentChat by lazy { ChatFragment() }
    private val fragmentPeople by lazy { PeopleFragment() }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.MyListView)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.mainBottomNavigation.run {
            setOnNavigationItemSelectedListener {
                when (it.itemId) {
                    R.id.menu_chat -> changeFragment(fragmentChat)
                    R.id.menu_people -> changeFragment(fragmentPeople)
                }
                true
            }
            selectedItemId = R.id.menu_chat
        }
    }

    private fun changeFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(binding.mainFragmentContainer.id, fragment)
            .commit()
    }
}