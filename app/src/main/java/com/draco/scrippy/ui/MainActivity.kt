package com.draco.scrippy.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.draco.scrippy.R
import com.draco.scrippy.adapters.ScriptAdapter
import com.draco.scrippy.database.ScriptDatabase
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {
    private lateinit var recycler: RecyclerView
    private lateinit var adapter: ScriptAdapter

    private lateinit var db: ScriptDatabase

    private val executorService = Executors.newFixedThreadPool(1)

    private val upload = registerForActivityResult(ActivityResultContracts.GetContent()) {
        contentResolver
            .openInputStream(it)
            ?.bufferedReader()
            .use {
                val text = it?.readText() ?: ""

            }
    }

    private fun updateScripts() {
        executorService.execute {
            val scripts = db.scriptDao().getAll()
            adapter.scripts = scripts.toMutableList()

            runOnUiThread {
                adapter.notifyDataSetChanged()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        db = Room.databaseBuilder(
            this,
            ScriptDatabase::class.java,
            ScriptDatabase.NAME
        ).build()

        recycler = findViewById(R.id.recycler)
        adapter = ScriptAdapter()
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = adapter
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        MenuInflater(this).inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.upload -> {
                upload.launch("")
                true
            }
            R.id.create -> {
                val intent = Intent(this, EditActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onResume() {
        super.onResume()
        updateScripts()
    }
}