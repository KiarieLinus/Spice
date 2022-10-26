package com.kiarielinus.spice.feature_meal_type.domain.use_cases

import com.kiarielinus.spice.feature_meal_type.data.local.entity.MealRecipeInfoEntity
import com.kiarielinus.spice.feature_meal_type.domain.repository.MealInfoRepository

class InsertRecipe(
    private val repository: MealInfoRepository
) {
    suspend operator fun invoke(mealRecipeInfoEntity: MealRecipeInfoEntity){
        repository.insertRecipe(mealRecipeInfoEntity)
    }
}