package com.example.dbcardealership.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dbcardealership.MainActivity
import com.example.dbcardealership.R
import com.example.dbcardealership.SetUpAPI
import com.example.dbcardealership.databinding.FragmentClientBinding
import com.example.dbcardealership.databinding.FragmentManagerBinding
import com.example.dbcardealership.user.MainDb
import com.example.dbcardealership.user.User
import com.example.dbcardealership.сar.Car
import com.example.dbcardealership.сar.CarAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext



class ManagerFragment : Fragment(), CarAdapter.Listener {

    lateinit var binding: FragmentManagerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentManagerBinding.inflate(inflater, container, false)
        var user: User

        lifecycleScope.launch(Dispatchers.IO) {
            user = MainDb.getDb(requireContext()).userDao().getAll()[0]

            withContext(Dispatchers.Main) {
                binding.textName.text = user.name
                binding.textPhone.text = user.phone

                val service = SetUpAPI()
                val response = service.getCarsByManagerPhone(user.phone)
                if(response.isSuccessful) {
                    response.body()?.let { InitRC(it) }
                }
                else {
                    binding.textWelcomeCars.text = "Вы еще не продали ни одной машины"
                }
            }
        }

        binding.toAdminButton.setOnClickListener {
            val fragment = ManagerPanelFragment()
            val transaction = fragmentManager?.beginTransaction()
            transaction?.replace(R.id.nav_container, fragment)?.commit()
        }

        binding.leaveButton.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                val db = MainDb.getDb(requireContext())
                db.userDao().delete()
                startActivity(Intent(requireContext(), MainActivity::class.java))
            }
        }

        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ManagerFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }

    fun InitRC(cars: List<Car>){
        binding.apply {
            rcView.layoutManager = LinearLayoutManager(requireContext())
            rcView.adapter = CarAdapter(this@ManagerFragment, cars)
        }
    }

    override fun onClick(car: Car) {
    }
}