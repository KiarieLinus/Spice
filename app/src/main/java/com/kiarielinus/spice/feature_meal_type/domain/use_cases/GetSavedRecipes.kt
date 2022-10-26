package com.kiarielinus.spice.feature_meal_type.domain.use_cases

import com.kiarielinus.spice.feature_meal_type.data.local.entity.MealRecipeInfoEntity
import com.kiarielinus.spice.feature_meal_type.domain.repository.MealInfoRepository
import kotlinx.coroutines.flow.Flow

class GetSavedRecipes(
    private val repository: MealInfoRepository
) {
    operator fun invoke():Flow<List<MealRecipeInfoEntity>>{
        return repository.getSavedRecipes()
    }
}