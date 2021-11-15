package com.draco.scrippy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.draco.scrippy.adapters.ScriptAdapter
import com.draco.scrippy.models.Script

class MainActivity : AppCompatActivity() {
    private lateinit var recycler: RecyclerView
    private lateinit var adapter: ScriptAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recycler = findViewById(R.id.recycler)

        adapter = ScriptAdapter(
            arrayOf(
                Script("Hello world 0", "echo Hello World!"),
                Script("Hello world 1", "echo Hello World!"),
                Script("Hello world 2", "echo Hello World!"),
                Script("Hello world 3", "echo Hello World!"),
                Script("Hello world 4", "echo Hello World!"),
                Script("Hello world 5", "echo Hello World!"),
                Script("Hello world 6", "echo Hello World!"),
                Script("Hello world 7", "echo Hello World!"),
                Script("Hello world 8", "echo Hello World!"),
            )
        )

        recycler.adapter = adapter
        recycler.layoutManager = LinearLayoutManager(this)
    }
}