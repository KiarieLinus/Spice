package com.kiarielinus.spice.feature_meal_type.data.local.entity


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kiarielinus.spice.feature_meal_type.domain.model.MealIngredient
import com.kiarielinus.spice.feature_meal_type.domain.model.MealInstruction
import com.kiarielinus.spice.feature_meal_type.domain.model.MealNutrient
import com.kiarielinus.spice.feature_meal_type.domain.model.MealRecipe

@Entity
data class MealRecipeInfoEntity(
    @PrimaryKey val id: Int,
    val dishType: List<String>,
    val cookingMinutes: Int,
    val instructions: List<MealInstruction>,
    val nutrients: List<MealNutrient>,
    val ingredients: List<MealIngredient>,
    val credits: String,
    val title: String
) {
    fun toMealRecipe():MealRecipe{
        return MealRecipe(
            id = id,
            dishType = dishType,
            title = title ,
            credits = credits,
            cookingMinutes = cookingMinutes,
            instructions = instructions,
            nutrients = nutrients,
            ingredients = ingredients
        )
    }
}
