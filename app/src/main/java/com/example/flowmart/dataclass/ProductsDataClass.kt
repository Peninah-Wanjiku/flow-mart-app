package com.example.flowmart.dataclass

data class ProductsDataClass(
    @PrimaryKey(autoGenerate = true) val id: Int=0,
    @ForeignKey val categoryId : Int,
    val name: String,
    val quanity: String
    )
