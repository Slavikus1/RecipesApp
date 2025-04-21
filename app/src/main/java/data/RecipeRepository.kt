package data

import android.util.Log
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

const val CONTENT_TYPE = "application/Json"
const val BASE_URL = "https://recipes.androidsprint.ru/api/"
const val BASE_IMAGE_URL = "https://recipes.androidsprint.ru/api/images/"

class RecipeRepository(private val dispatcher: CoroutineDispatcher = Dispatchers.IO) {
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

    suspend fun getCategories(): List<Category>? {
        return withContext(dispatcher) {
            try {
                val response = service.getCategories()
                response
            } catch (e: Exception) {
                Log.i("Error fetching categories", e.message ?: "Unknown error")
                null
            }
        }
    }

    suspend fun getRecipesByCategoryId(categoryId: Int): List<Recipe>? {
        return withContext(dispatcher) {
            try {
                val response = service.getRecipesWithCategoryId(categoryId)
                response
            } catch (e: Exception) {
                Log.i("!!!", "${e.message}")
                null
            }
        }
    }

    suspend fun getRecipeById(recipeId: Int): Recipe? {
        return withContext(dispatcher) {
            try {
                val response = service.getRecipeWithId(recipeId)
                response
            } catch (e: Exception) {
                Log.i("!!!", "${e.message}")
                null
            }
        }
    }

    suspend fun getRecipesByIds(ids: String): List<Recipe>? {
        return withContext(dispatcher) {
            try {
                val response = service.getRecipesByIds(ids)
                response
            } catch (e: Exception) {
                Log.i("!!!", "${e.message}")
                null
            }
        }
    }

    companion object {
        val INSTANCE: RecipeRepository by lazy { RecipeRepository() }
    }
}

