package com.gb.lesson2.ui.main.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.gb.lesson2.R
import com.gb.lesson2.ui.main.model.Weather

class MainAdapter : RecyclerView.Adapter<MainAdapter.MainViewHolder>() {

    var weatherData: List<Weather> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var listener: OnItemViewClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder =
        MainViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.main_fragment_item, parent, false)
        )

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder.bind(weatherData[position])
    }

    override fun getItemCount(): Int = weatherData.size

    inner class MainViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(weather: Weather) {
            itemView.findViewById<TextView>(R.id.cityName).text = weather.city.name
            itemView.setOnClickListener {
                listener?.onItemClick(weather)
            }
        }
    }

    fun interface OnItemViewClickListener {
        fun onItemClick(weather: Weather)
    }
}