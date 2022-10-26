package com.kiarielinus.spice.feature_meal_type.data.remote.dto.recipe_dto

data class IngredientX(
    val amount: Double,
    val id: Int,
    val name: String,
    val nutrients: List<NutrientX>,
    val unit: String
)