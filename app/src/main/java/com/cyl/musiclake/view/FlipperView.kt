package com.cyl.musiclake.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.View
import com.cyl.musiclake.R


/**
 * Created by cyl on 2018/7/26.
 * 向外扩散View，中间白色圆圈不变。四周扩散
 * 1、绘制中间圆圈
 * 2、绘制文字
 * 3、绘制渐变扩散圆圈
 */
class FlipperView : View {

    /**
     * 初始化画笔
     */
    private val mCirclePaint = Paint()

    private var centerX: Float = 0.0f
    private var centerY: Float = 0.0f
    private var radius: Float = 0.0f
    private var circleColor: Int = Color.RED
    private var circleColors = mutableListOf("#e51c23",
            "#e91e63",
            "#9c27b0",
            "#673ab7",
            "#3f51b5",
            "#5677fc",
            "#03a9f4",
            "#00bcd4",
            "#009688",
            "#259b24",
            "#8bc34a",
            "#cddc39",
            "#ffed3b",
            "#ffc107",
            "#ff9800",
            "#ff5722")

    //是否开启动画
    private var isStartAnim = true
    //alphas
    private var widths = mutableListOf<Int>()
    //向外扩散透明度
    private var alphas = mutableListOf<Int>()
    private var colors = mutableListOf<Int>()

    constructor(context: Context) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs) {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.FlipperView, 0, 0)
        radius = a.getDimension(R.styleable.FlipperView_fv_radius, context.resources.getDimension(R.dimen.dp_56))
        circleColor = a.getColor(R.styleable.FlipperView_fv_color, Color.RED)
        a.recycle()

        widths.add(0)
        alphas.add(200)
        colors.add(circleColor)
        /**
         * 圆弧画笔
         */
        mCirclePaint.isAntiAlias = true
        mCirclePaint.color = circleColor
        mCirclePaint.style = Paint.Style.FILL
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        //绘制渐变的圆圈
        for (i in 0 until widths.size) {
            mCirclePaint.color = colors[i]
            mCirclePaint.alpha = alphas[i]

            canvas?.drawCircle(centerX, centerY, radius + widths[i].toFloat(), mCirclePaint)

            Log.e("draw ", "--" + widths[i].toFloat() + "--" + i)

            if (widths[i] < centerX - radius) {
                alphas[i] = (200 - (200f / (centerX - radius)) * widths[i]).toInt()
                widths[i] = widths[i] + 1
            }

        }
        Log.e("draw widths", "--" + widths[widths.size - 1].toFloat() + "--" + (centerX - radius).toInt())
        if (widths[widths.size - 1] == (centerX - radius).toInt() / 6) {
            widths.add(0)
            alphas.add(200)
            colors.add(getColorInt())
        }

        if (widths.size >= 30) {
            widths.removeAt(0)
            alphas.removeAt(0)
            colors.removeAt(0)
        }

        //重绘
        if (isStartAnim) {
            invalidate()
        }
    }

    fun setOnClick() {
        widths.add(0)
        alphas.add(200)
        colors.add(getColorInt())
        invalidate()
    }

    var position = 0
    fun getColorInt(): Int {
        position = (position + 1) % (circleColors.size)
        return Color.parseColor(circleColors[position])
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val myWidthSpecSize = View.MeasureSpec.getSize(widthMeasureSpec)
        val myHeightSpecSize = View.MeasureSpec.getSize(heightMeasureSpec)

        // 获取宽
        val mWidth = if (myHeightSpecSize > myWidthSpecSize) {
            myWidthSpecSize
        } else {
            myHeightSpecSize
        }

        // 设置该view的宽高
        setMeasuredDimension(mWidth, mWidth)

        centerY = (mWidth / 2).toFloat()
        centerX = (mWidth / 2).toFloat()
    }

}