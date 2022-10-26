package com.kiarielinus.spice.feature_meal_type.data.remote.dto.recipe_dto

import com.kiarielinus.spice.feature_meal_type.domain.model.MealNutrition

data class Nutrition(
    val caloricBreakdown: CaloricBreakdown,
    val flavonoids: List<Flavonoid>,
    val ingredients: List<IngredientX>,
    val nutrients: List<NutrientX>,
    val properties: List<Property>,
    val weightPerServing: WeightPerServing
){
    fun toMealNutrition():MealNutrition{
        return  MealNutrition(
            nutrients = nutrients.map { it.toMealNutrients() }
        )
    }
}