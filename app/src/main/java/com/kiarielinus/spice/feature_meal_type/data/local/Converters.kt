package com.kiarielinus.spice.feature_meal_type.data.local

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.google.gson.reflect.TypeToken
import com.kiarielinus.spice.core.util.JsonParser
import com.kiarielinus.spice.feature_meal_type.domain.model.MealIngredient
import com.kiarielinus.spice.feature_meal_type.domain.model.MealInstruction
import com.kiarielinus.spice.feature_meal_type.domain.model.MealNutrient


@ProvidedTypeConverter
class Converters(
    private val jsonParser: JsonParser
) {
    @TypeConverter
    fun fromInstructionsJson(json: String): List<MealInstruction> {
        return jsonParser.fromJson<ArrayList<MealInstruction>>(
            json,
            object : TypeToken<ArrayList<MealInstruction>>() {}.type
        ) ?: emptyList()
    }

    @TypeConverter
    fun toInstructionsJson(mealInstructions: List<MealInstruction>): String {
        return jsonParser.toJson(
            mealInstructions,
            object : TypeToken<ArrayList<MealInstruction>>() {}.type
        ) ?: "[]"
    }

    @TypeConverter
    fun fromNutrientsJson(json: String): List<MealNutrient> {
        return jsonParser.fromJson<ArrayList<MealNutrient>>(
            json,
            object : TypeToken<ArrayList<MealNutrient>>() {}.type
        ) ?: emptyList()
    }

    @TypeConverter
    fun toNutrientsJson(mealNutrients: List<MealNutrient>): String {
        return jsonParser.toJson(
            mealNutrients,
            object : TypeToken<ArrayList<MealNutrient>>() {}.type
        ) ?: "[]"
    }

    @TypeConverter
    fun fromIngredientsJson(json: String): List<MealIngredient> {
        return jsonParser.fromJson<ArrayList<MealIngredient>>(
            json,
            object : TypeToken<ArrayList<MealIngredient>>() {}.type
        ) ?: emptyList()
    }

    @TypeConverter
    fun toIngredientsJson(mealIngredients: List<MealIngredient>): String {
        return jsonParser.toJson(
            mealIngredients,
            object : TypeToken<ArrayList<MealIngredient>>() {}.type
        ) ?: "[]"
    }

    @TypeConverter
    fun fromDishTypeJson(json: String): List<String> {
        return jsonParser.fromJson<ArrayList<String>>(
            json,
            object : TypeToken<ArrayList<String>>() {}.type
        ) ?: emptyList()
    }

    @TypeConverter
    fun toDishTypeJson(dishType: List<String>): String {
        return jsonParser.toJson(
            dishType,
            object : TypeToken<ArrayList<String>>() {}.type
        ) ?: "[]"
    }
}