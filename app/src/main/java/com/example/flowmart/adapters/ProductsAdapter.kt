package com.example.flowmart.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.flowmart.data.models.Product
import com.example.flowmart.databinding.RecyclerViewProductsBinding

class ProductAdapter: RecyclerView.Adapter<ProductAdapter.ViewHolder>() {
    private var productList = mutableListOf<Product>()

    class ViewHolder(private val binding: RecyclerViewProductsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(product: Product) {
            binding.productName.text = product.name
            binding.productCategory.text = product.category.name
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RecyclerViewProductsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(productList[position])

    }

    fun addProducts(products: List<Product>) {
        productList.clear()
        productList.addAll(products)
        notifyDataSetChanged()
    }
}