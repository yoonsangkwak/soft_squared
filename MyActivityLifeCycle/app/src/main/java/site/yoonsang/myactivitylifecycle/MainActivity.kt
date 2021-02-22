package site.yoonsang.myactivitylifecycle

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import site.yoonsang.myactivitylifecycle.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val fragmentHome by lazy { MainHomeFragment() }
    private val fragmentExplore by lazy { MainExploreFragment() }
    private val fragmentSubscribe by lazy { MainSubscribeFragment() }
    private val fragmentLibrary by lazy { MainLibraryFragment() }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.MyActivityLifeCycle)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bottomNavigationMain.run {
            setOnNavigationItemSelectedListener {
                when (it.itemId) {
                    R.id.menu_home -> changeFragment(fragmentHome)
                    R.id.menu_explore -> changeFragment(fragmentExplore)
                    R.id.menu_subscribe -> changeFragment(fragmentSubscribe)
                    R.id.menu_library -> changeFragment(fragmentLibrary)
                }
                true
            }
            selectedItemId = R.id.menu_home
        }
    }

    private fun changeFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.main_fragment_container, fragment)
            .commit()
    }
}