package com.allen.shapi.task.task1

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView

class SurfaceViewHandWriting : SurfaceView, SurfaceHolder.Callback {
    private var surfaceHolder: SurfaceHolder = holder
    private var paint: Paint = Paint()
    private var path: Path = Path()
    private var canvas: Canvas? = null
    constructor(context: Context):
            this(context, null)

    constructor(context: Context, attributeSet: AttributeSet?):
            this(context, attributeSet, 0)

    constructor(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int):
            super(context, attributeSet, defStyleAttr) {
        initPaint()
        initPath()
        initView()
    }

    override fun surfaceChanged(p0: SurfaceHolder?, p1: Int, p2: Int, p3: Int) {
    }

    override fun surfaceDestroyed(p0: SurfaceHolder?) {
    }

    override fun surfaceCreated(p0: SurfaceHolder?) {
        drawSomething()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val x = event?.x
        val y = event?.y
        when(event?.action) {
            MotionEvent.ACTION_DOWN -> {
                path.moveTo(x!!, y!!)
                drawSomething()
            }
            MotionEvent.ACTION_MOVE -> {
                path.lineTo(x!!, y!!)
                drawSomething()
            }
            MotionEvent.ACTION_UP -> {

            }
        }
        return true
    }

    private fun drawSomething() {
        try {
            canvas = surfaceHolder.lockCanvas()
            canvas?.drawColor(Color.GRAY)
            canvas?.drawPath(path, paint)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (canvas != null) {
                surfaceHolder.unlockCanvasAndPost(canvas)
            }
        }
    }


    private fun initPaint() {
        paint.isAntiAlias = true
        paint.strokeWidth = 5f
        paint.color = Color.RED
        paint.style = Paint.Style.STROKE
    }

    private fun initPath() {
        path.moveTo(0f, 300f)
    }

    private fun initView() {
        surfaceHolder.addCallback(this)
        isFocusable = true
        keepScreenOn = true
        isFocusableInTouchMode = true
    }
}