package com.kiarielinus.spice.feature_meal_type.presetation.meal_list

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kiarielinus.spice.core.MAX_LINES_TITLE
import com.kiarielinus.spice.core.util.toCamelCase
import com.kiarielinus.spice.feature_meal_type.domain.model.MealRecipe
import com.kiarielinus.spice.feature_meal_type.presetation.meal_recipe.DetailsPane
import com.kiarielinus.spice.feature_meal_type.presetation.meal_recipe.DetailsTabRow
import com.kiarielinus.spice.feature_meal_type.presetation.meal_recipe.NutrientsPane
import com.kiarielinus.spice.feature_meal_type.presetation.meal_recipe.TitlePane
import com.kiarielinus.spice.ui.theme.Josefin
import kotlinx.coroutines.launch
import java.util.*

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SavedRecipesScreen(
    savedRecipes: List<MealRecipe>,
    getIconId: (MealRecipe) -> Int,
    navClicked: () -> Unit,
    savedItemClicked: (MealRecipe, Boolean) -> Unit,
    state: SavedRecipeState,
    tabState: Int,
    onTabClicked: (index: Int) -> Unit
) {
    val showRecipe = state.showRecipe
    val backdropState = rememberBackdropScaffoldState(initialValue = BackdropValue.Concealed)

    LaunchedEffect(backdropState) {
        backdropState.reveal()
    }

    val scope = rememberCoroutineScope()

    BackdropScaffold(
        appBar = { TopBar { navClicked() } },
        backLayerContent = {
            val scrollState = rememberLazyListState()

            LazyColumn(state = scrollState) {
                items(
                    items = savedRecipes,
                    key = { it.id }
                ) { mealRecipe ->
                    SavedListItem(
                        getIconId = { getIconId(mealRecipe) },
                        mealRecipe = mealRecipe,
                        modifier = Modifier.clickable {
                            scope.launch {
                                backdropState.conceal()
                            }
                            savedItemClicked(mealRecipe, true)
                        }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        },
        peekHeight = (LocalConfiguration.current.screenHeightDp * 0.25).dp,
        frontLayerContent = {
            SavedMealRecipe(
                mealRecipe = state.mealRecipe,
                showRecipe = showRecipe,
                tabState = tabState,
                onTabClicked = onTabClicked
            )
        },
        scaffoldState = backdropState
    )
}

@Composable
private fun SavedListItem(
    modifier: Modifier = Modifier,
    getIconId: () -> Int,
    mealRecipe: MealRecipe
) {
    Card(
        shape = RoundedCornerShape(10.dp),
        modifier = modifier.padding(start = 8.dp, end = 8.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Image(
                painter = painterResource(id = getIconId()),
                contentDescription = mealRecipe.title,
                modifier = Modifier
                    .size(width = 120.dp, height = 89.dp)
            )

            Text(
                text = toCamelCase(mealRecipe.title.lowercase(Locale.ENGLISH)),
                style = MaterialTheme.typography.body1,
                color = MaterialTheme.colors.primary,
                maxLines = MAX_LINES_TITLE,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(start = 8.dp)
            )
        }
    }
}

@Composable
private fun TopBar(
    navClicked: () -> Unit
) {
    TopAppBar(
        navigationIcon = {
            Icon(
                imageVector = Icons.Outlined.Close,
                contentDescription = "Navigate Back",
                tint = Color.White,
                modifier = Modifier
                    .clickable { navClicked() },
            )
        },
        title = {
            Text(
                text = "Favorites", color = Color.White,
                style = TextStyle(
                    fontStyle = FontStyle.Italic,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = Josefin,
                    fontSize = 22.sp
                )
            )
        },
        backgroundColor = MaterialTheme.colors.primary
    )
}

@Composable
fun SavedMealRecipe(
    mealRecipe: MealRecipe?,
    showRecipe: Boolean,
    tabState: Int,
    onTabClicked: (index: Int) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        when (showRecipe) {
            false -> Text(
                text = "Click on a Recipe to View It",
                style = MaterialTheme.typography.body1,
                color = MaterialTheme.colors.primary
            )

            true -> {
                TitlePane(title = mealRecipe!!.title, cookingMinutes = mealRecipe.cookingMinutes)
                DetailsPane(
                    tabState = tabState,
                    ingredients = mealRecipe.ingredients,
                    instructions = mealRecipe.instructions.first().steps,
                    nutrients = mealRecipe.nutrients,
                    onTabClick = {onTabClicked(it)}
                )
            }
        }
    }
}