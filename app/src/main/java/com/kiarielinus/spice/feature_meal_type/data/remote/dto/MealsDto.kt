package com.kiarielinus.spice.feature_meal_type.data.remote.dto

data class MealsDto(
    val number: Int,
    val offset: Int,
    val results: List<MealInfoDto>,
    val totalResults: Int
)