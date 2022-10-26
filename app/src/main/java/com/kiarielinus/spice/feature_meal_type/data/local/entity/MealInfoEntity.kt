package com.kiarielinus.spice.feature_meal_type.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kiarielinus.spice.feature_meal_type.domain.model.MealInfo

@Entity
data class MealInfoEntity(
    @PrimaryKey val id: Int,
    val image: String,
    val title: String
){
    fun toMealInfo(): MealInfo {
        return MealInfo(
            id = id,
            imageUrl = image,
            title = title
        )
    }
}