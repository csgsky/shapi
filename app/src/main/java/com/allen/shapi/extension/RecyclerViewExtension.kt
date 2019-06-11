package com.allen.shapi.extension

import android.support.v7.widget.RecyclerView
import android.view.View

interface OnItemClickListener {
    fun onItemClick(position: Int, view: View)
}


fun RecyclerView.addOnItemClickListener(onClickListener: OnItemClickListener) {
    this.addOnChildAttachStateChangeListener(object : RecyclerView.OnChildAttachStateChangeListener {
        override fun onChildViewDetachedFromWindow(view: View) {
            view.setOnClickListener(null)
        }

        override fun onChildViewAttachedToWindow(view: View) {
            view.setOnClickListener {
                val holder = getChildViewHolder(view)
                onClickListener.onItemClick(holder.adapterPosition, view)
            }
        }
    })
}