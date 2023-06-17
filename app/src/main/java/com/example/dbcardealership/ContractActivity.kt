package com.example.dbcardealership

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.example.dbcardealership.fragments.*
import com.example.dbcardealership.user.MainDb
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ContractActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contract)

        val car_id = intent.getIntExtra("car_id", 1)
        val showroom_id = intent.getIntExtra("showroom_id", 1)

        lifecycleScope.launch(Dispatchers.IO) {
            val db = MainDb.getDb(this@ContractActivity)
            val user = db.userDao().getAll()
            if (user.isEmpty()) {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.nav_container_sec, ContractGuestFragment()).commit()
            } else {
                if (user[0].user_state == "client") {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.nav_container_sec, ContractClientFragment()).commit()
                } else {
                    supportFragmentManager.beginTransaction().replace(
                        R.id.nav_container_sec,
                        ContractManagerFragment.newInstance(car_id, showroom_id)
                    ).commit()
                }
            }
        }
    }
}