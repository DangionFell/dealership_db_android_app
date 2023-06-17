package com.example.dbcardealership.showroom

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.dbcardealership.R
import com.example.dbcardealership.databinding.ShowroomItemBinding

class ShowroomAdapter(val listener : Listener, private val showroomList : List<Showroom>): RecyclerView.Adapter<ShowroomAdapter.ShowroomHolder>() {

    class ShowroomHolder(item: View): RecyclerView.ViewHolder(item) {
        val binding = ShowroomItemBinding.bind(item)

        fun bind(showroom: Showroom, listener: Listener) = with(binding) {
            name.text = showroom.name
            address.text = showroom.address
            phone.text = showroom.phone

            itemView.setOnClickListener{
                listener.onClick(showroom)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShowroomHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.showroom_item, parent, false)
        return ShowroomHolder(view)
    }

    override fun onBindViewHolder(holder: ShowroomHolder, position: Int) {
        holder.bind(showroomList[position], listener)
    }

    override fun getItemCount(): Int {
        return showroomList.size
    }

    interface Listener{
        fun onClick(showroom: Showroom)
    }
}