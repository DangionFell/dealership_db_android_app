package com.example.dbcardealership.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.dbcardealership.*
import com.example.dbcardealership.contract.Contract
import com.example.dbcardealership.databinding.FragmentAddNewCarBinding
import com.example.dbcardealership.databinding.FragmentManagerPanelBinding
import com.example.dbcardealership.сar.CarCreateModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddNewCarFragment : Fragment() {

    lateinit var binding: FragmentAddNewCarBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentAddNewCarBinding.inflate(inflater, container, false)

        var address: String = ""
        var addresses = listOf<String>()

        lifecycleScope.launch(Dispatchers.Main) {
            val addresses = getShowroomsAddress()
            val adapter = ArrayAdapter<String>(requireContext(), R.layout.list_item, addresses)
            binding.autoCompleteTxt.setAdapter(adapter)
        }

        binding.autoCompleteTxt.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                address = parent.getItemAtPosition(position).toString()
            }

        binding.createCarButton.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                val model = binding.editTextModel.text.toString()
                val config = binding.editTextConfig.text.toString()
                val year = binding.editTextYear.text.toString()
                val power = binding.editTextHorsepower.text.toString().toInt()
                val price = binding.editTextPrice.text.toString().toInt()
                val color = binding.editTextColor.text.toString()
                val state = "в наличии"
                val showroomId = getShowroomIdByAddress(address)

                val service = SetUpAPI()

                val response = service.createCar(CarCreateModel(
                    model, config, year, power, price, color, state, showroomId))
                if (response.isSuccessful){
                    withContext(Dispatchers.Main){
                        Toast.makeText(
                            requireContext(),
                            "Машина с уникальным номером ${response.body()} добавлена",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
                else {
                    withContext(Dispatchers.Main){
                        Toast.makeText(
                            requireContext(),
                            "Не удалось добавить машину",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }

        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AddNewCarFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}