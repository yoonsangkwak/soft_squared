package site.yoonsang.mylistview

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ScrollView
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import site.yoonsang.mylistview.databinding.ActivityMessengerBinding

class MessengerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMessengerBinding
    var id = 0
    var lastMessage = ""
    var currentTime = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMessengerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.messengerTopOppositeImage.setImageResource(intent.getIntExtra("image", 0))
        binding.messengerOppositeImage.setImageResource(intent.getIntExtra("image", 0))
        binding.messengerOppositeChatImage.setImageResource(intent.getIntExtra("image", 0))
        binding.messengerTopOppositeName.text = intent.getStringExtra("name")
        binding.messengerOppositeName.text = intent.getStringExtra("name")

        id = intent.getIntExtra("id", 0)
        lastMessage = MyApplication.prefs.getString("${id}lastMessage")
        currentTime = MyApplication.prefs.getLong("${id}time")

        val helper = DBHelper(this, "$id", DB_VERSION)
        val myMessage = helper.selectMyChat()
        val adapter = MyMessageAdapter(this)
        binding.messengerMyChatRecyclerView.adapter = adapter
        binding.messengerMyChatRecyclerView.layoutManager = LinearLayoutManager(this)
        adapter.messageList.addAll(myMessage)

        binding.likeOrSend.setOnClickListener {
            val message = binding.messengerMyChatEditText.text.trim().toString()
            if (message.isNotEmpty()) {
                helper.insertMyChat(Message(message))
                lastMessage = message
                currentTime = System.currentTimeMillis()
                binding.messengerMyChatEditText.setText("")
                adapter.messageList.add(Message(message))
                adapter.notifyDataSetChanged()
                binding.messengerScroll.fullScroll(ScrollView.FOCUS_DOWN)
            }
        }

        binding.messengerMyChatEditText.addTextChangedListener {
            if (binding.messengerMyChatEditText.text.trim().toString().isNotEmpty()) {
                binding.likeOrSend.setImageResource(R.drawable.ic_send)
            } else {
                binding.likeOrSend.setImageResource(R.drawable.ic_like)
            }
        }

        binding.messengerArrowBack.setOnClickListener {
            MyApplication.prefs.setString("${id}lastMessage", lastMessage)
            MyApplication.prefs.setLong("${id}time", currentTime)
            finish()
        }
    }

    override fun onBackPressed() {
        MyApplication.prefs.setString("${id}lastMessage", lastMessage)
        MyApplication.prefs.setLong("${id}time", currentTime)
        finish()
    }
}