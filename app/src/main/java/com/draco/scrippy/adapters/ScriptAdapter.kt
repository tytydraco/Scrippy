package com.draco.scrippy.adapters

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.draco.scrippy.R
import com.draco.scrippy.database.Script
import com.draco.scrippy.database.ScriptDatabase
import com.draco.scrippy.ui.EditActivity
import com.draco.scrippy.ui.RunActivity
import java.util.concurrent.Executors

class ScriptAdapter(var scripts: MutableList<Script> = mutableListOf()) :
    RecyclerView.Adapter<ScriptAdapter.ViewHolder>() {
    private lateinit var db: ScriptDatabase

    private val executorService = Executors.newFixedThreadPool(1)

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.name)
        val delete: ImageButton = view.findViewById(R.id.delete)
        val run: ImageButton = view.findViewById(R.id.run)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)

        db = Room.databaseBuilder(
            recyclerView.context,
            ScriptDatabase::class.java,
            ScriptDatabase.NAME
        ).build()
    }

    override fun getItemCount() = scripts.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = LayoutInflater.from(parent.context)
            .inflate(R.layout.script_item, parent, false)
        return ViewHolder(binding)
    }

    private fun delete(context: Context, position: Int) {
        val script = scripts[position]

        AlertDialog.Builder(context)
            .apply {
                setTitle(R.string.are_you_sure_title)
                setMessage(R.string.are_you_sure_message)
                setPositiveButton(R.string.confirm) { _, _ ->
                    scripts.removeAt(position)
                    executorService.execute {
                        db.scriptDao().delete(script)
                    }
                    notifyItemRemoved(position)
                }
                setNegativeButton(R.string.cancel, null)
            }
            .create()
            .show()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val script = scripts[position]

        holder.name.text = script.name

        holder.delete.setOnClickListener {
            delete(holder.itemView.context, position)
        }

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, EditActivity::class.java)
                .putExtra("position", position)
            holder.itemView.context.startActivity(intent)
        }

        holder.run.setOnClickListener {
            val intent = Intent(holder.itemView.context, RunActivity::class.java)
                .putExtra("position", position)
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemId(position: Int) = scripts[position].id?.toLong() ?: -1
}