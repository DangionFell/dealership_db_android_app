package com.example.dbcardealership.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.dbcardealership.R
import com.example.dbcardealership.SetUpAPI
import com.example.dbcardealership.client.ClientRegisterModel
import com.example.dbcardealership.databinding.FragmentRegisterBinding
import com.example.dbcardealership.getShowroomIdByAddress
import com.example.dbcardealership.getShowroomsAddress
import com.example.dbcardealership.manager.ManagerRegisterModel
import com.example.dbcardealership.user.MainDb
import com.example.dbcardealership.user.User
import com.example.dbcardealership.user.UserLogin
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        var isManager = false
        var address: String = ""
        var addresses = listOf<String>()

        lifecycleScope.launch(Dispatchers.Main) {
            val addresses = getShowroomsAddress()
            val adapter = ArrayAdapter<String>(requireContext(), R.layout.list_item, addresses)
            binding.autoCompleteTxt.setAdapter(adapter)
        }

        binding.autoCompleteTxt.onItemClickListener =
            OnItemClickListener { parent, view, position, id ->
                address = parent.getItemAtPosition(position).toString()
            }

        binding.managerSwitch.setOnCheckedChangeListener { buttonView, isCheked ->
            if(isCheked) {
                isManager = true
                binding.editTextAddress.visibility = View.VISIBLE
            }
            else {
                isManager = false
                binding.editTextAddress.visibility = View.GONE
            }
        }

        binding.registerButton.setOnClickListener{
            lifecycleScope.launch(Dispatchers.IO) {
                val name = binding.editTextName.text.toString()
                val phone = binding.editTextPhone.text.toString()
                val password = binding.editTextTextPassword.text.toString()
                val servise = SetUpAPI()

                if(isManager){
                    val showroomId = getShowroomIdByAddress(address)
                    val managerResponse = servise.registerManager(ManagerRegisterModel(name, phone, password, showroomId))
                    if(managerResponse.isSuccessful){
                        val db = MainDb.getDb(requireContext())
                        db.userDao().insert(User(managerResponse.body()!!, name, phone, "manager"))
                        val fragment = ManagerFragment()
                        val transaction = fragmentManager?.beginTransaction()
                        transaction?.replace(R.id.nav_container, fragment)?.commit()
                    }
                }
                else {
                    val clientResponse = servise.registerClient(ClientRegisterModel(name, phone, password))
                    if(clientResponse.isSuccessful){
                        val db = MainDb.getDb(requireContext())
                        db.userDao().insert(User(clientResponse.body()!!, name, phone, "client"))
                        val fragment = ClientFragment()
                        val transaction = fragmentManager?.beginTransaction()
                        transaction?.replace(R.id.nav_container, fragment)?.commit()
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
            RegisterFragment().apply {
                arguments = Bundle().apply {
//                    putString(ARG_PARAM1, param1)
//                    putString(ARG_PARAM2, param2)
                }
            }
    }
}