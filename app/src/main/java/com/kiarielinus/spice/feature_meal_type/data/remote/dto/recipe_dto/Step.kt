package com.kiarielinus.spice.feature_meal_type.data.remote.dto.recipe_dto

import com.kiarielinus.spice.feature_meal_type.domain.model.MealInstructionStep

data class Step(
    val equipment: List<Equipment>,
    val ingredients: List<Ingredient>,
    val length: Length,
    val number: Int,
    val step: String
){
    fun toMealInstructionStep(): MealInstructionStep {
        return MealInstructionStep(
            step = step
        )
    }
}