package com.example.flowmart.dataclass

data class UserDataClass(
    @PrimaryKey(autoGenerate = true) val id: Int=0,
    val name: String,
    val email: String,
    val phone: String,
    val password: String
)
