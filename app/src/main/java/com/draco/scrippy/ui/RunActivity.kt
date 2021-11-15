package com.draco.scrippy.ui

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.draco.scrippy.R
import com.draco.scrippy.database.Script
import com.draco.scrippy.database.ScriptDatabase
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class RunActivity : AppCompatActivity() {
    private lateinit var output: TextView

    private lateinit var db: ScriptDatabase

    private val executorService = Executors.newFixedThreadPool(1)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_run)

        output = findViewById(R.id.output)

        db = Room.databaseBuilder(
            this,
            ScriptDatabase::class.java,
            ScriptDatabase.NAME
        ).build()

        val position = intent.getIntExtra("position", 0)
        executorService.execute {
            val script = db.scriptDao().getAll()[position]
            runScript(script)
        }
    }

    private fun runScript(script: Script) {
        val contents = script.contents

        executorService.execute {
            val process = ProcessBuilder("sh", "-c", contents)
                .directory(externalCacheDir)
                .start()
                .also {
                    it.waitFor()
                }

            val out = process.inputStream.bufferedReader().use { it.readText() }
            Log.d("OUTPUT", out)
            runOnUiThread {
                output.text = out
            }
        }
    }
}