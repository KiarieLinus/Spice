package com.kiarielinus.spice.feature_meal_type.presetation.meal_list

import com.kiarielinus.spice.feature_meal_type.domain.model.MealRecipe

data class SavedRecipeState(
    val mealRecipe: MealRecipe? = null,
    val showRecipe: Boolean = false
)
