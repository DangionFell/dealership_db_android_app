package com.example.dbcardealership

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dbcardealership.сar.Car
import com.example.dbcardealership.сar.CarAdapter
import com.example.dbcardealership.databinding.ActivityShowroomBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ShowroomActivity : AppCompatActivity(), CarAdapter.Listener {
    lateinit var binding: ActivityShowroomBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShowroomBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lifecycleScope.launch(Dispatchers.IO) {
            val id = intent.getIntExtra("showroom_id", 1)
            val cars = getCarsInStockByShowroomId(id)
            if (cars.isNotEmpty()) {
                withContext(Dispatchers.Main) {
                    InitRC(cars)
                }
            }
        }
    }

    fun InitRC(cars: List<Car>){
        binding.apply {
            rcView.layoutManager = LinearLayoutManager(this@ShowroomActivity)
            rcView.adapter = CarAdapter(this@ShowroomActivity, cars)
        }
    }

    override fun onClick(car: Car) {
        startActivity(Intent(this, CarDetailActivity::class.java).apply {
            putExtra("car_id", car.id)
        })
    }
}