package data

import android.util.Log
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import model.Category
import model.Recipe
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.Executors

const val CONTENT_TYPE = "application/Json"
const val BASE_URL = "https://recipes.androidsprint.ru/api/"

class RecipeRepository {
    val loadImageUrl = "https://recipes.androidsprint.ru/api/images/"
    private val executor = Executors.newFixedThreadPool(10)
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

    fun getCategories(callback: (List<Category>?) -> Unit) {
        executor.execute {
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

    fun getRecipesByCategoryId(categoryId: Int, callback: (List<Recipe>?) -> Unit) {
        executor.execute {
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

    fun getRecipeById(recipeId: Int, callback: (Recipe?) -> Unit) {
        executor.execute {
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

    fun getRecipesByIds(ids: String, callback: (List<Recipe>?) -> Unit) {
        executor.execute {
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
        val INSTANSE: RecipeRepository by lazy { RecipeRepository() }
    }
}

