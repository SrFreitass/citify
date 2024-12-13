package com.freitasdev.citify.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.util.Function
import com.freitasdev.citify.R
import androidx.recyclerview.widget.RecyclerView
import com.freitasdev.citify.model.entities.CityEntity

class CityAdapter(
    private val context: Context,
    private var cities: MutableList<CityEntity>,
    private val deleteAction: (Int) -> Unit,
    private val updateAction: (Int) -> Unit
) : RecyclerView.Adapter<CityAdapter.CityViewHolder>() {

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

        holder.deleteButton.setOnClickListener {
            deleteAction(cities[position].id)
        }

        holder.updateButton.setOnClickListener {
            updateAction(cities[position].id)
        }
    }


    inner class CityViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name = itemView.findViewById<TextView>(R.id.city)
        val state = itemView.findViewById<TextView>(R.id.uf)
        val region = itemView.findViewById<TextView>(R.id.region)
        val deleteButton = itemView.findViewById<Button>(R.id.delete_btn)
        val updateButton = itemView.findViewById<Button>(R.id.update_btn)
    }
}