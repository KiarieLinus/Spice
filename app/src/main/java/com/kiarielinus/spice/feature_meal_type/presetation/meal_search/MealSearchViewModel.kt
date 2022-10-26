package com.kiarielinus.spice.feature_meal_type.presetation.meal_search

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kiarielinus.spice.core.util.Resource
import com.kiarielinus.spice.feature_meal_type.domain.use_cases.UseCases
import com.kiarielinus.spice.feature_meal_type.presetation.meal_list.MealInfoState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MealSearchViewModel @Inject constructor(
    private val useCases: UseCases
) : ViewModel() {

    private val _state = mutableStateOf(MealInfoState())
    val state: State<MealInfoState> = _state

    private val _eventFlow = MutableSharedFlow<UIEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    var mealQuery = ""

    fun updateMealQuery(searchedMeal: String) {
        searchedMeal.also { mealQuery = it }
    }

    fun searchTypedMeal() {
        if(mealQuery.isNotEmpty()){

            _state.value = _state.value.copy(
                mealInfoItems = emptyList(),
                isLoading = true
            )

            viewModelScope.launch {
                when(val result = useCases.getSearchedMeals(mealQuery)){
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
    }

    sealed class UIEvent {
        data class ShowSnackbar(val message: String) : UIEvent()
    }
}