package ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import androidx.navigation.findNavController
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import data.RecipeApiService
import kotlinx.serialization.json.Json
import model.Category
import model.Recipe
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import ru.aliohin.recipesapp.R
import ru.aliohin.recipesapp.databinding.ActivityMainBinding
import java.util.concurrent.Executors


private const val API_CATEGORIES = "https://recipes.androidsprint.ru/api/category"
private val httpInterceptor = HttpLoggingInterceptor().apply {
    level = HttpLoggingInterceptor.Level.BODY
}

class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("Binding for ActivityMainBinding must not be null ")
    private val executor = Executors.newFixedThreadPool(10)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        loadCategories()
        Log.i("!!!", "Метод onCreate выполняется в потоке: ${Thread.currentThread().name}")

        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.categoryButton.setOnClickListener {
            supportFragmentManager.commit {
                val navController = findNavController(R.id.nav_host_fragment)
                if (navController.currentDestination?.id != R.id.categoriesListFragment) navController.navigate(
                    R.id.action_global_categoriesListFragment
                )
            }
        }

        binding.favouritesButton.setOnClickListener {
            supportFragmentManager.commit {
                val navController = findNavController(R.id.nav_host_fragment)
                if (navController.currentDestination?.id != R.id.favoritesFragment) navController.navigate(
                    R.id.action_global_favouritesListFragment
                )
            }
        }
    }

//    private fun loadCategories() {
//        executor.execute {
//            try {
//                val contentType = "application/Json".toMediaType()
//                val retrofit = Retrofit.Builder()
//                    .baseUrl("https://recipes.androidsprint.ru/api/")
//                    .addConverterFactory(Json.asConverterFactory(contentType))
//                    .build()
//                val service: RecipeApiService = retrofit.create(RecipeApiService::class.java)
//                val categoryCall: Call<List<Category>> = service.getCategories()
//                val categoryResponse = categoryCall.execute()
//                val categories: List<Category>? = categoryResponse.body()
//
//                Log.i("!!!","categories: $categories")
//            } catch (e: Exception) {
//                Log.e("!!!", "Ошибка при выполнении запроса категорий ${e.message}")
//            }
//        }
//    }

    private fun getRecipesWithCategoriesId(categoryId: Int) {
        executor.execute {
            try {
                val client = OkHttpClient.Builder()
                    .addInterceptor(httpInterceptor)
                    .build()

                val request = Request.Builder()
                    .url("$API_CATEGORIES/$categoryId/recipes")
                    .build()
                val response = client.newCall(request).execute()
                if (response.isSuccessful) {
                    val responseBody = response.body?.string()
                    if (responseBody != null) {
                        Log.i("!!!", responseBody)
                    }
                    val recipes: List<Recipe>? = responseBody?.let { Json.decodeFromString(it) }
                    Log.i("!!!", "recipes: $recipes")
                } else Log.i("!!!", "error - ${response.code} - ${response.message}")
                response.close()
            } catch (e: Exception) {
                Log.e("!!!", "Ошибка при выполнии запроса списка рецептов ${e.message}")
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        executor.shutdown()
        _binding = null
    }
}