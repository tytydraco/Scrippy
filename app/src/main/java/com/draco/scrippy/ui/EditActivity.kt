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
    private lateinit var name: EditText
    private lateinit var code: EditText

    private lateinit var db: ScriptDatabase

    private val executorService = Executors.newFixedThreadPool(1)

    private lateinit var script: Script

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)

        supportActionBar?.elevation = 0f

        name = findViewById(R.id.name)
        code = findViewById(R.id.code)

        db = Room.databaseBuilder(
            this,
            ScriptDatabase::class.java,
            ScriptDatabase.NAME
        ).build()

        val id = intent.getIntExtra("id", -1)

        code.requestFocus()

        executorService.execute {
            script = db.scriptDao().get(id)
            name.setText(script.name)
            code.setText(script.contents)
        }
    }

    override fun onBackPressed() {
        script.name = name.text.toString()
        script.contents = code.text.toString()

        executorService.execute {
            db.scriptDao().update(script)

            runOnUiThread {
                super.onBackPressed()
            }
        }
    }
}