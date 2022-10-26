package com.kiarielinus.spice.feature_meal_type.data.remote.dto.recipe_dto

data class WinePairing(
    val pairedWines: List<String>,
    val pairingText: String,
    val productMatches: List<ProductMatche>
)