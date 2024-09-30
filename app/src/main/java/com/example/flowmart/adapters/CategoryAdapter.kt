package com.example.flowmart.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.flowmart.data.models.Category
import com.example.flowmart.databinding.RecyclerViewCategorizedProductsBinding

class CategoryAdapter : RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {
    private val categoryList = mutableListOf<Category>()

    class ViewHolder(private val binding: RecyclerViewCategorizedProductsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(category: Category) {
            binding.productCategory.text = category.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RecyclerViewCategorizedProductsBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(categoryList[position])
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addCategories(mCategories: List<Category>) {
        categoryList.clear()
        categoryList.addAll(mCategories)
        notifyDataSetChanged()
    }
}
