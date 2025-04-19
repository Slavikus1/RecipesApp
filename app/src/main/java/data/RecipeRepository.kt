package data

import android.util.Log
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.coroutines.withContext
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.json.Json
import model.Category
import model.Recipe
import okhttp3.Dispatcher
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

const val CONTENT_TYPE = "application/Json"
const val BASE_URL = "https://recipes.androidsprint.ru/api/"
const val BASE_IMAGE_URL = "https://recipes.androidsprint.ru/api/images/"

class RecipeRepository {
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

    suspend fun getCategories(callback: (List<Category>?) -> Unit) {
        withContext(Dispatchers.IO) {
            try {
                val response = service.getCategories().execute()
                if (response.isSuccessful) callback(response.body())
                else callback(null)
            } catch (e: Exception) {
                Log.i("!!!", "${e.message}")
                callback(null)
            }
        }
    }

    suspend fun getRecipesByCategoryId(categoryId: Int, callback: (List<Recipe>?) -> Unit) {
        withContext(Dispatchers.IO) {
            try {
                val response = service.getRecipesWithCategoryId(categoryId).execute()
                if (response.isSuccessful) callback(response.body())
                else callback(null)
            } catch (e: Exception) {
                Log.i("!!!", "${e.message}")
                callback(null)
            }
        }
    }

    suspend fun getRecipeById(recipeId: Int, callback: (Recipe?) -> Unit) {
        withContext(Dispatchers.IO) {
            try {
                val response = service.getRecipeWithId(recipeId).execute()
                if (response.isSuccessful) callback(response.body())
                else callback(null)
            } catch (e: Exception) {
                Log.i("!!!", "${e.message}")
                callback(null)
            }
        }
    }

    suspend fun getRecipesByIds(ids: String, callback: (List<Recipe>?) -> Unit) {
        withContext(Dispatchers.IO) {
            try {
                Log.i("!!!!", ids)
                val response = service.getRecipesByIds(ids).execute()
                if (response.isSuccessful) callback(response.body())
                else callback(null)
            } catch (e: Exception) {
                Log.i("!!!", "${e.message}")
                callback(null)
            }
        }
    }

    companion object {
        val INSTANCE: RecipeRepository by lazy { RecipeRepository() }
    }
}

