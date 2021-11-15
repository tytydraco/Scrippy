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
    private var position: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)

        name = findViewById(R.id.name)
        code = findViewById(R.id.code)

        db = Room.databaseBuilder(
            this,
            ScriptDatabase::class.java,
            ScriptDatabase.NAME
        ).build()

        position = intent.getIntExtra("position", -1)

        if (position != -1) {
            executorService.execute {
                script = db.scriptDao().getAll()[position]
                name.setText(script.name)
                code.setText(script.contents)
            }
        } else {
            script = Script("", "")
        }
    }

    override fun onBackPressed() {
        script.name = name.text.toString()
        script.contents = code.text.toString()

        executorService.execute {
            if (position != -1)
                db.scriptDao().update(script)
            else
                db.scriptDao().insert(script)

            runOnUiThread {
                super.onBackPressed()
            }
        }
    }
}