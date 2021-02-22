package site.yoonsang.myactivitylifecycle

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import site.yoonsang.myactivitylifecycle.databinding.FragmentPlayerBasicBinding

class PlayerBasicFragment : Fragment() {

    private var _binding: FragmentPlayerBasicBinding? = null
    private val binding get() = _binding!!
    private val fragmentComment by lazy { PlayerCommentFragment() }

    private var isLike = false
    private var isHate = false
    private var isSubscribe = MyApplication.prefs.getBoolean("isSubscribe")

    private lateinit var isLikeKey: String
    private lateinit var isHateKey: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlayerBasicBinding.inflate(layoutInflater, container, false)

        arguments?.let {
            binding.playerTitle.text = it.getString("title")
            binding.playerViewCount.text = it.getString("viewCount")
            binding.playerUploadDate.text = it.getString("date")
        }

        isLikeKey = binding.playerTitle.text.toString() + "isLike"
        isHateKey = binding.playerTitle.text.toString() + "isHate"

        isLike = MyApplication.prefs.getBoolean(isLikeKey)
        isHate = MyApplication.prefs.getBoolean(isHateKey)

        val videos = createVideos()

        binding.playerRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = VideoAdapter(videos, layoutInflater)
            isNestedScrollingEnabled = false
        }

        binding.playerCommentContent.setOnClickListener {
            activity?.supportFragmentManager
                ?.beginTransaction()
                ?.setCustomAnimations(R.anim.enter_from_bottom, R.anim.hold)
                ?.addToBackStack(null)
                ?.replace(R.id.player_fragment_container, fragmentComment)
                ?.commit()
        }

        if (!isLike) binding.playerLikeImage.setImageResource(R.drawable.ic_like)
        else binding.playerLikeImage.setImageResource(R.drawable.ic_like_active)

        if (!isHate) binding.playerHateImage.setImageResource(R.drawable.ic_hate)
        else binding.playerHateImage.setImageResource(R.drawable.ic_hate_active)

        binding.playerLikeImage.setOnClickListener {
            when {
                !isLike and !isHate -> likeVideo()
                !isLike and isHate -> {
                    hateVideoCancel()
                    likeVideo()
                }
                isLike and !isHate -> {
                    likeVideoCancel()
                    Snackbar.make(view!!, "좋아요 표시한 동영상에서 삭제됨", Snackbar.LENGTH_SHORT).show()
                }
            }
        }

        binding.playerHateImage.setOnClickListener {
            when {
                !isHate and !isLike -> hateVideo()
                !isHate and isLike -> {
                    likeVideoCancel()
                    hateVideo()
                }
                isHate and !isLike -> {
                    hateVideoCancel()
                    Snackbar.make(view!!, "싫어요 표시가 삭제됨", Snackbar.LENGTH_SHORT).show()
                }
            }
        }

        val layout = binding.subscribeLayout
        val layoutParams =
            ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        val notificationOff = ImageView(context)

        if (!isSubscribe) {
            binding.btnSubscribe.text = "구독"
            binding.btnSubscribe.setTextColor(Color.RED)
        } else {
            binding.btnSubscribe.text = "구독중"
            binding.btnSubscribe.setTextColor(Color.BLACK)
            notificationOff.setImageResource(R.drawable.ic_notification_off)
            notificationOff.setColorFilter(Color.BLACK)
            notificationOff.layoutParams = layoutParams
            layout.addView(notificationOff)
        }

        binding.btnSubscribe.setOnClickListener {
            if (!isSubscribe) {
                binding.btnSubscribe.text = "구독중"
                binding.btnSubscribe.setTextColor(Color.BLACK)
                notificationOff.setImageResource(R.drawable.ic_notification_off)
                notificationOff.setColorFilter(Color.BLACK)
                notificationOff.layoutParams = layoutParams
                layout.addView(notificationOff)
                isSubscribe = true
                MyApplication.prefs.setBoolean("isSubscribe", isSubscribe)
            } else {
                AlertDialog.Builder(requireContext())
                    .setMessage(binding.playerChannelName.text.toString() + " 구독을 취소하시겠습니까?")
                    .setPositiveButton("구독 취소") { _, _ ->
                        binding.btnSubscribe.text = "구독"
                        binding.btnSubscribe.setTextColor(Color.RED)
                        layout.removeView(notificationOff)

                        val snackBar = Snackbar.make(view!!, "구독을 취소했습니다.", Snackbar.LENGTH_SHORT)
                        snackBar.setAction("실행 취소") {
                            binding.btnSubscribe.text = "구독"
                            binding.btnSubscribe.setTextColor(Color.RED)
                            layout.removeView(notificationOff)
                        }
                        snackBar.show()
                        isSubscribe = false
                        MyApplication.prefs.setBoolean("isSubscribe", isSubscribe)
                    }
                    .setNegativeButton("취소") { _, _ -> }
                    .show()
            }
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun likeVideo() {
        binding.playerLikeImage.setImageResource(R.drawable.ic_like_active)
        isLike = true
        MyApplication.prefs.setBoolean(isLikeKey, isLike)
        Snackbar.make(view!!, "좋아요 표시한 동영상에 추가됨", Snackbar.LENGTH_SHORT).show()
    }

    private fun likeVideoCancel() {
        binding.playerLikeImage.setImageResource(R.drawable.ic_like)
        isLike = false
        MyApplication.prefs.setBoolean(isLikeKey, isLike)
    }

    private fun hateVideo() {
        binding.playerHateImage.setImageResource(R.drawable.ic_hate_active)
        isHate = true
        MyApplication.prefs.setBoolean(isHateKey, isHate)
        Snackbar.make(view!!, "동영상에 싫어요 표시를 했습니다.", Snackbar.LENGTH_SHORT).show()
    }

    private fun hateVideoCancel() {
        binding.playerHateImage.setImageResource(R.drawable.ic_hate)
        isHate = false
        MyApplication.prefs.setBoolean(isHateKey, isHate)
    }
}
