package com.kiarielinus.spice.feature_meal_type.presetation.meal_list

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import com.kiarielinus.spice.R
import com.kiarielinus.spice.core.Destination
import com.kiarielinus.spice.core.MAX_LINES_TITLE
import com.kiarielinus.spice.core.MOCK_TITLE
import com.kiarielinus.spice.core.mealTypes
import com.kiarielinus.spice.core.util.toCamelCase
import com.kiarielinus.spice.feature_meal_type.domain.model.MealInfo
import com.kiarielinus.spice.ui.theme.Josefin
import com.kiarielinus.spice.ui.theme.ghost_white
import kotlinx.coroutines.flow.collectLatest
import java.util.*

@OptIn(ExperimentalPagerApi::class)
@Composable
fun MealListScreen(
    navController: NavController,
    viewModel: MealInfoViewModel = hiltViewModel(),
) {
    val selectedIndex = viewModel.selectedIndex.value
    val scaffoldState = rememberScaffoldState()
    val pagerState: PagerState = rememberPagerState()
    val showSavedRecipes = viewModel.showSavedRecipes.value

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is MealInfoViewModel.UIEvent.ShowSnackbar -> {
                    scaffoldState.snackbarHostState.showSnackbar(
                        event.message
                    )
                }
            }
        }
    }

    Surface(modifier = Modifier.fillMaxSize()) {
        when (showSavedRecipes) {
            true -> SavedRecipesScreen(
                savedRecipes = viewModel.savedRecipes.value,
                getIconId = { viewModel.getSavedIconId(it) },
                navClicked = { viewModel.changeSavedRecipesVisibility() },
                savedItemClicked = { mealRecipe, showRecipe ->
                    viewModel.setSavedRecipeState(mealRecipe, showRecipe)
                },
                state = viewModel.savedRecipeState.value,
                tabState = viewModel.tabState.value,
                onTabClicked = { index ->
                    viewModel.onTabClicked(index)
                }
            )
            false -> {
                Scaffold(
                    topBar = {
                        TopBar(
                            { navController.navigate(route = Destination.Search) },
                            {
                                viewModel.changeSavedRecipesVisibility()
                                viewModel.getSavedRecipes()
                            }
                        )
                    },
                    bottomBar = {
                        BottomBar(selectedIndex = selectedIndex, pagerState = pagerState)
                    },
                    scaffoldState = scaffoldState
                ) {
                    Box(
                        modifier = Modifier
                            .padding(bottom = 56.dp)
                            .fillMaxSize()
                            .background(color = MaterialTheme.colors.primary)
                    ) {
                        val loadingModifier = Modifier
                            .size(120.dp)
                            .align(Center)

                        LaunchedEffect(selectedIndex) {
                            snapshotFlow { pagerState.currentPage }.collect { mealTypeIndex ->
                                viewModel.mealTypeSelectedEvent(mealTypeIndex)
                            }
                        }
                        HorizontalPager(
                            count = mealTypes.size,
                            state = pagerState
                        ) {
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
        }
    }
}

@Composable
fun MealsList(
    navController: NavController,
    loadingModifier: Modifier,
    mealItems: List<MealInfo>,
    isLoading: Boolean
) {
    if (isLoading && mealItems.isEmpty()) {
        CircularProgressIndicator(
            modifier = loadingModifier,
            color = ghost_white
        )
    }

    val scrollState = rememberLazyListState()

    LazyColumn(state = scrollState) {
        items(
            count = mealItems.size,
            key = {
                mealItems[it].id
            }
        ) { index ->
            val mealInfo = mealItems[index]
            MealListItem(
                index = index,
                mealInfo = mealInfo,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        navController.navigate(
                            route = Destination.Recipe + "?mealId=${mealInfo.id}"
                        )
                    }
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun MealListItem(
    modifier: Modifier = Modifier,
    mealInfo: MealInfo,
    index: Int,
) {
    if (index == 0) {
        Spacer(modifier = Modifier.height(8.dp))
    }
    Card(
        shape = RoundedCornerShape(10.dp),
        modifier = modifier.padding(start = 8.dp, end = 8.dp)
    ) {
        val painter = rememberAsyncImagePainter(
            model = ImageRequest.Builder(LocalContext.current)
                .data(mealInfo.imageUrl)
                .crossfade(false)
                .build()
        )
        Row {
            val state = painter.state
            val title by remember { mutableStateOf(mealInfo.title) }
            if (state is AsyncImagePainter.State.Loading) {
                CircularProgressIndicator(
                    color = MaterialTheme.colors.primary,
                    modifier = Modifier
                        .align(CenterVertically)
                        .padding(start = 30.dp)
                )
            }
            Image(
                painter = painter,
                contentDescription = title,
                modifier = Modifier
                    .size(width = 120.dp, height = 89.dp)
            )

            Text(
                text = toCamelCase(title.lowercase(Locale.ENGLISH)),
                style = MaterialTheme.typography.body1,
                color = MaterialTheme.colors.primary,
                maxLines = MAX_LINES_TITLE,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .align(CenterVertically)
                    .padding(start = 8.dp)
            )
        }
    }
}


@Composable
private fun TopBar(
    onSearch: () -> Unit,
    onFavoriteClicked: () -> Unit
) {
    TopAppBar(
        elevation = 0.dp,
        modifier = Modifier.fillMaxWidth(),
        title = {
            Box(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Spice",
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
            IconButton(
                onClick = { onSearch() }
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "meal search",
                    tint = Color.White
                )
            }
            IconButton(
                onClick = { onFavoriteClicked() }
            ) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "to favorite screen",
                    tint = Color.White
                )
            }
        },
        backgroundColor = MaterialTheme.colors.primary,
    )
}


@OptIn(ExperimentalPagerApi::class)
@Composable
private fun BottomBar(
    selectedIndex: Int,
    viewModel: MealInfoViewModel = hiltViewModel(),
    pagerState: PagerState,
) {
    val bottomBarItems = listOf(
        "Entrée",
        "Dessert",
        "Drinks",
        "Salad"
    )


    BottomNavigation(backgroundColor = MaterialTheme.colors.primary) {
        bottomBarItems.forEachIndexed { index, label ->
            val isSelected = selectedIndex == index
            BottomNavigationItem(
                selected = isSelected,
                label = {
                    Text(
                        text = label,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = Josefin,
                    )
                },
                icon = {
                    when (index) {
                        0 -> {
                            BottomBarIcon(
                                iconRes = R.drawable.ic_main_dish_icon
                            )
                        }
                        1 -> {
                            BottomBarIcon(
                                iconRes = R.drawable.ic_dessert_icon
                            )
                        }
                        2 -> {
                            BottomBarIcon(
                                iconRes = R.drawable.ic_drink_icon
                            )
                        }
                        3 -> {
                            BottomBarIcon(
                                iconRes = R.drawable.ic_salad_icon
                            )
                        }
                    }
                },
                onClick = { viewModel.onBottomNavClickEvent(index, pagerState) },
                alwaysShowLabel = false,
                selectedContentColor = Color.White,
                unselectedContentColor = Color.White.copy(alpha = 0.6f)
            )
        }
    }
}

@Composable
private fun BottomBarIcon(
    iconRes: Int,
) {
    Icon(
        painter = painterResource(iconRes),
        contentDescription = "selected_icon"
    )
}


@Preview
@Composable
fun ListItemPreview() {
    MealListItem(
        mealInfo = MealInfo.mock().copy(
            title = MOCK_TITLE
        ),
        index = 1
    )
}

@Preview(showBackground = true)
@Composable
fun TopBarPreview() {
    TopBar({}, {})
}

