package com.allen.shapi.task.task2

import android.content.Context

class PlayFactory(context: Context) {
    private var context: Context? = null

    init {
        this.context = context
    }

    companion object {
        const val STREAMMODE = 1
        const val STATICMODE = 2
    }



}