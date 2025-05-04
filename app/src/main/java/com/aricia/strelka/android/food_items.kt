package com.aricia.strelka.android

import kotlinx.serialization.Serializable

@Serializable
data class FoodItem(
    val id: String,
    val user_id: String?,
    val name: String,
    val unit: String,
    val calories_per_unit: Double,
    val created_at: String
)

@Serializable
data class UserFoodEntry( //future data class to record the user's eating
    val id: String,
    val user_id: String,
    val entry_date: String,
    val food_item_id: String,
    val quantity: Double,
    val created_at: String
)