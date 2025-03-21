package ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import androidx.navigation.findNavController
import kotlinx.serialization.json.Json
import model.Category
import model.Recipe
import ru.aliohin.recipesapp.R
import ru.aliohin.recipesapp.databinding.ActivityMainBinding
import java.net.HttpURLConnection
import java.net.URL

import java.util.concurrent.Executors

private const val API_CATEGORIES = "https://recipes.androidsprint.ru/api/category"

class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("Binding for ActivityMainBinding must not be null ")
    private val executor = Executors.newFixedThreadPool(10)
    private var categoriesId: MutableList<Int> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadCategories()
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

    private fun loadCategories() {
        val thread = Thread {
            try {
                val connection = URL(API_CATEGORIES).openConnection() as HttpURLConnection
                connection.connect()
                val response = connection.inputStream.bufferedReader().readText()

                Log.i("!!!", "responseCode${connection.responseCode}")
                Log.i("!!!", "responseMessage${connection.responseMessage}")
                Log.i("!!!", "Выполняю запрос в потоке ${Thread.currentThread().name}")
                Log.i("!!!", "Body: $response")
                val categories: List<Category> = Json.decodeFromString(response)
                for (category in categories) {
                    categoriesId.add(category.id)
                    getRecipesWithCategoriesId(category.id)
                }
                Log.i("!!!", "categories: $categories")
            } catch (e: Exception) {
                Log.e("!!!", "Ошибка при выполнении запроса ${e.message}")
            }
        }
        thread.start()
    }

    private fun getRecipesWithCategoriesId(categoryId: Int) {
        executor.execute {
            try {
                val connection =
                    URL("$API_CATEGORIES/$categoryId/recipes").openConnection() as HttpURLConnection
                val response = connection.inputStream.bufferedReader().readText()
                val recipes: List<Recipe> = Json.decodeFromString(response)
                Log.i("!!!", "recipes: $recipes")
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