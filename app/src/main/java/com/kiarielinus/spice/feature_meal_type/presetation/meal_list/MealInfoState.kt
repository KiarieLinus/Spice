package com.kiarielinus.spice.feature_meal_type.presetation.meal_list

import com.kiarielinus.spice.feature_meal_type.domain.model.MealInfo

data class MealInfoState(
    val mealInfoItems: List<MealInfo> = emptyList(),
    val isLoading:Boolean = false
)
