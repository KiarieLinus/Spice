package com.kiarielinus.spice.feature_meal_type.presetation.meal_recipe

import com.kiarielinus.spice.feature_meal_type.domain.model.MealIngredient
import com.kiarielinus.spice.feature_meal_type.domain.model.MealInstruction

data class MealRecipeDetailState(
    val ingredients: List<MealIngredient>  = emptyList(),
    val instructions: List<MealInstruction> = emptyList(),
)
