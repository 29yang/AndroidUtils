package com.dhy.utilslibrary.dialog

import android.app.Dialog
import android.content.Context
import android.os.CountDownTimer
import android.text.TextUtils
import android.view.Display
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.view.animation.LinearInterpolator
import android.widget.*
import androidx.core.content.ContextCompat
import com.dhy.utilslibrary.R
import com.dhy.utilslibrary.dptool.DpTools


/***
 * 弹框提示
 */
class AlertDialog(private val context: Context) {
    private var dialog: Dialog? = null
    private var lLayout_bg: LinearLayout? = null
    private var txt_title: TextView? = null
    private var txt_msg: TextView? = null
    private var btn_neg: Button? = null
    private var btn_pos: Button? = null
    private var img_line: ImageView? = null
    private var txt_iv: ImageView? = null
    private val display: Display
    private var showTitle = false
    private var showMsg = false
    private var showPosBtn = false
    private var showNegBtn = false
    fun builder(): AlertDialog {
        val view = LayoutInflater.from(context).inflate(
            R.layout.u_view_alert_dialog, null
        )
        lLayout_bg = view.findViewById<View>(R.id.lLayout_bg) as LinearLayout
        txt_title = view.findViewById<View>(R.id.txt_title) as TextView
        txt_msg = view.findViewById<View>(R.id.txt_msg) as TextView
        btn_neg = view.findViewById<View>(R.id.btn_neg) as Button
        btn_pos = view.findViewById<View>(R.id.btn_pos) as Button
        img_line = view.findViewById<View>(R.id.img_line) as ImageView
        txt_iv = view.findViewById<View>(R.id.txt_iv) as ImageView
        setGone()
        dialog = Dialog(context, R.style.AlertDialogStyle)
        dialog!!.setContentView(view)
        return this
    }

    /**
     * 恢复初始
     * @return
     */
    fun setGone(): AlertDialog {
        if (lLayout_bg != null) {
            txt_title!!.visibility = View.GONE
            txt_msg!!.visibility = View.GONE
            btn_neg!!.visibility = View.GONE
            btn_pos!!.visibility = View.GONE
            img_line!!.visibility = View.GONE
        }
        showTitle = false
        showMsg = false
        showPosBtn = false
        showNegBtn = false
        return this
    }

    /**
     * 设置title
     * @param title
     * @return
     */
    fun setTitle(title: String?): AlertDialog {
        showTitle = true
        if (TextUtils.isEmpty(title)) {
            txt_title!!.text = "提示"
        } else {
            txt_title!!.text = title
        }
        return this
    }

    /**
     * 设置Message
     * @param msg
     * @return
     */
    fun setMsg(msg: String?): AlertDialog {
        showMsg = true
        if (TextUtils.isEmpty(msg)) {
            txt_msg!!.text = ""
        } else {
            txt_msg!!.text = msg
        }
        return this
    }

    fun setImage(int: Int): AlertDialog {
        txt_iv!!.setImageResource(int)
        return this
    }

    fun setAnim(isAnim: Boolean): AlertDialog {
        if (isAnim) {
            val loadAnimation = AnimationUtils.loadAnimation(context, R.anim.load_rotate)
            loadAnimation.interpolator = LinearInterpolator()
            txt_iv?.startAnimation(loadAnimation)
        }
        return this
    }

    /**
     * 设置点击外部是否消失
     * @param cancel
     * @return
     */
    fun setCancelable(cancel: Boolean): AlertDialog {
        dialog!!.setCancelable(cancel)
        return this
    }

    /**
     * 右侧按钮
     *
     * @param text
     * @param listener
     * @return
     */
    fun setPositiveButton(
        text: String,
        listener: View.OnClickListener?
    ): AlertDialog {
        return setPositiveButton(text, -1, listener)
    }

    /**
     * 右侧按钮 点击不取消弹窗
     *
     * @param text
     * @param listener
     * @return
     */
    fun setPositiveButtonIsShow(
        text: String,
        listener: View.OnClickListener?
    ): AlertDialog {
        return setPositiveButtonIsShow(text, -1, listener)
    }

    private fun setPositiveButton(
        text: String, color: Int,
        listener: View.OnClickListener?
    ): AlertDialog {
        var color = color
        showPosBtn = true
        if ("" == text) {
            btn_pos!!.text = ""
        } else {
            btn_pos!!.text = text
        }
        if (color == -1) {
            color = R.color.parking_suc
        }
        btn_pos!!.setTextColor(ContextCompat.getColor(context, color))
        btn_pos!!.setOnClickListener { v ->
            listener?.onClick(v)
            dismiss()
        }
        return this
    }

    private fun setPositiveButtonIsShow(
        text: String, color: Int,
        listener: View.OnClickListener?
    ): AlertDialog {
        var color = color
        showPosBtn = true
        if ("" == text) {
            btn_pos!!.text = ""
        } else {
            btn_pos!!.text = text
        }
        if (color == -1) {
            color = R.color.parking_suc
        }
        btn_pos!!.setTextColor(ContextCompat.getColor(context, color))
        btn_pos!!.setOnClickListener { v ->
            listener?.onClick(v)
//            dismiss()
        }
        return this
    }

    /**
     * 右侧按钮显示倒计时
     * @param timeLength 时间长度单位秒  例 4000 倒计时为 3s 2s 1s 0s
     */
    fun setPositionTimeCountDown(timeLength: Long, listener: View.OnClickListener?): AlertDialog {
        var text = String.format(context.resources.getString(R.string.done), timeLength / 1000 - 1)
        var color = -1
        showPosBtn = true
        if ("" == text) {
            btn_pos!!.text = ""
        } else {
            btn_pos!!.text = text
        }
        if (color == -1) {
            color = R.color.parking_suc
        }
        var timer: CountDownTimer? = object : CountDownTimer(timeLength, 1000) {
            override fun onTick(sin: Long) {
                text =
                    String.format(context.resources.getString(R.string.done), sin / 1000)
                btn_pos!!.text = text
            }

            override fun onFinish() {
                listener?.onClick(btn_pos)
                dismiss()
            }
        }
        timer?.start()
        btn_pos!!.setTextColor(ContextCompat.getColor(context, color))
        btn_pos!!.setOnClickListener { v ->
            timer?.cancel()
            timer = null
            listener?.onClick(v)
            dismiss()
        }
        return this
    }

    /**
     * 左侧按钮
     *
     * @param text
     * @param listener
     * @return
     */
    fun setNegativeButton(
        text: String,
        listener: View.OnClickListener?
    ): AlertDialog {
        return setNegativeButton(text, -1, listener)
    }

    private fun setNegativeButton(
        text: String, color: Int,
        listener: View.OnClickListener?
    ): AlertDialog {
        var color = color
        showNegBtn = true
        if ("" == text) {
            btn_neg!!.text = ""
        } else {
            btn_neg!!.text = text
        }
        if (color == -1) {
            color = R.color.black_9
        }
        btn_neg!!.setTextColor(ContextCompat.getColor(context, color))
        btn_neg!!.setOnClickListener { v ->
            listener?.onClick(v)
            dismiss()
        }
        return this
    }

    /**
     * 设置显示
     */
    private fun setLayout() {
        if (!showTitle && !showMsg) {
            txt_title!!.text = ""
            txt_title!!.visibility = View.VISIBLE
        }
        if (showTitle) {
            txt_title!!.visibility = View.VISIBLE
        }
        if (showMsg) {
            txt_msg!!.visibility = View.VISIBLE
        }
        if (!showPosBtn && !showNegBtn) {
            btn_pos!!.text = ""
            btn_pos!!.visibility = View.VISIBLE
            btn_pos!!.setBackgroundResource(R.drawable.u_alert_dialog_selector)
            btn_pos!!.setOnClickListener { dismiss() }
        }
        if (showPosBtn && showNegBtn) {
            btn_pos!!.visibility = View.VISIBLE
            btn_pos!!.setBackgroundResource(R.drawable.u_alert_dialog_right_selector)
            btn_neg!!.visibility = View.VISIBLE
            btn_neg!!.setBackgroundResource(R.drawable.u_alert_dialog_left_selector)
            img_line!!.visibility = View.VISIBLE
        }
        if (showPosBtn && !showNegBtn) {
            btn_pos!!.visibility = View.VISIBLE
            btn_pos!!.setBackgroundResource(R.drawable.u_alert_dialog_selector)
        }
        if (!showPosBtn && showNegBtn) {
            btn_neg!!.visibility = View.VISIBLE
            btn_neg!!.setBackgroundResource(R.drawable.u_alert_dialog_selector)
        }
    }

    fun show() {
        setLayout()
        setCancelable(false)
        val window = dialog?.window
        val lp = window!!.attributes
        lp.width = DpTools.dp2px(context,400f)
        lp.height = LinearLayout.LayoutParams.WRAP_CONTENT
        window.attributes = lp
        dialog!!.show()
    }

    val isShowing: Boolean
        get() = if (dialog != null) {
            dialog!!.isShowing
        } else false

    fun dismiss() {
        if (dialog != null) {
            dialog!!.dismiss()
        }
    }

    init {
        val windowManager = context
            .getSystemService(Context.WINDOW_SERVICE) as WindowManager
        display = windowManager.defaultDisplay
    }
}