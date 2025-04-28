package data

import android.app.Application
import android.util.Log
import androidx.room.Room
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.json.Json
import model.Category
import model.Recipe
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import kotlin.concurrent.Volatile

const val CONTENT_TYPE = "application/Json"
const val BASE_URL = "https://recipes.androidsprint.ru/api/"
const val BASE_IMAGE_URL = "https://recipes.androidsprint.ru/api/images/"

class RecipeRepository(
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
    context: Application,
) {
    val loadImageUrl = BASE_IMAGE_URL
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

    suspend fun getRecipesFromCacheByCategoryId(categoryId: Int): List<Recipe> {
        return recipeDao.getRecipesFromCacheByCategoryId(categoryId)
    }

    suspend fun getRecipeFromCacheById(recipeId: Int): Recipe {
        return recipeDao.getRecipeFromCacheById(recipeId)
    }

    suspend fun getAllRecipesFromCache(): List<Recipe> {
        return recipeDao.getAllRecipesFromCache()
    }

    suspend fun insertRecipe(recipe: Recipe) {
        recipeDao.insertRecipe(recipe)
    }

    suspend fun insertRecipesList(recipesList: List<Recipe>) {
        recipeDao.insertRecipesList(recipesList)
    }

    suspend fun insertCategoriesInDataBase(categories: List<Category>) {
        categoriesDao.insertCategories(categories)
    }

    suspend fun getCategoriesFromCache(): List<Category> {
        return categoriesDao.getAll()
    }

    suspend fun getCategories(): List<Category>? {
        return withContext(dispatcher) {
            try {
                service.getCategories()
            } catch (e: Exception) {
                Log.i("Error fetching categories", e.message ?: "Unknown error")
                null
            }
        }
    }

    suspend fun getRecipesByCategoryId(categoryId: Int): List<Recipe>? {
        return withContext(dispatcher) {
            try {
                service.getRecipesWithCategoryId(categoryId)
            } catch (e: Exception) {
                Log.i("!!!", "${e.message}")
                null
            }
        }
    }

    suspend fun getRecipeById(recipeId: Int): Recipe? {
        return withContext(dispatcher) {
            try {
                service.getRecipeWithId(recipeId)
            } catch (e: Exception) {
                Log.i("!!!", "${e.message}")
                null
            }
        }
    }

    suspend fun getRecipesByIds(ids: String): List<Recipe>? {
        return withContext(dispatcher) {
            try {
                service.getRecipesByIds(ids)
            } catch (e: Exception) {
                Log.i("!!!", "${e.message}")
                null
            }
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: RecipeRepository? = null

        fun getInstance(context: Application): RecipeRepository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: RecipeRepository(context = context).also { INSTANCE = it }
            }
        }
    }
}

