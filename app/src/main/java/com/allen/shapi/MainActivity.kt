package com.allen.shapi

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.allen.shapi.adapter.HomeAdapter
import com.allen.shapi.constant.Constant
import com.allen.shapi.extension.OnItemClickListener
import com.allen.shapi.extension.addOnItemClickListener
import com.allen.shapi.task.task2.TaskTwoActivity
import com.allen.shapi.widget.SpaceItemDecoration
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val data = Constant.taskNames

    private val linearLayoutManager: LinearLayoutManager by lazy {
        LinearLayoutManager(this)
    }

    private val recyclerViewItemDecoration by lazy {
        this.let {
            SpaceItemDecoration(it)
        }
    }

    private val homeAdapter : HomeAdapter by lazy {
        HomeAdapter(this, data)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
    }

    private fun initView() {
        recyclerView.run {
            layoutManager = linearLayoutManager
            adapter = homeAdapter
            itemAnimator = DefaultItemAnimator()
            addItemDecoration(recyclerViewItemDecoration)
            addOnItemClickListener(object : OnItemClickListener{
                override fun onItemClick(position: Int, view: View) {
                    val intent = Intent(applicationContext, TaskTwoActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    applicationContext.startActivity(intent)
                }
            })
        }
    }
}