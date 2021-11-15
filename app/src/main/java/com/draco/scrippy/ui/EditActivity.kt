package com.draco.scrippy.ui

import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.draco.scrippy.R
import com.draco.scrippy.database.Script
import com.draco.scrippy.database.ScriptDatabase
import java.util.concurrent.Executors

class EditActivity: AppCompatActivity() {
    private lateinit var code: EditText

    private lateinit var db: ScriptDatabase

    private val executorService = Executors.newFixedThreadPool(1)

    private lateinit var script: Script

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)

        code = findViewById(R.id.code)

        db = Room.databaseBuilder(
            this,
            ScriptDatabase::class.java,
            ScriptDatabase.NAME
        ).build()

        val position = intent.getIntExtra("position", 0)
        executorService.execute {
            script = db.scriptDao().getAll().get(position)
            code.setText(script.contents)
        }
    }

    override fun onBackPressed() {
        script.contents = code.text.toString()

        executorService.execute {
            db.scriptDao().update(script)

            runOnUiThread {
                super.onBackPressed()
            }
        }
    }
}