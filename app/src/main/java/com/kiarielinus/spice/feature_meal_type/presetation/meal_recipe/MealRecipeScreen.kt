package com.kiarielinus.spice.feature_meal_type.presetation.meal_recipe

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterStart
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.kiarielinus.spice.R
import com.kiarielinus.spice.core.MOCK_TITLE
import com.kiarielinus.spice.core.util.Resource
import com.kiarielinus.spice.core.util.toCamelCase
import com.kiarielinus.spice.feature_meal_type.domain.model.MealIngredient
import com.kiarielinus.spice.feature_meal_type.domain.model.MealInstructionStep
import com.kiarielinus.spice.feature_meal_type.domain.model.MealNutrient
import com.kiarielinus.spice.feature_meal_type.domain.model.MealRecipe
import com.kiarielinus.spice.ui.theme.Josefin
import com.kiarielinus.spice.ui.theme.Purple200
import com.kiarielinus.spice.ui.theme.Purple500
import com.kiarielinus.spice.ui.theme.ghost_white
import java.util.*

@Composable
fun MealRecipeScreen(
    mealId: Int,
    navController: NavController,
    viewModel: MealRecipeViewModel = hiltViewModel(),
) {

    val mealRecipeInfo =
        produceState<Resource<MealRecipe>>(initialValue = Resource.Loading()) {
            value = viewModel.getMealRecipe(mealId)
        }.value

    viewModel.setMealRecipeState(mealRecipeInfo)
    val isSaved by remember { viewModel.isSaved }
    val scaffoldState = rememberScaffoldState()
    val tabState = viewModel.tabState.value

    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Scaffold(
            topBar = {
                TopBar(
                    navController = navController,
                    isSaved = isSaved
                ) { viewModel.saveMealRecipe() }
            },
            scaffoldState = scaffoldState
        ) {
            Surface(
                color = MaterialTheme.colors.primary
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            color = ghost_white,
                            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                        )
                        .clip(RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp))
                ) {
                    if (mealRecipeInfo is Resource.Loading) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .align(Center),
                            color = ghost_white
                        )
                    }
                    if (mealRecipeInfo is Resource.Success) {
                        val data = mealRecipeInfo.data!!


                        Column {
                            val title = data.title
                            val cookingMinutes = data.cookingMinutes
                            val nutrients = data.nutrients

                            TitlePane(title, cookingMinutes)
                            DetailsPane(
                                tabState = tabState,
                                ingredients = viewModel.detailState.value.ingredients,
                                instructions = viewModel.detailState.value.instructions.first().steps,
                                nutrients = nutrients,
                                onTabClick = {index ->
                                    viewModel.tabClickedEvent(index)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DetailsPane(
    tabState: Int,
    ingredients: List<MealIngredient>,
    instructions: List<MealInstructionStep>,
    nutrients: List<MealNutrient>,
    onTabClick: (index: Int) -> Unit
) {
    Box(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxSize()
    ) {
        val scrollState = rememberLazyListState()
        LazyColumn(state = scrollState) {
            item {
                Column {
                    NutrientsPane(nutrients = nutrients)
                    DetailsTabRow(tabState = tabState) {onTabClick(it)}
                }
            }
            when (tabState) {
                0 -> {
                    itemsIndexed(
                        items = ingredients
                    ) { index, ingredient ->
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp),
                            horizontalArrangement = Arrangement.Start
                        ) {
                            Text(
                                text = "${index + 1}.",
                                color = MaterialTheme.colors.primary,
                                modifier = Modifier
                                    .weight(1f)
                            )
                            Text(
                                text = ingredient.name + " (${ingredient.amount} ${ingredient.unit})",
                                color = MaterialTheme.colors.primary,
                                modifier = Modifier.weight(8f)
                            )
                        }
                    }
                }
                1 -> {
                    itemsIndexed(
                        items = instructions
                    ) { index, instruction ->
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp),
                            horizontalArrangement = Arrangement.Start
                        ) {
                            Text(
                                text = "${index + 1}.",
                                color = MaterialTheme.colors.primary,
                                modifier = Modifier
                                    .weight(1f)
                            )
                            Text(
                                text = instruction.step,
                                color = MaterialTheme.colors.primary,
                                modifier = Modifier.weight(8f)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DetailsTabRow(
    tabState: Int,
    onTabClick: (index: Int) -> Unit
) {
    val titles = listOf("INGREDIENTS", "INSTRUCTIONS")
    TabRow(
        selectedTabIndex = tabState,
        backgroundColor = Purple500,
        modifier = Modifier
            .padding(vertical = 4.dp, horizontal = 8.dp)
            .clip(RoundedCornerShape(50)),
        indicator = { Box {} }
    ) {
        titles.forEachIndexed { index, title ->
            CustomDetailTab(
                text = title,
                isSelected = tabState == index,
                onTabClick = { onTabClick(index) }
            )
        }
    }
}

@Composable
fun CustomDetailTab(
    text: String,
    isSelected: Boolean,
    onTabClick: () -> Unit,
) {
    Tab(
        modifier = if (isSelected) Modifier
            .clip(RoundedCornerShape(50))
            .background(
                Color.White
            )
        else Modifier
            .clip(RoundedCornerShape(50))
            .background(Purple500),
        selected = isSelected,
        onClick = { onTabClick() },
        text = { Text(text = text, color = Purple200) }
    )
}

@Composable
fun TitlePane(title: String, cookingMinutes: Int) {
    val hasCookingMinutes = cookingMinutes != -1
    Box {
        Row(
            verticalAlignment = CenterVertically,
            modifier = Modifier.padding(8.dp)
        ) {
            Box(modifier = Modifier.weight(4F)) {
                Text(
                    text = toCamelCase(title.lowercase(Locale.ENGLISH)),
                    color = MaterialTheme.colors.primary,
                    style = MaterialTheme.typography.body1,
                    modifier = Modifier
                        .align(
                            if (hasCookingMinutes) CenterStart else Center
                        )
                )
            }

            if (hasCookingMinutes) {
                Row(
                    modifier = Modifier
                        .weight(1F),
                    verticalAlignment = CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Icon(
                        imageVector = Icons.Default.AccessTime,
                        contentDescription = "cooking minutes icon"
                    )

                    Text(
                        text = "$cookingMinutes Min",
                        fontFamily = Josefin,
                        fontWeight = FontWeight.Light,
                        fontSize = 12.sp,
                        color = MaterialTheme.colors.primary
                    )
                }
            }
        }
    }
}

@Composable
private fun TopBar(
    navController: NavController,
    isSaved: Boolean,
    onSaveClicked: () -> Unit,
) {
    TopAppBar(
        elevation = 0.dp,
        modifier = Modifier.fillMaxWidth(),
        navigationIcon = {
            Icon(
                imageVector = Icons.Outlined.Close,
                contentDescription = "Navigate Back",
                tint = Color.White,
                modifier = Modifier
                    .clickable { navController.popBackStack() },
            )
        },
        title = {
            Box(modifier = Modifier.fillMaxWidth()) {

                Text(
                    text = "Recipe",
                    modifier = Modifier.align(Center),
                    color = Color.White,
                    style = TextStyle(
                        fontStyle = FontStyle.Italic,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = Josefin,
                        fontSize = 22.sp
                    )
                )
            }
        },
        actions = {
            Icon(
                imageVector = Icons.Outlined.Favorite,
                contentDescription = "save recipe",
                tint = if (isSaved) Color.Red else Color.White,
                modifier = Modifier
                    .clickable { onSaveClicked() },
            )
        },
        backgroundColor = MaterialTheme.colors.primary,
    )
}

@Composable
fun NutrientEntry(name: String, amount: Double, unit: String, iconRes: Int) {
    Row {
        Icon(
            painter = painterResource(iconRes),
            contentDescription = "nutrient_icon",
            tint = Color.Black,
            modifier = Modifier
                .size(48.dp)
                .padding(8.dp)


        )
        Text(
            text = "$amount$unit $name",
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            fontFamily = Josefin,
            color = MaterialTheme.colors.primary,
            modifier = Modifier.align(CenterVertically)
        )
    }
}

@Composable
fun NutrientsPane(
    nutrients: List<MealNutrient>,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = CenterVertically
    ) {
        Column {
            NutrientEntry(
                name = "",
                amount = nutrients[0].amount,
                unit = nutrients[0].unit,
                iconRes = R.drawable.ic_nutrient_calories
            )
            NutrientEntry(
                name = "fats",
                amount = nutrients[1].amount,
                unit = nutrients[1].unit,
                iconRes = R.drawable.ic_nutrient_fat
            )
        }
        Column {
            NutrientEntry(
                name = "carbs",
                amount = nutrients[2].amount,
                unit = nutrients[2].unit,
                iconRes = R.drawable.ic_nutrient_carbs
            )
            NutrientEntry(
                name = "proteins",
                amount = nutrients[3].amount,
                unit = nutrients[3].unit,
                iconRes = R.drawable.ic_nutrient_protein
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NutrientEntryPreview() {
    NutrientEntry("protein", 35.4, "g", R.drawable.ic_nutrient_protein)
}

@Preview
@Composable
fun TopBarPreview(
    navController: NavController = NavController(LocalContext.current),
) {
    TopBar(navController = navController, true) {}
}

@Preview(showBackground = true)
@Composable
fun TitlePanePreview() {
    TitlePane(title = MOCK_TITLE, cookingMinutes = 1)
}