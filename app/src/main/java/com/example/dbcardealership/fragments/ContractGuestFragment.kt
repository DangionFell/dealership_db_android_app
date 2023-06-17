package com.example.dbcardealership.fragments

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
import com.example.dbcardealership.R
import com.example.dbcardealership.SetUpAPI
import com.example.dbcardealership.client.ClientRegisterModel
import com.example.dbcardealership.databinding.FragmentContractGuestBinding
import com.example.dbcardealership.user.MainDb
import com.example.dbcardealership.user.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ContractGuestFragment : Fragment() {

    lateinit var binding: FragmentContractGuestBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentContractGuestBinding.inflate(inflater, container, false)

        binding.regButton.setOnClickListener{
            lifecycleScope.launch(Dispatchers.IO) {
                val name = binding.editTextName.text.toString()
                val phone = binding.editTextPhone.text.toString()
                val password = binding.editTextTextPassword.text.toString()
                val servise = SetUpAPI()

                val clientResponse = servise.registerClient(ClientRegisterModel(name, phone, password))
                if(clientResponse.isSuccessful){
                    val db = MainDb.getDb(requireContext())
                    db.userDao().insert(User(1, name, phone, "client"))
                    startActivity(Intent(requireContext(), MainActivity::class.java))
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            requireContext(),
                            "Ваша заявка создана, обратитесь к менеджеру",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        requireContext(),
                        "Такой пользователь уже существует или вы ввели неверные данные",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

        }

        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ContractGuestFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}