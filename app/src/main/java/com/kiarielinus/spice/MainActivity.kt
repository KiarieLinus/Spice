package com.kiarielinus.spice

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.kiarielinus.spice.core.Destination
import com.kiarielinus.spice.feature_meal_type.presetation.meal_list.MealListScreen
import com.kiarielinus.spice.feature_meal_type.presetation.meal_recipe.MealRecipeScreen
import com.kiarielinus.spice.feature_meal_type.presetation.meal_search.MealsSearchScreen
import com.kiarielinus.spice.ui.theme.SpiceTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SpiceTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = Destination.Meals
                ) {
                    composable(route = Destination.Meals) {
                        MealListScreen(navController = navController)
                    }
                    composable(
                        route = Destination.Recipe
                                + "?mealId={mealId}"
                        , arguments = listOf(
                            navArgument(
                                name = "mealId"
                            ) {
                                type = NavType.IntType
                                defaultValue = -1
                            }
                        )
                    ) {
                        val mealId = it.arguments?.getInt("mealId")?: -1
                        MealRecipeScreen(
                            navController = navController,
                            mealId = mealId
                        )
                    }
                    composable(
                        route = Destination.Search
                    ){
                        MealsSearchScreen(navController = navController)
                    }
                }
            }
        }
    }
}

