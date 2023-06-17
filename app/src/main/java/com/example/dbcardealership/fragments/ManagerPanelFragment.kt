package com.example.dbcardealership.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.dbcardealership.R
import com.example.dbcardealership.databinding.FragmentManagerPanelBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private const val REQUEST_CODE = 123

class ManagerPanelFragment : Fragment() {

    lateinit var binding: FragmentManagerPanelBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentManagerPanelBinding.inflate(inflater, container, false)

        binding.newCarButton.setOnClickListener {
            val fragment = AddNewCarFragment()
            val transaction = fragmentManager?.beginTransaction()
            transaction?.replace(R.id.nav_container, fragment)?.commit()
        }

        binding.toBackupButton.setOnClickListener {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val fragment = BackupsFragment()
                val transaction = fragmentManager?.beginTransaction()
                transaction?.replace(R.id.nav_container, fragment)?.commit()
            }
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                val fragment = BackupsFragment()
                val transaction = fragmentManager?.beginTransaction()
                transaction?.replace(R.id.nav_container, fragment)?.commit()
            } else {
                ActivityCompat.requestPermissions(requireActivity(),
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    REQUEST_CODE)
            }
        }

        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ManagerPanelFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE && permissions[0] == Manifest.permission.WRITE_EXTERNAL_STORAGE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                try {
                    Toast.makeText(
                        requireContext(),
                        "Нажмите еще раз для загрузки файла",
                        Toast.LENGTH_LONG
                    ).show()
                } catch (e: Exception) {
                    e.printStackTrace()
                    backupFail()
                }

            } else backupFail()
        }
    }

    fun backupFail(){
        lifecycleScope.launch(Dispatchers.Main) {
            Toast.makeText(
                requireContext(),
                "Не удалось cохранить файл",
                Toast.LENGTH_LONG
            ).show()
        }
    }
}