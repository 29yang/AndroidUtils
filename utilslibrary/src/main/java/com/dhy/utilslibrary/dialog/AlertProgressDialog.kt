package com.dhy.utilslibrary.dialog

import android.app.Dialog
import android.content.Context
import android.view.Display
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.view.animation.LinearInterpolator
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import com.dhy.utilslibrary.R
import com.dhy.utilslibrary.dptool.DpTools


/***
 * 弹框提示
 */
class AlertProgressDialog(private val context: Context) {
    private var dialog: Dialog? = null
    private var lLayout_bg: LinearLayout? = null
    private var txt_iv: ImageView? = null
    private val display: Display
    fun builder(): AlertProgressDialog {
        val view = LayoutInflater.from(context).inflate(
            R.layout.u_view_alert_progress_dialog, null
        )
        lLayout_bg = view.findViewById<View>(R.id.lLayout_bg) as LinearLayout
        txt_iv = view.findViewById<View>(R.id.txt_iv) as ImageView
        dialog = Dialog(context, R.style.AlertDialogStyle)
        dialog!!.setContentView(view)
        lLayout_bg!!.layoutParams = FrameLayout.LayoutParams(
            DpTools.dp2px(context, 280f), DpTools.dp2px(context, 200f)
        )
        return this
    }

    fun setImage(int: Int): AlertProgressDialog {
        txt_iv!!.setImageResource(int)
        return this
    }

    fun setAnim(isAnim: Boolean): AlertProgressDialog {
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
    fun setCancelable(cancel: Boolean): AlertProgressDialog {
        dialog!!.setCancelable(cancel)
        return this
    }

    fun show() {
        setCancelable(false)
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