package com.example.dbcardealership

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.dbcardealership.fragments.*
import com.example.dbcardealership.user.MainDb
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        lifecycleScope.launch(Dispatchers.IO) {
            val db = MainDb.getDb(this@ProfileActivity)
            val user = db.userDao().getAll()
            if (user.isEmpty()) {
                supportFragmentManager.beginTransaction().replace(R.id.nav_container, LoginFragment()).commit()
            }
            else {
                if(user[0].user_state == "client"){
                    supportFragmentManager.beginTransaction().replace(R.id.nav_container, ClientFragment()).commit()
                }
                else {
                    supportFragmentManager.beginTransaction().replace(R.id.nav_container, ManagerFragment()).commit()
                }
            }
        }
    }
}