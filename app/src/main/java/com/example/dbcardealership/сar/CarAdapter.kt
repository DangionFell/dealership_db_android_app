package com.example.dbcardealership.сar

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.dbcardealership.R
import com.example.dbcardealership.databinding.CarItemBinding

class CarAdapter(val listener : Listener, private val carList : List<Car>): RecyclerView.Adapter<CarAdapter.CarHolder>() {

    class CarHolder(item: View): RecyclerView.ViewHolder(item) {
        val binding = CarItemBinding.bind(item)

        fun bind(car: Car, listener: Listener) = with(binding) {
            model.text = car.model
            config.text = car.config
            price.text = car.price.toString()

            itemView.setOnClickListener{
                listener.onClick(car)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.car_item, parent, false)
        return CarHolder(view)
    }

    override fun onBindViewHolder(holder: CarHolder, position: Int) {
        holder.bind(carList[position], listener)
    }

    override fun getItemCount(): Int {
        return carList.size
    }

    interface Listener{
        fun onClick(car: Car)
    }
}