package com.example.listricks.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.ListAdapter
import com.example.listricks.R
import com.example.listricks.models.ListItem


class ListsAdapter : ListAdapter<ListItem, ListsAdapter.ListsHolder>(DiffCallback()) {
    private var cardClickListener: ClickListener? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListsHolder {
        val v: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_list_row, parent, false)
        return ListsHolder(v)
    }

    override fun onBindViewHolder(holder: ListsHolder, position: Int) {
        holder.bind(getItem(position))
        holder.setOnClickCardListener {
            cardClickListener?.onClick(getItem(position).listName)
        }
    }

    class ListsHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val row: CardView = view.findViewById(R.id.listCardView)
        private val listNameView: TextView = view.findViewById(R.id.listName)


        fun bind(listRow: ListItem) {
            listNameView.text = listRow.listName.split("_")[1]
        }

        fun setOnClickCardListener(cardListener: View.OnClickListener) {
            row.setOnClickListener(cardListener)
        }
    }

    fun setCardClickListener(clickListener: ClickListener) {
        this.cardClickListener = clickListener
    }

    fun interface ClickListener {
        fun onClick(listName: String)
    }

    class DiffCallback : DiffUtil.ItemCallback<ListItem>() {
        override fun areItemsTheSame(oldItem: ListItem, newItem: ListItem) =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: ListItem, newItem: ListItem) =
            oldItem == newItem
    }
}
