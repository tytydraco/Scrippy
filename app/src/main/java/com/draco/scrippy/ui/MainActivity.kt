package com.draco.scrippy.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.draco.scrippy.R
import com.draco.scrippy.adapters.ScriptAdapter
import com.draco.scrippy.database.ScriptDatabase
import com.draco.scrippy.database.Script
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {
    private lateinit var recycler: RecyclerView
    private lateinit var adapter: ScriptAdapter

    private lateinit var db: ScriptDatabase

    private val executorService = Executors.newFixedThreadPool(1)

    private fun getScripts(callback: (scripts: Array<Script>) -> Unit) {
        executorService.execute {
            callback(db.scriptDao().getAll())
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

        getScripts {
            adapter = ScriptAdapter(
                it
            )

            recycler.adapter = adapter
        }

        recycler.layoutManager = LinearLayoutManager(this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        MenuInflater(this).inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.upload -> {
                true
            }
            R.id.create -> {
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}