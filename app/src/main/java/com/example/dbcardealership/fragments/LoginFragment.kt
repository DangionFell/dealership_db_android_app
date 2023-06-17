package com.example.dbcardealership.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.dbcardealership.R
import com.example.dbcardealership.SetUpAPI
import com.example.dbcardealership.databinding.FragmentLoginBinding
import com.example.dbcardealership.user.MainDb
import com.example.dbcardealership.user.User
import com.example.dbcardealership.user.UserLogin
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class LoginFragment : Fragment() {

//    private var param1: String? = null
//    private var param2: String? = null
    private lateinit var binding: FragmentLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        arguments?.let {
//            param1 = it.getString(ARG_PARAM1)
//            param2 = it.getString(ARG_PARAM2)
//        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater, container, false)

        binding.toRegText.setOnClickListener {
            val fragment = RegisterFragment()
            val transaction = fragmentManager?.beginTransaction()
            transaction?.replace(R.id.nav_container, fragment)?.commit()
        }

        binding.loginButton.setOnClickListener{
            lifecycleScope.launch(Dispatchers.IO) {
                val phone = binding.editTextPhone.text.toString()
                val password = binding.editTextTextPassword.text.toString()
                val servise = SetUpAPI()

                var clientResponse = servise.loginClient(UserLogin(phone, password))
                if(clientResponse.isSuccessful){
                    val db = MainDb.getDb(requireContext())
                    val client = clientResponse.body()
                    if (client != null) {
                        db.userDao().insert(User(client.id, client.name, client.phone, "client"))
                    }
                    val fragment = ClientFragment()
                    val transaction = fragmentManager?.beginTransaction()
                    transaction?.replace(R.id.nav_container, fragment)?.commit()
                }

                val managerResponse = servise.loginManager(UserLogin(phone, password))
                if(managerResponse.isSuccessful){
                    val db = MainDb.getDb(requireContext())
                    val manager = managerResponse.body()
                    if (manager != null) {
                        db.userDao().insert(User(manager.id, manager.name, manager.phone, "manager"))
                    }
                    val fragment = ManagerFragment()
                    val transaction = fragmentManager?.beginTransaction()
                    transaction?.replace(R.id.nav_container, fragment)?.commit()
                }
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        requireContext(),
                        "Неправильное имя пользователя или пароль",
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
            LoginFragment().apply {
//                arguments = Bundle().apply {
//                    putString(ARG_PARAM1, param1)
//                    putString(ARG_PARAM2, param2)
//                }
            }
    }
}