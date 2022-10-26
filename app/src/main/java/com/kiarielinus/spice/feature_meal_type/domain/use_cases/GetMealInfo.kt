package com.kiarielinus.spice.feature_meal_type.domain.use_cases

import com.kiarielinus.spice.core.util.Resource
import com.kiarielinus.spice.feature_meal_type.domain.model.MealInfo
import com.kiarielinus.spice.feature_meal_type.domain.repository.MealInfoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetMealInfo(
    private val repository: MealInfoRepository
) {
    suspend operator fun invoke(mealType: String) : Resource<List<MealInfo>> {
        return repository.getMealInfo(mealType)
    }
}