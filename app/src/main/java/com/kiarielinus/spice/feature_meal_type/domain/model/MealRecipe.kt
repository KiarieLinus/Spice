package com.kiarielinus.spice.feature_meal_type.domain.model

import com.kiarielinus.spice.feature_meal_type.data.local.entity.MealRecipeInfoEntity

data class MealRecipe(
    val cookingMinutes: Int,
    val instructions: List<MealInstruction>,
    val nutrients: List<MealNutrient>,
    val ingredients: List<MealIngredient>,
    val credits: String,
    val title: String,
    val id: Int,
    val dishType: List<String>
){
    fun toMealRecipeInfoEntity(): MealRecipeInfoEntity{
        return MealRecipeInfoEntity(
            id = id,
            cookingMinutes = cookingMinutes,
            instructions = instructions,
            nutrients = nutrients,
            ingredients = ingredients,
            credits = credits,
            title = title,
            dishType = dishType
        )
    }
}