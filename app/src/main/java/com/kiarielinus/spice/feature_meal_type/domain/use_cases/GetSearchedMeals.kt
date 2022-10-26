package com.kiarielinus.spice.feature_meal_type.domain.use_cases

import com.kiarielinus.spice.core.util.Resource
import com.kiarielinus.spice.feature_meal_type.domain.model.MealInfo
import com.kiarielinus.spice.feature_meal_type.domain.repository.MealInfoRepository

class GetSearchedMeals(
    private val repository: MealInfoRepository
) {
    suspend operator fun invoke(searchQuery: String): Resource<List<MealInfo>>{
        return repository.searchMeals(searchQuery)
    }
}