package com.allen.shapi.task.task1

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.SurfaceHolder
import android.view.View
import com.allen.shapi.R
import com.allen.shapi.base.BaseA
import kotlinx.android.synthetic.main.activity_task_one.*
import kotlinx.android.synthetic.main.toolbar.*

/**
 * task 1: 设置图片的三种方式
 * 1. 使用 ImageView
 * 2. 使用 SurfaceView
 * 3. 自定义 view
 */
class TaskOneActivity : BaseA() {
    override fun attachLayoutRes(): Int = R.layout.activity_task_one

    override fun initView() {
        initToolbar(toolbar = toolbar, homeAsUpEnabled = true, title = "设置图片的三种方式")
        // 1. 方式一
        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.csg)
        imageView.setImageBitmap(bitmap)

        // 2. 方式二
        surfaceView.holder.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceCreated(surfaceHolder: SurfaceHolder?) {
                if (surfaceHolder == null)
                    return
                val paint = Paint()
                paint.isAntiAlias = true
                paint.style = Paint.Style.STROKE

                val bm = BitmapFactory.decodeResource(resources, R.drawable.csg)
                val canvas = surfaceHolder.lockCanvas()
                canvas.drawBitmap(bm, 0f, 0f, paint)
                surfaceHolder.unlockCanvasAndPost(canvas)

            }

            override fun surfaceChanged(surfaceHolder: SurfaceHolder?, p1: Int, p2: Int, p3: Int) {

            }

            override fun surfaceDestroyed(surfaceHolder: SurfaceHolder?) {

            }
        })
    }
}

class CustomImageView(context: Context, attributeSet: AttributeSet) : View(context, attributeSet) {
    private val paint = Paint()
    private var bitmap: Bitmap? = null

    init {
        paint.isAntiAlias = true
        paint.style = Paint.Style.STROKE
        bitmap = BitmapFactory.decodeResource(resources, R.drawable.csg)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (bitmap != null) {
            canvas?.drawBitmap(bitmap, 0f, 0f, paint)
        }
    }
}


