package com.kiarielinus.spice.feature_meal_type.data.remote

import com.kiarielinus.spice.BuildConfig
import com.kiarielinus.spice.feature_meal_type.data.remote.dto.MealsDto
import com.kiarielinus.spice.feature_meal_type.data.remote.dto.recipe_dto.MealRecipeDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MealsApi {

    @GET("recipes/complexSearch?instructionsRequired=true")
    suspend fun getMeals(
        @Query("apiKey") apiKey: String = BuildConfig.API_KEY,
        @Query("type") mealType: String,
    ): MealsDto

    @GET("recipes/{id}/information?includeNutrition=true")
    suspend fun getMealRecipe(
        @Path("id") id: Int,
        @Query("apiKey") apiKey: String = BuildConfig.API_KEY
    ): MealRecipeDto

    @GET("recipes/complexSearch?instructionsRequired=true")
    suspend fun searchMeals(
        @Query("query") searchQuery: String,
        @Query("apiKey") apiKey: String = BuildConfig.API_KEY
    ): MealsDto

    companion object {
        const val BASE_URL = "https://api.spoonacular.com/"
    }
}