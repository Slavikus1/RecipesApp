package di

import android.content.Context
import androidx.room.Room
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import data.AppDatabase
import data.BASE_URL
import data.CONTENT_TYPE
import data.RecipeApiService
import data.RecipeRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

class AppContainer(context: Context) {
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
    private val interceptor =
        HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
    private val client = OkHttpClient.Builder()
        .addInterceptor(interceptor)
        .build()
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(Json.asConverterFactory(CONTENT_TYPE.toMediaType()))
        .build()
    private val service: RecipeApiService = retrofit.create(RecipeApiService::class.java)

    private val database: AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "database-categories")
            .fallbackToDestructiveMigration(false)
            .build()

    private val categoriesDao = database.categoriesDao()

    private val recipeDao = database.recipesDao()


    val repository = RecipeRepository(
        dispatcher = dispatcher,
        categoriesDao = categoriesDao,
        recipeDao = recipeDao,
        service = service,
    )

    val categoriesListViewModelFactory = CategoryListViewModelFactory(repository)
    val favouritesListViewModelFactory = FavouritesListViewModelFactory(repository)
    val recipeViewModelFactory = RecipeViewModelFactory(repository)
    val recipesListViewModelFactory = RecipesListViewModelFactory(repository)
}