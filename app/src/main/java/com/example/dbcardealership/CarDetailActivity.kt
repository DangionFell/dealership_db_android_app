package com.example.dbcardealership

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.example.dbcardealership.Imageapi.getCarUrl
import com.example.dbcardealership.databinding.ActivityCarDetailBinding
import com.example.dbcardealership.databinding.ActivityMainBinding
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CarDetailActivity : AppCompatActivity() {

    lateinit var binding: ActivityCarDetailBinding

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCarDetailBinding.inflate(layoutInflater)

        val id = intent.getIntExtra("car_id", 1)

        var showroom_id = 0

        lifecycleScope.launch(Dispatchers.IO){
            val service = SetUpAPI()
            val car = service.getCarById(id)
            showroom_id = car.showroomId
            if(car.state == "в наличии") {
                withContext(Dispatchers.Main) {
                    binding.buyButton.visibility = View.VISIBLE
                }
            }
            val carUrl = getCarUrl(car.model)

            withContext(Dispatchers.Main) {
                Picasso.get().load(carUrl).into(binding.image)
                binding.apply {
                    model.text = car.model
                    config.text = "Комплектация: " + car.config
                    yearOfManufacture.text = "Год выпуска: " + car.yearOfManufacture
                    horsepower.text = "Лошадиных сил: " + car.horsepower.toString()
                    color.text = "Цвет: " + car.color
                    price.text = "Цена " + car.price.toString()
                }
            }
        }

        binding.buyButton.setOnClickListener {
            startActivity(Intent(this, ContractActivity::class.java).apply {
                putExtra("car_id", id)
                putExtra("showroom_id", showroom_id)
            })
        }

        setContentView(binding.root)
    }
}