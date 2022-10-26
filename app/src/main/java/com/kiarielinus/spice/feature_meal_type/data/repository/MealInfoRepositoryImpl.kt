package com.kiarielinus.spice.feature_meal_type.data.repository

import com.kiarielinus.spice.core.util.Resource
import com.kiarielinus.spice.feature_meal_type.data.local.MealInfoDao
import com.kiarielinus.spice.feature_meal_type.data.local.entity.MealRecipeInfoEntity
import com.kiarielinus.spice.feature_meal_type.data.remote.MealsApi
import com.kiarielinus.spice.feature_meal_type.domain.model.MealInfo
import com.kiarielinus.spice.feature_meal_type.domain.model.MealRecipe
import com.kiarielinus.spice.feature_meal_type.domain.repository.MealInfoRepository
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException
import java.io.IOException

class MealInfoRepositoryImpl(
    private val api: MealsApi,
    private val dao: MealInfoDao,
) : MealInfoRepository {

    override suspend fun getMealInfo(mealType: String): Resource<List<MealInfo>> {
        var mealInfos = listOf<MealInfo>()

        return try {
            val response = api.getMeals(mealType = mealType).results
            val mealInfoEntities = response.map { it.toMealInfoEntity() }
            mealInfos = mealInfoEntities.map { it.toMealInfo() }
            Resource.Success(mealInfos)
        } catch (e: HttpException) {
            Resource.Error(
                message = if (e.code() == 402) {
                    "Your daily points limit of 150 has been reached."
                } else "Oops! Something went wrong",
                data = mealInfos
            )
        } catch (e: IOException) {
            Resource.Error(
                message = "Couldn't reach server, check your internet connection",
                data = mealInfos
            )
        }

    }

    override suspend fun getMealRecipe(mealId: Int): Resource<MealRecipe> {

        return try {
            val response = api.getMealRecipe(id = mealId)
            val result = response.toMealRecipeInfoEntity()
            val mealRecipe = result.toMealRecipe()
            Resource.Success(mealRecipe)
        } catch (e: HttpException) {
            Resource.Error(
                message = if (e.code() == 402) {
                    "Your daily points limit of 150 has been reached."
                } else "Oops! Something went wrong"
            )
        } catch (e: IOException) {
            Resource.Error(
                message = "Couldn't reach server, check your internet connection"
            )
        }
    }

    override suspend fun searchMeals(searchQuery: String): Resource<List<MealInfo>> {
        var mealInfos = listOf<MealInfo>()

        return try {
            val response = api.searchMeals(searchQuery = searchQuery).results
            val mealInfoEntities = response.map { it.toMealInfoEntity() }
            mealInfos = mealInfoEntities.map { it.toMealInfo() }
            Resource.Success(mealInfos)
        } catch (e: HttpException) {
            Resource.Error(
                message = if (e.code() == 402) {
                    "Your daily points limit of 150 has been reached."
                } else "Oops! Something went wrong",
                data = mealInfos
            )
        } catch (e: IOException) {
            Resource.Error(
                message = "Couldn't reach server, check your internet connection",
                data = mealInfos
            )
        }
    }

    override fun getSavedRecipes(): Flow<List<MealRecipeInfoEntity>> {
        return dao.getSavedRecipes()
    }

    override suspend fun insertRecipe(mealRecipeInfoEntity: MealRecipeInfoEntity) {
        return dao.insertRecipe(mealRecipeInfoEntity)
    }

    override suspend fun deleteRecipe(mealRecipeInfoEntity: MealRecipeInfoEntity) {
        return dao.deleteRecipe(mealRecipeInfoEntity)
    }
}