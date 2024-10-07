package com.example.flowmart.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.flowmart.data.models.Category
import com.example.flowmart.databinding.RecyclerViewChipCategoriesBinding

class ChipAdapter: RecyclerView.Adapter<ChipAdapter.ViewHolder>() {
    private val chipList = mutableListOf<Category>()
    class ViewHolder(val binding: RecyclerViewChipCategoriesBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RecyclerViewChipCategoriesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return chipList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val chip = chipList[position]
        holder.binding.categoryChip.text = chip.name

    }
    fun addCategories(categories: List<Category>) {
        chipList.clear()
        chipList.addAll(categories)
        notifyDataSetChanged()
    }
}