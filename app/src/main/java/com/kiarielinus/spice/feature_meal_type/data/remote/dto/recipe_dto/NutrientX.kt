package com.kiarielinus.spice.feature_meal_type.data.remote.dto.recipe_dto

import com.kiarielinus.spice.feature_meal_type.domain.model.MealNutrient

data class NutrientX(
    val amount: Double,
    val name: String,
    val percentOfDailyNeeds: Double,
    val unit: String
){
    fun toMealNutrients(): MealNutrient {
        return MealNutrient(
            name = name,
            amount = amount,
            unit = unit,
        )
    }
}