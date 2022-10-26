package com.kiarielinus.spice.feature_meal_type.di

import android.app.Application
import androidx.room.Room
import com.google.gson.Gson
import com.kiarielinus.spice.R
import com.kiarielinus.spice.core.util.GsonParser
import com.kiarielinus.spice.feature_meal_type.data.local.Converters
import com.kiarielinus.spice.feature_meal_type.data.local.MealInfoDatabase
import com.kiarielinus.spice.feature_meal_type.data.remote.MealsApi
import com.kiarielinus.spice.feature_meal_type.data.repository.MealInfoRepositoryImpl
import com.kiarielinus.spice.feature_meal_type.domain.repository.MealInfoRepository
import com.kiarielinus.spice.feature_meal_type.domain.use_cases.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideMealDatabase(app: Application): MealInfoDatabase {
        return Room.databaseBuilder(
            app,
            MealInfoDatabase::class.java,
            MealInfoDatabase.DB_NAME
        )
            .addTypeConverter(Converters(GsonParser(Gson())))
            .build()
    }

    @Provides
    @Singleton
    fun provideMealsApi(): MealsApi {
        return Retrofit.Builder()
            .baseUrl(MealsApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MealsApi::class.java)
    }

    @Provides
    @Singleton
    fun provideMealInfoRepository(api: MealsApi, db: MealInfoDatabase): MealInfoRepository {
        return MealInfoRepositoryImpl(
            api = api,
            dao = db.dao
        )
    }

    @Provides
    @Singleton
    fun provideUseCases(repository: MealInfoRepository): UseCases {
        return UseCases(
            getMealInfo = GetMealInfo(repository),
            getMealRecipe = GetMealRecipe(repository),
            getSavedRecipes = GetSavedRecipes(repository),
            deleteRecipe = DeleteRecipe(repository),
            insertRecipe = InsertRecipe(repository),
            getSearchedMeals = GetSearchedMeals(repository)
        )
    }
}