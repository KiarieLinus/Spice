package com.kiarielinus.spice.feature_meal_type.data.remote.dto.recipe_dto

import com.kiarielinus.spice.feature_meal_type.domain.model.MealInstruction

data class AnalyzedInstruction(
    val name: String,
    val steps: List<Step>
){
    fun toMealInstruction(): MealInstruction{
        return MealInstruction(
            steps = steps.map { it.toMealInstructionStep() }
        )
    }
}