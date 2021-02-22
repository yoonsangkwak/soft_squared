package site.yoonsang.myactivitylifecycle

import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import site.yoonsang.myactivitylifecycle.databinding.FragmentPlayerCommentBinding

class PlayerCommentFragment : Fragment() {

    private var _binding: FragmentPlayerCommentBinding? = null
    private val binding get() = _binding!!
    private val fragmentReply by lazy { PlayerCommentReplyFragment() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlayerCommentBinding.inflate(layoutInflater, container, false)

        val comments = createComments()
        binding.commentRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = CommentAdapter(comments, layoutInflater,
                activity as PlayerActivity, fragmentReply)
            isNestedScrollingEnabled = false
        }

        binding.commentFragmentClose.setOnClickListener {
            fragmentManager?.popBackStack()
        }


        val content: String = binding.commentInfo.text.toString()
        val spannableString = SpannableString(content)
        val word = "커뮤니티 가이드"
        val start = content.indexOf(word)
        val end = start + word.length
        spannableString.setSpan(
            ForegroundColorSpan(Color.parseColor("#2D5BB3")),
            start,
            end,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        binding.commentInfo.text = spannableString

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

private fun createComments(number: Int = 5, comments: Comments = Comments()): Comments {
    for (i in 1.. number) {
        comments.addComment(
            Comment("홍길동$i", "재밌네 ㅋㅋㅋㅋ", "3일 전")
        )
    }
    return comments
}

class CommentAdapter(
    private val itemList: Comments,
    private val inflater: LayoutInflater,
    private val activity: PlayerActivity,
    private val fragment: PlayerCommentReplyFragment
): RecyclerView.Adapter<CommentAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val commenterName: TextView = itemView.findViewById(R.id.comment_channel_name)
        val commentContent: TextView = itemView.findViewById(R.id.comment_comment_content)
        val commentUploadDate: TextView = itemView.findViewById(R.id.comment_upload_date)
        private val reply: TextView = itemView.findViewById(R.id.comment_reply)

        init {
            reply.setOnClickListener {
                val bundle = Bundle()
                bundle.putString("name", commenterName.text.toString())
                bundle.putString("content", commentContent.text.toString())
                bundle.putString("uploadDate", commentUploadDate.text.toString())
                fragment.arguments = bundle
                activity.supportFragmentManager.beginTransaction()
                    .setCustomAnimations(R.anim.enter_from_right, R.anim.hold)
                    .addToBackStack(null)
                    .replace(R.id.player_fragment_container, fragment)
                    .commit()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = inflater.inflate(R.layout.item_comment, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.commenterName.text = itemList.commentList[position].name
        holder.commentContent.text = itemList.commentList[position].content
        holder.commentUploadDate.text = itemList.commentList[position].date
    }

    override fun getItemCount(): Int {
        return itemList.commentList.size
    }
}

class Comments {
    val commentList = ArrayList<Comment>()
    fun addComment(comment: Comment) {
        commentList.add(comment)
    }
}

class Comment(val name: String, val content: String, val date: String)