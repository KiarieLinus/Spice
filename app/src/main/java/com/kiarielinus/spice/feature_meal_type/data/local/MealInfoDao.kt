package com.kiarielinus.spice.feature_meal_type.data.local

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import com.kiarielinus.spice.feature_meal_type.data.local.entity.MealInfoEntity
import com.kiarielinus.spice.feature_meal_type.data.local.entity.MealRecipeInfoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MealInfoDao {

    @Insert(entity = MealRecipeInfoEntity::class, onConflict = REPLACE)
    suspend fun insertRecipe(mealRecipeInfoEntity: MealRecipeInfoEntity)

    @Delete
    suspend fun deleteRecipe(mealRecipeInfoEntity: MealRecipeInfoEntity)

    @Query("SELECT * FROM mealrecipeinfoentity")
    fun getSavedRecipes(): Flow<List<MealRecipeInfoEntity>>
}