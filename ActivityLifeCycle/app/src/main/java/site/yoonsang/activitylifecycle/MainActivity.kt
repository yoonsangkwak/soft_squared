package site.yoonsang.activitylifecycle

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import site.yoonsang.activitylifecycle.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val fragmentFriends = FriendsFragment()
    private val fragmentChat = ChatFragment()
    private val fragmentMore = MoreFragment()
    private val fragmentHash = HashFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("checkkk", "main onCreate")
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        startActivityForResult(Intent(this, LockActivity::class.java), 1)

        binding.bottomNavigationMain.run {
            setOnNavigationItemSelectedListener {
                when (it.itemId) {
                    R.id.first -> {
                        changeFragment(fragmentFriends)
                    }
                    R.id.second -> {
                        changeFragment(fragmentChat)
                    }
                    R.id.third -> {
                        changeFragment(fragmentHash)
                    }
                    R.id.fourth -> {
                        changeFragment(fragmentMore)
                    }
                }
                true
            }
            selectedItemId = R.id.first
        }
    }

    override fun onStart() {
        Log.d("checkkk", "main onStart")
        super.onStart()
    }

    override fun onResume() {
        Log.d("checkkk", "main onResume")
        super.onResume()
    }

    override fun onPause() {
        Log.d("checkkk", "main onPause")
        super.onPause()
    }

    override fun onStop() {
        Log.d("checkkk", "main onStop")
        super.onStop()
    }

    override fun onDestroy() {
        Log.d("checkkk", "main onDestroy")
        super.onDestroy()
    }

    override fun onRestart() {
        Log.d("checkkk", "main onRestart")
        super.onRestart()
    }

    private fun changeFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}