package com.kiarielinus.spice.feature_meal_type.presetation.meal_list

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import com.kiarielinus.spice.R
import com.kiarielinus.spice.core.mealTypes
import com.kiarielinus.spice.core.util.Resource
import com.kiarielinus.spice.feature_meal_type.domain.model.MealRecipe
import com.kiarielinus.spice.feature_meal_type.domain.use_cases.UseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MealInfoViewModel @Inject constructor(
    private val useCases: UseCases,
) : ViewModel() {
    private val _state = mutableStateOf(MealInfoState())
    val state: State<MealInfoState> = _state

    private val _savedRecipeState = mutableStateOf(SavedRecipeState())
    val savedRecipeState: State<SavedRecipeState> = _savedRecipeState

    private val _selectedIndex = mutableStateOf(0)
    val selectedIndex: State<Int> = _selectedIndex

    private val _showSavedRecipes = mutableStateOf(false)
    val showSavedRecipes: State<Boolean> = _showSavedRecipes

    private val _savedRecipes =
        mutableStateOf<List<MealRecipe>>(emptyList())
    val savedRecipes: State<List<MealRecipe>> = _savedRecipes

    private val _eventFlow = MutableSharedFlow<UIEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private val _tabState = mutableStateOf(0)
    val tabState: State<Int> = _tabState

    fun getSavedRecipes() {
        useCases.getSavedRecipes().onEach { list ->
            _savedRecipes.value = list.map { it.toMealRecipe() }
        }.launchIn(viewModelScope)
    }

    fun mealTypeSelectedEvent(mealTypeIndex: Int) {
        val mealType = mealTypes[mealTypeIndex]
        _selectedIndex.value = mealTypeIndex

        _state.value = _state.value.copy(
            mealInfoItems = emptyList(),
            isLoading = true
        )

        viewModelScope.launch {
            when (val result = useCases.getMealInfo(mealType)) {
                is Resource.Loading -> {}
                is Resource.Success -> {
                    _state.value = _state.value.copy(
                        mealInfoItems = result.data ?: emptyList(),
                        isLoading = false
                    )
                }
                is Resource.Error -> {
                    _state.value = _state.value.copy(
                        mealInfoItems = result.data ?: emptyList(),
                        isLoading = false
                    )
                    _eventFlow.emit(UIEvent.ShowSnackbar(result.message ?: "Unknown Error"))
                }
            }
        }
    }

    @OptIn(ExperimentalPagerApi::class)
    fun onBottomNavClickEvent(index: Int, pagerState: PagerState) {
        if (index != pagerState.currentPage) {
            viewModelScope.launch {
                pagerState.scrollToPage(index)
            }
        }
    }

    fun changeSavedRecipesVisibility() {
        _showSavedRecipes.value = !_showSavedRecipes.value
        _savedRecipeState.value = _savedRecipeState.value.copy(
            mealRecipe = null,showRecipe = false
        )
    }

    fun getSavedIconId(mealRecipe: MealRecipe): Int {
        val dishTypes = mealRecipe.dishType
        val icon: Int = if (dishTypes.contains(mealTypes[0])) {
            R.drawable.ic_main_dish_icon
        } else if (dishTypes.contains(mealTypes[1])) {
            R.drawable.ic_dessert_icon
        } else if (dishTypes.contains(mealTypes[2])) {
            R.drawable.ic_drink_icon
        } else if (dishTypes.contains(mealTypes[3])) {
            R.drawable.ic_salad_icon
        } else {
            R.drawable.ic_main_dish_icon
        }
        return icon
    }

    fun setSavedRecipeState(mealRecipe: MealRecipe, showRecipe: Boolean) {
        _savedRecipeState.value = _savedRecipeState.value.copy(
            mealRecipe = mealRecipe,
            showRecipe = showRecipe
        )
    }

    fun onTabClicked(index: Int) {
        _tabState.value = index
    }

    sealed class UIEvent {
        data class ShowSnackbar(val message: String) : UIEvent()
    }
}