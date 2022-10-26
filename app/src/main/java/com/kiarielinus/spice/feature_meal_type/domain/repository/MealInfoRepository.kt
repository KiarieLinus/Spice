package com.kiarielinus.spice.feature_meal_type.domain.repository

import com.kiarielinus.spice.core.util.Resource
import com.kiarielinus.spice.feature_meal_type.data.local.entity.MealRecipeInfoEntity
import com.kiarielinus.spice.feature_meal_type.domain.model.MealInfo
import com.kiarielinus.spice.feature_meal_type.domain.model.MealRecipe
import kotlinx.coroutines.flow.Flow

interface MealInfoRepository {
    suspend fun getMealInfo(mealType: String): Resource<List<MealInfo>>

    suspend fun getMealRecipe(mealId: Int): Resource<MealRecipe>

    suspend fun searchMeals(searchQuery: String): Resource<List<MealInfo>>

    fun getSavedRecipes(): Flow<List<MealRecipeInfoEntity>>

    suspend fun insertRecipe(mealRecipeInfoEntity: MealRecipeInfoEntity)

    suspend fun deleteRecipe(mealRecipeInfoEntity: MealRecipeInfoEntity)
}