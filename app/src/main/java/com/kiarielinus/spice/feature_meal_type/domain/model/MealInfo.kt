package com.kiarielinus.spice.feature_meal_type.domain.model

data class MealInfo(
    val id: Int,
    val imageUrl: String,
    val title: String
){
    companion object{
        fun mock() = MealInfo(
            id = 0,
            imageUrl = "",
            title = ""
        )
    }
}