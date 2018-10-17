package com.allen.shapi.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.allen.shapi.R

class HomeAdapter(private val context: Context?, datas: ArrayList<String>?) : RecyclerView.Adapter<HomeAdapter.Companion.ViewHolder>() {
    private var datas = datas
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_home_list, parent,false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
       return datas?.size ?: 0
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.i("HomeAdapter", "onBindViewHolder")
        holder.mTv?.text = datas!![position]
    }

    companion object {
        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            var mTv: TextView? = null
            var ll: LinearLayout? = null
            init {
                mTv = itemView.findViewById(R.id.tv_task)
                ll = itemView.findViewById(R.id.ll)
            }
        }
    }
}
