package com.kiarielinus.spice.feature_meal_type.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.kiarielinus.spice.feature_meal_type.data.local.entity.MealInfoEntity
import com.kiarielinus.spice.feature_meal_type.data.local.entity.MealRecipeInfoEntity

@Database(
    entities = [MealInfoEntity::class, MealRecipeInfoEntity::class],
    version = 3,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class MealInfoDatabase:RoomDatabase() {

    abstract val dao: MealInfoDao

    companion object{
        const val DB_NAME = "meal_db"
    }
}