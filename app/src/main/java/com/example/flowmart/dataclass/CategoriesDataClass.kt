package com.example.flowmart.dataclass

data class CategoriesDataClass(
    @PrimaryKey(autoGenerate = true) val id: Int=0,
    @ForeignKey val userId: Int,
    val name: String
)
