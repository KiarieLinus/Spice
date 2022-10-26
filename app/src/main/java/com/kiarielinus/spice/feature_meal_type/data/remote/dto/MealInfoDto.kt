package com.kiarielinus.spice.feature_meal_type.data.remote.dto

import com.kiarielinus.spice.feature_meal_type.data.local.entity.MealInfoEntity

data class MealInfoDto(
    val id: Int,
    val image: String,
    val imageType: String,
    val title: String
){
    fun toMealInfoEntity(): MealInfoEntity {
        return MealInfoEntity(
            id = id,
            image = image,
            title = title
        )
    }
}