package ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import ru.aliohin.recipesapp.R
import ru.aliohin.recipesapp.databinding.ActivityMainBinding
import ui.categories.CategoriesListFragment
import ui.recipes.favourites.FavoritesFragment

class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("Binding for ActivityMainBinding must not be null ")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                replace<CategoriesListFragment>(R.id.mainContainer)
            }
        }
        binding.categoryButton.setOnClickListener {
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                replace<CategoriesListFragment>(R.id.mainContainer)
                addToBackStack(null)
            }
        }

        binding.favouritesButton.setOnClickListener {
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                replace<FavoritesFragment>(R.id.mainContainer)
                addToBackStack(null)
            }
        }
    }
}