package com.example.grind.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.grind.R
import com.example.grind.models.DiscoverItemClass

class DiscoverAdapter(
    private val context: Context,
    private val trainerList: ArrayList<DiscoverItemClass>
) : BaseAdapter() {

    override fun getCount(): Int = trainerList.size

    override fun getItem(position: Int): Any = trainerList[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_trainer, parent, false)

        val trainer = trainerList[position]
        val image: ImageView = view.findViewById(R.id.trainerImage)
        val name: TextView = view.findViewById(R.id.trainerName)
        val msg: TextView = view.findViewById(R.id.trainerMsg)
        val time: TextView = view.findViewById(R.id.trainerTime)

        image.setImageResource(trainer.image)
        name.text = trainer.name
        msg.text = trainer.msg
        time.text = trainer.time

        return view
    }
}
