package com.draco.scrippy.ui

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.draco.scrippy.R
import com.draco.scrippy.database.Script
import com.draco.scrippy.database.ScriptDatabase
import com.draco.scrippy.utils.OutputBuffer
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicBoolean

class RunActivity : AppCompatActivity() {
    private lateinit var output: TextView

    private lateinit var db: ScriptDatabase

    private val executorService = Executors.newFixedThreadPool(1)

    private var process: Process? = null
    private var scanForOutput = AtomicBoolean(true)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_run)

        output = findViewById(R.id.output)

        db = Room.databaseBuilder(
            this,
            ScriptDatabase::class.java,
            ScriptDatabase.NAME
        ).build()

        val id = intent.getIntExtra("id", 0)
        executorService.execute {
            val script = db.scriptDao().get(id)
            runScript(script)
        }
    }

    private fun runScript(script: Script) {
        val contents = script.contents

        executorService.execute {
            process = ProcessBuilder("sh", "-c", contents)
                .directory(externalCacheDir)
                .redirectErrorStream(true)
                .start()

            val buffer = OutputBuffer(128)
            process?.inputStream?.bufferedReader().use {
                while (scanForOutput.get()) {
                    try {
                        val line = it?.readLine() ?: break
                        buffer.add(line)

                        val out = buffer.get()
                        runOnUiThread {
                            output.text = out
                        }
                    } catch (_: Exception) {
                        scanForOutput.set(false)
                        return@execute
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        scanForOutput.set(false)
        process?.destroy()
        super.onDestroy()
    }
}