package com.kiarielinus.spice.feature_meal_type.presetation.meal_search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.SnackbarDefaults.backgroundColor
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.kiarielinus.spice.feature_meal_type.presetation.meal_list.MealsList
import com.kiarielinus.spice.ui.theme.Black
import com.kiarielinus.spice.ui.theme.ghost_white
import kotlinx.coroutines.flow.collectLatest

@Composable
fun MealsSearchScreen(
    navController: NavController,
    viewModel: MealSearchViewModel = hiltViewModel()
) {
    val scaffoldState = rememberScaffoldState()

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is MealSearchViewModel.UIEvent.ShowSnackbar -> {
                    scaffoldState.snackbarHostState.showSnackbar(
                        event.message
                    )
                }
            }
        }
    }



    Surface(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            topBar = {
                var mealQuery by remember { mutableStateOf("") }
                TopBar(
                    mealQuery = mealQuery,
                    onMealQueryChanged = {
                        mealQuery = it
                        viewModel.updateMealQuery(it)
                    },
                    onKeyboardSearch = { viewModel.searchTypedMeal() }
                )
            },
            scaffoldState = scaffoldState
        ) {
            Box(
                modifier = Modifier
                    .background(colors.primary)
                    .fillMaxSize()
            ) {
                val loadingModifier = Modifier
                    .size(120.dp)
                    .align(Alignment.Center)

                MealsList(
                    navController = navController,
                    loadingModifier = loadingModifier,
                    mealItems = viewModel.state.value.mealInfoItems,
                    isLoading = viewModel.state.value.isLoading
                )
            }
        }
    }
}

@Composable
private fun TopBar(
    mealQuery: String,
    onMealQueryChanged: (String) -> Unit,
    onKeyboardSearch: () -> Unit
) {
    TopAppBar(
        elevation = 4.dp,
        modifier = Modifier.fillMaxWidth(),
        title = {
            Box {
                OutlinedTextField(
                    value = mealQuery,
                    onValueChange = onMealQueryChanged,
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                        .padding(horizontal = 2.dp, vertical = 2.dp),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Search
                    ),
                    keyboardActions = KeyboardActions(
                        onSearch = { onKeyboardSearch() }
                    ),
                    textStyle = LocalTextStyle.current.copy(fontSize = 14.sp),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        textColor = ghost_white,
                        cursorColor = ghost_white,
                        backgroundColor = Black.copy(0.4f)
                    )
                )
            }
        },
        backgroundColor = colors.primary
    )
}

@Preview
@Composable
fun TopBarPreview() {
}
