package com.artofmainstreams.examples.ui.example01.adapter
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.artofmainstreams.examples.R
import com.artofmainstreams.examples.ui.example01.TodoSectionModel

class TodoSectionAdapter(private val context: Context, private val items: ArrayList<TodoSectionModel>?) :
    RecyclerView.Adapter<TodoSectionAdapter.TodoSectionViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoSectionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_section, parent, false)

        return TodoSectionViewHolder(view)
    }

    override fun getItemCount(): Int {
        if (items != null) {
            return items.count()
        }

        return 0
    }

    override fun onBindViewHolder(holder: TodoSectionViewHolder, position: Int) {
        val name = items?.get(position)?.title
        val sections = items?.get(position)?.items

        val adapter = TodoAdapter(context, sections)

        holder.recyclerView.setHasFixedSize(true)
        holder.recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        holder.recyclerView.adapter = adapter

        holder.title.text = name
        holder.button_more.setOnClickListener {
            Toast.makeText(context, name, Toast.LENGTH_SHORT).show()
        }
    }

    inner class TodoSectionViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var title: TextView = view.findViewById(R.id.title) as TextView
        var recyclerView: RecyclerView = view.findViewById(R.id.recyclerViewTodo) as RecyclerView
        var button_more: Button = view.findViewById(R.id.button_more) as Button

    }

}