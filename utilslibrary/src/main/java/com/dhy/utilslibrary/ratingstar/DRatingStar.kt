package com.dhy.utilslibrary.ratingstar

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.dhy.utilslibrary.R
import kotlin.math.round

/**
 * Created by dhy
 * Date: 2020/12/11
 * Time: 11:08
 * describe:
 */
class DRatingStar : View {
    private var mStarStep: Float = 0.1f //每次增加的步数 默认0.1(每次增加半颗星)
    private lateinit var mStarFill: Drawable //亮星星图标
    private lateinit var mStarFillBitmap: Bitmap //亮星星图标
    private lateinit var mStarEmpty: Drawable //暗星星图标
    private lateinit var mStarEmptyBitmap: Bitmap //暗星星图标
    private var mStarCount: Int = 5      //星星个数 默认5
    private lateinit var mPaint: Paint
    private var mStarTop = 0f //星星居中top值

    private var isChange = false //是否可拖动

    private lateinit var srcRect: Rect
    private lateinit var dstRect: Rect

    /**
     * 请传入0.0 ~ starCount之间的数字
     */
    var mStarScore: Float = 0f      //点亮星星个数 默认0
        set(value) {
            invalidate()
            field = if (value > mStarCount) mStarCount.toFloat() else if (value < 0) 0f else value
        }

    private var starSize: Int = 20    //星星的大小，星星默认正方形宽高一样 默认20px
    private var starDistance: Int = 0 //星星之间的间距  默认0

    constructor(context: Context, starFill: Drawable, starEmpty: Drawable) : super(context) {
        this.mStarFill = starFill
        this.mStarEmpty = starEmpty
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet) {
        val mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.RatingBar)
        starDistance =
            mTypedArray.getDimension(R.styleable.RatingBar_RatingBar_starDistance, 10f).toInt()
        mStarCount = mTypedArray.getInteger(R.styleable.RatingBar_RatingBar_starCount, 5).toInt()
        mStarEmpty = mTypedArray.getDrawable(R.styleable.RatingBar_RatingBar_starEmpty)!!
        mStarFill = mTypedArray.getDrawable(R.styleable.RatingBar_RatingBar_starFill)!!
        mStarStep = mTypedArray.getFloat(R.styleable.RatingBar_RatingBar_starStep, 0.1f)
        mStarScore = mTypedArray.getFloat(R.styleable.RatingBar_RatingBar_starScore, 0f)
        isChange = mTypedArray.getBoolean(R.styleable.RatingBar_RatingBar_starTouchable, false)
        mTypedArray.recycle()

        mPaint = Paint()
        mPaint.isAntiAlias = true        // 打开抗锯齿
        srcRect = Rect()
        dstRect = Rect()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        for (i in 0 until mStarCount) {
            //绘制暗色星星
            canvas.drawBitmap(
                mStarEmptyBitmap,
                i * starSize.toFloat() + (if (i == 0) 0 else i * starDistance),
                mStarTop, mPaint
            )
        }
        //整颗点亮的星星个数
        val starNumInt = mStarScore.toInt()
        //点亮一个星星的比例
        val percentStar = round((mStarScore - starNumInt) / mStarStep) * mStarStep
        //绘制 完整的点亮星星
        for (i in 0 until starNumInt) {
            //绘制暗色星星
            canvas.drawBitmap(
                mStarFillBitmap,
                i * starSize.toFloat() + (if (i == 0) 0 else i * starDistance),
                mStarTop, mPaint
            )
        }
        //绘制 点亮不到整颗的星星
        val left = starNumInt * starSize + starNumInt * starDistance
        val top = mStarTop.toInt()
        val right = (percentStar * starSize).toInt() + left
        val bottom = starSize + top
        srcRect.set(0, 0, (percentStar * starSize).toInt(), starSize)
        dstRect.set(left, top, right, bottom)
        canvas.drawBitmap(
            mStarFillBitmap,
            srcRect,
            dstRect,
            mPaint
        )
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (isChange) {
            var x = event.x.toInt()
            if (x < 0) x = 0
            if (x > measuredWidth)
                x = measuredWidth
            mStarScore = ((x + starDistance).toFloat() / (starSize + starDistance))
            if (x == 0) mStarScore = 0f
            invalidate()
            return true
        }
        return false
    }


    //可根据空间宽高计算出星星的高度 间距等数据
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        //这个是获取宽跟高，给下面计算星星大小用
        calculationSize(
            MeasureSpec.getSize(widthMeasureSpec),
            MeasureSpec.getSize(heightMeasureSpec)
        )
    }

    private fun calculationSize(width: Int, height: Int) {
        starSize = if (width >= (mStarCount * height + (mStarCount - 1) * starDistance)) {
            height
        } else {
            (width - (mStarCount - 1) * starDistance) / mStarCount
        }
        mStarTop = (height - starSize) / 2f
        mStarEmptyBitmap = drawableToBitmap(mStarEmpty, starSize)
        mStarFillBitmap = drawableToBitmap(mStarFill, starSize)
    }

    private fun drawableToBitmap(drawable: Drawable, size: Int): Bitmap {
        val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        drawable.bounds = Rect(0, 0, size, size)
        drawable.draw(canvas)
        return bitmap
    }
}