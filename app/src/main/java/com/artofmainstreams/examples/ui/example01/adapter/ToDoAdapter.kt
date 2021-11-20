package com.artofmainstreams.examples.ui.example01.adapter
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.artofmainstreams.examples.R
import com.artofmainstreams.examples.ui.example01.TodoModel
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.facebook.shimmer.Shimmer
import com.facebook.shimmer.ShimmerDrawable

class TodoAdapter(private val context: Context, private val items: ArrayList<TodoModel>?) :
    RecyclerView.Adapter<TodoAdapter.TodoViewHolder>() {
    private val shimmer = Shimmer.ColorHighlightBuilder() // The attributes for a ShimmerDrawable is set by this builder
        .setBaseColor(ContextCompat.getColor(context, R.color.color_blue_50))
        .setDuration(1800) // how long the shimmering animation takes to do one full sweep
        .setBaseAlpha(1.0f) //the alpha of the underlying children
        .setHighlightColor(ContextCompat.getColor(context, R.color.color_blue_100))
        .setDirection(Shimmer.Direction.LEFT_TO_RIGHT)
        .setAutoStart(true)
        .build()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_todo, parent, false)

        return TodoViewHolder(view)
    }

    override fun getItemCount(): Int {
        if (items != null) {
            return items.count()
        }
        return 0
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        val title = items?.get(position)?.title
        val imageUrl = items?.get(position)?.imageUrl

        holder.todoTitle.text = title

        // This is the placeholder for the imageView
        val shimmerDrawable = ShimmerDrawable().apply {
            setShimmer(shimmer)
        }

        Glide.with(context)
            .load(imageUrl)
            .placeholder(shimmerDrawable)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .centerCrop()
            .error(android.R.drawable.ic_menu_report_image)
            .into(holder.todoImage)
    }

    inner class TodoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var todoTitle: TextView = view.findViewById(R.id.todoTitle) as TextView
        var todoImage: ImageView = view.findViewById(R.id.todoImage) as ImageView

    }

}