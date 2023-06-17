package com.example.dbcardealership

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dbcardealership.databinding.ActivityMainBinding
import com.example.dbcardealership.showroom.Showroom
import com.example.dbcardealership.showroom.ShowroomAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity(), ShowroomAdapter.Listener {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lifecycleScope.launch(Dispatchers.IO) {
            val showrooms = getAllShowrooms()
            withContext(Dispatchers.Main) {
                InitRC(showrooms)
            }
        }
    }

    fun InitRC(showrooms: List<Showroom>){
        binding.apply {
            rcView.layoutManager = LinearLayoutManager(this@MainActivity)
            rcView.adapter = ShowroomAdapter(this@MainActivity, showrooms)
        }
    }

    override fun onClick(showroom: Showroom) {
        startActivity(Intent(this, ShowroomActivity::class.java).apply {
            putExtra("showroom_id", showroom.id)
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.profile -> {
                startActivity(Intent(this, ProfileActivity::class.java))
            }
        }
        return true
    }
}