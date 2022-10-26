package com.kiarielinus.spice.feature_meal_type.presetation.meal_recipe

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kiarielinus.spice.core.util.Resource
import com.kiarielinus.spice.feature_meal_type.domain.model.MealRecipe
import com.kiarielinus.spice.feature_meal_type.domain.use_cases.UseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MealRecipeViewModel @Inject constructor(
    private val useCases: UseCases,
) : ViewModel() {
    private val _detailState = mutableStateOf(MealRecipeDetailState())
    val detailState: State<MealRecipeDetailState> = _detailState

    private val _tabState = mutableStateOf(0)
    val tabState: State<Int> = _tabState

    private val _isSaved = mutableStateOf(false)
    val isSaved: State<Boolean> = _isSaved

    suspend fun getMealRecipe(mealId: Int): Resource<MealRecipe> {
        return useCases.getMealRecipe(mealId)
    }

    private var mealRecipe: MealRecipe? = null
    fun setMealRecipeState(mealRecipeInfo: Resource<MealRecipe>) {
        when (mealRecipeInfo) {
            is Resource.Error -> {}
            is Resource.Loading -> {}
            is Resource.Success -> {
                mealRecipe = mealRecipeInfo.data
                mealRecipe?.let {
                    _detailState.value = _detailState.value.copy(
                        ingredients = it.ingredients,
                        instructions = it.instructions
                    )
                }
                getSavedState()
            }
        }

    }

    fun tabClickedEvent(index: Int) {
        _tabState.value = index
    }

    fun saveMealRecipe() {
        mealRecipe?.let {
            val mealRecipeInfoEntity = it.toMealRecipeInfoEntity()
            viewModelScope.launch { useCases.insertRecipe(mealRecipeInfoEntity) }
            _isSaved.value = true
        }
    }

    private fun getSavedState() {
       useCases.getSavedRecipes().onEach { savedRecipes->
            _isSaved.value = savedRecipes.any { it.id == mealRecipe!!.id }
        }.launchIn(viewModelScope)
    }
}