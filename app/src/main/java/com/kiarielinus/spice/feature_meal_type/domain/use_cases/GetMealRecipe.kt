package com.kiarielinus.spice.feature_meal_type.domain.use_cases

import com.kiarielinus.spice.core.util.Resource
import com.kiarielinus.spice.feature_meal_type.domain.model.MealRecipe
import com.kiarielinus.spice.feature_meal_type.domain.repository.MealInfoRepository

class GetMealRecipe(
    private val repository: MealInfoRepository
) {
    suspend operator fun invoke(mealId: Int): Resource<MealRecipe> {
        return repository.getMealRecipe(mealId)
    }
}