package com.allen.shapi.task.task2

import com.allen.shapi.R
import com.allen.shapi.base.BaseA
import kotlinx.android.synthetic.main.toolbar.*
class TaskTwoActivity: BaseA() {

    override fun attachLayoutRes(): Int = R.layout.activity_two_task

    override fun initView() {
        initToolbar(toolbar = toolbar, homeAsUpEnabled = true, title = "采集和播放音频")
    }






}

