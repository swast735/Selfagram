package com.example.selfagram

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class MyAdapter(private val pics_list:ArrayList<Pic>, val context: Context):RecyclerView.Adapter<MyAdapter.MyViewHolder>(){
    interface Recclicklist {
        fun onPicClick(pos:Int)
    }
    private lateinit var reclick:Recclicklist
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val picView=LayoutInflater.from(parent.context).inflate(R.layout.pics,parent,false)
        return  MyViewHolder(picView,reclick)
    }
    fun setOnPicClickListener(listener: Recclicklist)
    {
        reclick=listener
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        Glide.with(context).load(pics_list[position].Pic).into(holder.pics)
        }
    override fun getItemCount(): Int {
        return pics_list.size
    }
    class MyViewHolder(picview:View, listener: Recclicklist):RecyclerView.ViewHolder(picview){
        val pics: ImageView=picview.findViewById(R.id.sp)
        init {
            picview.setOnClickListener{
                listener.onPicClick(adapterPosition)
            }
        }
    }
}