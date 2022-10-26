package com.kiarielinus.spice.feature_meal_type.domain.use_cases

data class UseCases(
    val getMealInfo: GetMealInfo,
    val getMealRecipe: GetMealRecipe,
    val getSavedRecipes: GetSavedRecipes,
    val deleteRecipe: DeleteRecipe,
    val insertRecipe: InsertRecipe,
    val getSearchedMeals: GetSearchedMeals
)