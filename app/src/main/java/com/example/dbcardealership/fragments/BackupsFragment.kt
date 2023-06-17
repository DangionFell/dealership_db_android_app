package com.example.dbcardealership.fragments

import android.Manifest
import android.content.ContentResolver
import android.content.ContentValues
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.dbcardealership.R
import com.example.dbcardealership.SetUpAPI
import com.example.dbcardealership.databinding.FragmentBackupsBinding
import com.example.dbcardealership.databinding.FragmentContractGuestBinding
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.apache.poi.ss.usermodel.CellType
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class BackupsFragment : Fragment() {
    lateinit var binding: FragmentBackupsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBackupsBinding.inflate(inflater, container, false)

        binding.carJsonButton.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO){
                val service = SetUpAPI()
                val respond = service.getCars()
                val gson = Gson()
                val json = gson.toJson(respond)
                downloadFile(json, "cars.json")
            }
        }

        binding.carExcelButton.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO){
                val service = SetUpAPI()
                val respond = service.getCars()
                val workbook = XSSFWorkbook()
                val sheet = workbook.createSheet("Cars")
                for ((index, car) in respond.withIndex()) {
                    val dataRow = sheet.createRow(index)
                    dataRow.createCell(0, CellType.STRING).setCellValue(car.id.toDouble())
                    dataRow.createCell(1, CellType.STRING).setCellValue(car.model)
                    dataRow.createCell(2, CellType.STRING).setCellValue(car.config)
                    dataRow.createCell(3, CellType.STRING).setCellValue(car.yearOfManufacture)
                    dataRow.createCell(4, CellType.STRING).setCellValue(car.horsepower.toDouble())
                    dataRow.createCell(5, CellType.STRING).setCellValue(car.price.toDouble())
                    dataRow.createCell(6, CellType.STRING).setCellValue(car.color)
                    dataRow.createCell(7, CellType.STRING).setCellValue(car.state)
                    dataRow.createCell(8, CellType.STRING).setCellValue(car.showroomId.toDouble())
                }
                downloadExcelFile(workbook, "cars.xlsx")
                workbook.close()
            }
        }

        binding.clientJsonButton.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO){
                val service = SetUpAPI()
                val respond = service.getClients()
                val gson = Gson()
                val json = gson.toJson(respond)
                downloadFile(json, "clients.json")
            }
        }

        binding.clientTxtButton.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO){
                val service = SetUpAPI()
                val respond = service.getClients()
                val gson = Gson()
                val json = gson.toJson(respond)
                downloadFile(json, "clients.txt")
            }
        }

        binding.managerJsonButton.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO){
                val service = SetUpAPI()
                val respond = service.getManagers()
                val gson = Gson()
                val json = gson.toJson(respond)
                downloadFile(json, "managers.json")
            }
        }

        binding.managerTxtButton.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO){
                val service = SetUpAPI()
                val respond = service.getManagers()
                val gson = Gson()
                val json = gson.toJson(respond)
                downloadFile(json, "managers.txt")
            }
        }

        binding.contactJsonButton.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO){
                val service = SetUpAPI()
                val respond = service.getContracts()
                val gson = Gson()
                val json = gson.toJson(respond)
                downloadFile(json, "contracts.json")
            }
        }

        binding.contactTxtButton.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO){
                val service = SetUpAPI()
                val respond = service.getContracts()
                val gson = Gson()
                val json = gson.toJson(respond)
                downloadFile(json, "contracts.txt")
            }
        }

        binding.showroomJsonButton.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO){
                val service = SetUpAPI()
                val respond = service.getShowrooms()
                val gson = Gson()
                val json = gson.toJson(respond)
                downloadFile(json, "showrooms.json")
            }
        }

        binding.showroomTxtButton.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO){
                val service = SetUpAPI()
                val respond = service.getShowrooms()
                val gson = Gson()
                val json = gson.toJson(respond)
                downloadFile(json, "showrooms.txt")
            }
        }

        return binding.root
    }

    fun downloadFile(json: String, file_name: String){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            writeFileAndroid10(json, file_name)
        }
        else {
            writeFileAndroid9(json, file_name)
        }
    }

    fun writeFileAndroid9(json: String, file_name: String){
        val downloadDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val file = File(downloadDirectory, file_name)
        file.writeText(json)
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    fun writeFileAndroid10(json: String, file_name: String){
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, file_name)
        }
        val dstUri = requireContext().contentResolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)
        if (dstUri != null) {
            val dst = requireContext().contentResolver.openOutputStream(dstUri)
            dst?.use { outputStream ->
                outputStream.write(json.toByteArray())
            }
            backupOk()
        } else {
            backupFail()
        }

    }


    fun downloadExcelFile(workbook: XSSFWorkbook, file_name: String){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            writeExcelFileAndroid10(workbook, file_name)
        }
        else {
            writeExcelFileAndroid9(workbook, file_name)
        }
    }

    fun writeExcelFileAndroid9(workbook: XSSFWorkbook, file_name: String){
        val downloadDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val file = File(downloadDirectory, file_name)
        FileOutputStream(file).use { outputStream ->
            workbook.write(outputStream)
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    fun writeExcelFileAndroid10(workbook: XSSFWorkbook, file_name: String){
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, file_name)
        }
        val dstUri = requireContext().contentResolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)
        if (dstUri != null) {
            val dst = requireContext().contentResolver.openOutputStream(dstUri)
            dst?.use { outputStream ->
                outputStream.use {
                    workbook.write(it)
                    it.flush()
                }
            }
            backupOk()
        } else {
            backupFail()
        }

    }





    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            BackupsFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }


    fun backupOk(){
        lifecycleScope.launch(Dispatchers.Main) {
            Toast.makeText(
                requireContext(),
                "Файл cохранен в папку загрузки",
                Toast.LENGTH_LONG
            ).show()
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