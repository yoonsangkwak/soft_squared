package site.yoonsang.mythread

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import site.yoonsang.mythread.databinding.SoundDialogBinding

class SoundDialog
constructor(context: Context) : Dialog(context) {
    private val binding: SoundDialogBinding = SoundDialogBinding.inflate(layoutInflater)
    private lateinit var listener: SoundDialogOKClickedListener
    private var bgmIsOn = MyApplication.prefs.getBoolean("bgm")
    private var sefIsOn = MyApplication.prefs.getBoolean("sef")

    fun start() {
        setCanceledOnTouchOutside(false)
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setContentView(binding.root)

        setBgm()
        setSef()

        binding.soundBackground.setOnClickListener {
            bgmIsOn = !bgmIsOn
            setBgm()
        }

        binding.soundEffect.setOnClickListener {
            sefIsOn = !sefIsOn
            setSef()
        }

        binding.soundOk.setOnClickListener {
            MyApplication.prefs.setBoolean("bgm", bgmIsOn)
            MyApplication.prefs.setBoolean("sef", sefIsOn)
            listener.onOKClicked(bgmIsOn)
            dismiss()
        }

        show()
    }

    fun setOnOKClickedListener(listener: (Boolean) -> Unit) {
        this.listener = object : SoundDialogOKClickedListener {
            override fun onOKClicked(content: Boolean) {
                listener(content)
            }
        }
    }

    interface SoundDialogOKClickedListener {
        fun onOKClicked(content: Boolean)
    }

    private fun setBgm() {
        if (bgmIsOn) {
            binding.soundBackground.isChecked = true
            binding.soundBackground.text = "ON"
        } else {
            binding.soundBackground.isChecked = false
            binding.soundBackground.text = "OFF"
        }
    }

    private fun setSef() {
        if (sefIsOn) {
            binding.soundEffect.isChecked = true
            binding.soundEffect.text = "ON"
        } else {
            binding.soundEffect.isChecked = false
            binding.soundEffect.text = "OFF"
        }
    }
}