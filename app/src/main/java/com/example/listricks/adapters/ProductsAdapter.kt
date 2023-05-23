package com.example.listricks.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.ListAdapter
import com.example.listricks.R
import com.example.listricks.models.ProductItem


class ProductsAdapter : ListAdapter<ProductItem, ProductsAdapter.ProductsHolder>(DiffCallback()) {
    private var deleteClickListener: ClickListener? = null
    private var selectClickListener: ClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductsHolder {
        val v: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_product_row, parent, false)
        return ProductsHolder(v)
    }

    override fun onBindViewHolder(holder: ProductsHolder, position: Int) {
        holder.bind(getItem(position))
        holder.setOnClickCheckBoxListener{
            selectClickListener?.onClick(holder.getProductName())
        }
        holder.setOnClickDeleteListener{
            deleteClickListener?.onClick(holder.getProductName())
        }
    }

    class ProductsHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val deleteButton: ImageButton = view.findViewById(R.id.deleteProductButton)
        private val productCheckBox: CheckBox = view.findViewById(R.id.productCheckBox)

        fun bind(productRow: ProductItem) {
            productCheckBox.text = productRow.productName
            productCheckBox.setChecked(productRow.isMarked)
        }

        fun setOnClickDeleteListener(deleteListener: View.OnClickListener) {
            deleteButton.setOnClickListener(deleteListener)
        }

        fun setOnClickCheckBoxListener(selectListener: View.OnClickListener) {
            productCheckBox.setOnClickListener(selectListener)
        }

        fun getProductName(): String {
            return productCheckBox.text.toString()
        }
    }

    fun setSelectClickListener(clickListener: ClickListener) {
        this.selectClickListener = clickListener
    }

    fun setDeleteListener(clickListener: ClickListener) {
        this.deleteClickListener = clickListener
    }

    fun interface ClickListener {
        fun onClick(productName: String)
    }

    class DiffCallback : DiffUtil.ItemCallback<ProductItem>() {
        override fun areItemsTheSame(oldItem: ProductItem, newItem: ProductItem) =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: ProductItem, newItem: ProductItem) =
            oldItem == newItem
    }
}
