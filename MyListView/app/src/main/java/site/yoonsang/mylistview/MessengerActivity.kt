package site.yoonsang.mylistview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ScrollView
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import site.yoonsang.mylistview.databinding.ActivityMessengerBinding

class MessengerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMessengerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMessengerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.messengerTopOppositeImage.setImageResource(intent.getIntExtra("image", 0))
        binding.messengerOppositeImage.setImageResource(intent.getIntExtra("image", 0))
        binding.messengerTopOppositeName.text = intent.getStringExtra("name")
        binding.messengerOppositeName.text = intent.getStringExtra("name")

        val helper = DBHelper(this, binding.messengerOppositeName.text.toString(), DB_VERSION)
        val myMessage = helper.selectMyChat()
        val adapter = MyMessageAdapter(this)
        binding.messengerMyChatRecyclerView.adapter = adapter
        binding.messengerMyChatRecyclerView.layoutManager = LinearLayoutManager(this)
        adapter.messageList.addAll(myMessage)

        val chatHelper = DBChatHelper(this, DB_CHAT, DB_VERSION)

        binding.likeOrSend.setOnClickListener {
            val message = binding.messengerMyChatEditText.text.trim().toString()
            if (message.isNotEmpty()) {
                helper.insertMyChat(Message(message))
                val currentTime = System.currentTimeMillis()
                val chat = chatHelper.searchChat(binding.messengerOppositeName.text.toString())
                if (chat != null) {
                    chatHelper.updateChatList(Chat(chat.name, message, currentTime, chat.profileImage))
                }
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
            finish()
        }
    }

    override fun onBackPressed() {
        finish()
    }
}