package com.example.dbcardealership.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.dbcardealership.CarDetailActivity
import com.example.dbcardealership.MainActivity
import com.example.dbcardealership.SetUpAPI
import com.example.dbcardealership.contract.Contract
import com.example.dbcardealership.databinding.FragmentContractManagerBinding
import com.example.dbcardealership.user.MainDb
import com.example.dbcardealership.user.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val ARG_CAR_ID = "car_id"
private const val ARG_SHOWROOM_ID = "showroom_id"

class ContractManagerFragment : Fragment() {

    private var car_id: Int? = null
    private var showroom_id: Int? = null

    lateinit var binding: FragmentContractManagerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            car_id = it.getInt(ARG_CAR_ID)
            showroom_id = it.getInt(ARG_SHOWROOM_ID)
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentContractManagerBinding.inflate(inflater, container, false)

        var user = User(1, "", "", "")

        lifecycleScope.launch(Dispatchers.IO) {
            val db = MainDb.getDb(requireContext())
            user = db.userDao().getAll()[0]
            withContext(Dispatchers.Main){
                binding.apply {
                    carId.text = carId.text.toString() + car_id.toString()
                    managerId.text = managerId.text.toString() + user.id.toString()
                    showroomId.text = showroomId.text.toString() + showroom_id.toString()
                }
            }
        }

        binding.contractButton.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                val phone = binding.editTextPhone.text.toString()
                val service = SetUpAPI()
                val date = binding.editTextDate.text.toString()
                val client_id = service.getClientIdByPhone(phone)
                if(client_id.isSuccessful){
                    val response = service.sendContract(Contract(date, showroom_id!!, car_id!!, client_id.body()!!, user.id))
                    if (response.isSuccessful){
                        withContext(Dispatchers.Main){
                            Toast.makeText(
                                requireContext(),
                                "Контракт с номером ${response.body()} заключен",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                        startActivity(Intent(requireContext(), MainActivity::class.java))
                    }
                    else {
                        withContext(Dispatchers.Main){
                            Toast.makeText(
                                requireContext(),
                                "Не удалось заключить контракт",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }
                else{
                    withContext(Dispatchers.Main){
                        Toast.makeText(
                            requireContext(),
                            "Проверьте правильность введенных данных",
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
        fun newInstance(car_id: Int, showroom_id: Int) =
            ContractManagerFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_CAR_ID, car_id)
                    putInt(ARG_SHOWROOM_ID, showroom_id)
                }
            }
    }
}