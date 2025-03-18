package ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import androidx.navigation.findNavController
import kotlinx.serialization.json.Json
import model.Category
import ru.aliohin.recipesapp.R
import ru.aliohin.recipesapp.databinding.ActivityMainBinding
import java.net.HttpURLConnection
import java.net.URL

private const val API_CATEGORIES = "https://recipes.androidsprint.ru/api/category"

class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("Binding for ActivityMainBinding must not be null ")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
                Log.i("!!!", "categories: $categories")
            }
            catch (e: Exception){
                Log.e("!!!", "Ошибка при выполнении запроса ${e.message}")
            }
        }
        thread.start()

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
}