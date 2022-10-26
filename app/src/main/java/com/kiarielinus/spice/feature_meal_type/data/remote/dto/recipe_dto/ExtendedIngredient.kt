package com.kiarielinus.spice.feature_meal_type.data.remote.dto.recipe_dto

import com.kiarielinus.spice.feature_meal_type.domain.model.MealIngredient

data class ExtendedIngredient(
    val aisle: String,
    val amount: Double,
    val consistency: String,
    val id: Int,
    val image: String,
    val measures: Measures,
    val meta: List<String>,
    val name: String,
    val nameClean: String,
    val original: String,
    val originalName: String,
    val unit: String
){
    fun toMealIngredient(): MealIngredient{
        return MealIngredient(
            name = name,
            amount = measures.metric.amount,
            unit = measures.metric.unitShort
        )
    }
}