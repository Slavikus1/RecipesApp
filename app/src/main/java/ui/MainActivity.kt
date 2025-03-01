package ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import androidx.navigation.findNavController
import ru.aliohin.recipesapp.R
import ru.aliohin.recipesapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("Binding for ActivityMainBinding must not be null ")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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