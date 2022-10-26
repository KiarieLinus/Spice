package com.kiarielinus.spice.feature_meal_type.data.remote.dto.recipe_dto

import com.kiarielinus.spice.core.nutrients
import com.kiarielinus.spice.feature_meal_type.data.local.entity.MealRecipeInfoEntity

data class MealRecipeDto(
    val aggregateLikes: Int,
    val analyzedInstructions: List<AnalyzedInstruction>,
    val cheap: Boolean,
    val cookingMinutes: Int,
    val creditsText: String,
    val cuisines: List<String>,
    val dairyFree: Boolean,
    val diets: List<String>,
    val dishTypes: List<String>,
    val extendedIngredients: List<ExtendedIngredient>,
    val gaps: String,
    val glutenFree: Boolean,
    val healthScore: Int,
    val id: Int,
    val image: String,
    val imageType: String,
    val instructions: String,
    val lowFodmap: Boolean,
    val nutrition: Nutrition,
    val occasions: List<Any>,
    val originalId: Any,
    val preparationMinutes: Int,
    val pricePerServing: Double,
    val readyInMinutes: Int,
    val servings: Int,
    val sourceName: String,
    val sourceUrl: String,
    val summary: String,
    val sustainable: Boolean,
    val title: String,
    val vegan: Boolean,
    val vegetarian: Boolean,
    val veryHealthy: Boolean,
    val veryPopular: Boolean,
    val weightWatcherSmartPoints: Int,
    val winePairing: WinePairing,
) {
    fun toMealRecipeInfoEntity(): MealRecipeInfoEntity {
        return MealRecipeInfoEntity(
            id = id,
            dishType = dishTypes,
            title = title,
            credits = creditsText,
            cookingMinutes = cookingMinutes,
            instructions = analyzedInstructions.map { it.toMealInstruction() }, //map
            ingredients = extendedIngredients.map { it.toMealIngredient() },
            nutrients = nutrition.toMealNutrition().nutrients.filter { nutrients.contains(it.name) } //map
        )
    }
}