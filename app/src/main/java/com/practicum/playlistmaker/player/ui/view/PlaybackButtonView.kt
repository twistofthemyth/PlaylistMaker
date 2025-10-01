package com.practicum.playlistmaker.player.ui.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.RectF
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.annotation.AttrRes
import androidx.annotation.StyleRes
import com.practicum.playlistmaker.R

class PlaybackButtonView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = 0,
    @StyleRes defStyleRes: Int = 0
) : View(context, attrs, defStyleAttr, defStyleRes) {
    private var stopDrawable: Drawable?
    private var startDrawable: Drawable?
    private val size: Int
    private var isPlaying = false
    private var imageRect = RectF(0f, 0f, 0f, 0f)
    private var listener: PlaybackButtonListener? = null

    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.PlaybackButtonView,
            defStyleAttr,
            defStyleRes
        ).apply {
            try {
                size = getDimensionPixelSize(R.styleable.PlaybackButtonView_size, 0)
                stopDrawable = getDrawable(R.styleable.PlaybackButtonView_stop_icon_ref)
                startDrawable = getDrawable(R.styleable.PlaybackButtonView_play_icon_ref)
            } finally {
                recycle()
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(size, size)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        imageRect = RectF(0f, 0f, measuredWidth.toFloat(), measuredHeight.toFloat())
    }

    override fun onDraw(canvas: Canvas) {
        if (isPlaying) {
            stopDrawable?.setBounds(imageRect.left.toInt(), imageRect.top.toInt(), imageRect.right.toInt(), imageRect.bottom.toInt())
            stopDrawable?.draw(canvas)
        } else {
            startDrawable?.setBounds(imageRect.left.toInt(), imageRect.top.toInt(), imageRect.right.toInt(), imageRect.bottom.toInt())
            startDrawable?.draw(canvas)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                return true
            }

            MotionEvent.ACTION_UP -> {
                toggle()
                return true
            }
        }
        return super.onTouchEvent(event)
    }

    fun play() {
        if (!isPlaying) {
            isPlaying = true
            listener?.onStart()
            invalidate()
        }
    }

    fun stop() {
        if (isPlaying) {
            isPlaying = false
            listener?.onStop()
            invalidate()
        }
    }

    fun toggle() {
        if (isPlaying) stop() else play()
    }

    fun setPlaybackButtonListener(listener: PlaybackButtonListener) {
        this.listener = listener
    }

    fun release() {
        listener = null
        stopDrawable = null
        startDrawable = null
    }
}