package com.freitasdev.citify.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.freitasdev.citify.R
import androidx.recyclerview.widget.RecyclerView
import com.freitasdev.citify.model.entities.CityEntity

class CityAdapter(private val context: Context, private val cities: List<CityEntity>) : RecyclerView.Adapter<CityAdapter.CityViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityViewHolder {
        val itemList = LayoutInflater.from(context).inflate(R.layout.city, parent, false)
        val holder = CityViewHolder(itemList)

        return holder
    }

    override fun getItemCount(): Int = cities.size

    override fun onBindViewHolder(holder: CityViewHolder, position: Int) {
        holder.name.text = cities[position].name
        holder.state.text = cities[position].uf
        holder.region.text = cities[position].region
    }

    inner class CityViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name = itemView.findViewById<TextView>(R.id.city)
        val state = itemView.findViewById<TextView>(R.id.uf)
        val region = itemView.findViewById<TextView>(R.id.region)
    }
}