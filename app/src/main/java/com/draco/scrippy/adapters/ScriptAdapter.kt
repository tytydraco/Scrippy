package com.draco.scrippy.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.draco.scrippy.R
import com.draco.scrippy.database.Script

class ScriptAdapter(private val scripts: Array<Script>) :
    RecyclerView.Adapter<ScriptAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name = view.findViewById<TextView>(R.id.name)
    }

    override fun getItemCount() = scripts.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = LayoutInflater.from(parent.context)
            .inflate(R.layout.script_item, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val script = scripts[position]

        holder.name.text = script.name
    }
}