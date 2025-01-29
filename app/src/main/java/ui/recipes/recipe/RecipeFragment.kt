package ui.recipes.recipe

import android.content.Context
import android.content.SharedPreferences
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.divider.MaterialDividerItemDecoration
import data.PreferencesUtils
import ru.aliohin.recipesapp.R
import model.Recipe
import ui.recipes.recipesList.RecipesListFragment.Companion.ARG_RECIPE
import ru.aliohin.recipesapp.databinding.FragmentRecipeBinding

class RecipeFragment : Fragment() {
    private val recipeViewModel: RecipeViewModel by viewModels()

    companion object {
        const val SHARED_PREFERENCES = "MyPrefs"
        const val KEY_FAVOURITES_RECIPE = "MyFavourites"
    }

    private var isFavourite: Boolean = false
    private var _binding: FragmentRecipeBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("Binding for FragmentRecipeBinding must not be null ")

    private val sharedPref: SharedPreferences by lazy {
        requireActivity().getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecipeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recipe: Recipe? = getRecipeFromArguments()
        initRecycler(recipe)
        initUI(recipe)
        setDividerItemDecoration()
        recipeViewModel.recipeState.observe(viewLifecycleOwner, Observer{
            Log.i("!!!", "IsFavourite: ${recipeViewModel.recipeState.value?.isFavourite}")
        })
    }

    private fun initRecycler(recipe: Recipe?) {
        if (recipe != null) {
            binding.rvIngredients.adapter = IngredientsAdapter(recipe.ingredients)
            binding.rvMethod.adapter = MethodAdapter(recipe.method)
            binding.SeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    binding.tvNumberOfPortions.text = "$progress"
                    (binding.rvIngredients.adapter as? IngredientsAdapter)?.updateIngredients(
                        progress
                    )
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {
                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                }
            })
            binding.SeekBar.progress = 1
        }
    }

    private fun getRecipeFromArguments(): Recipe? {
        val recipe: Recipe? = arguments.let { bundle ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                bundle?.getParcelable(ARG_RECIPE, Recipe::class.java)
            } else {
                @Suppress("DEPRECATION")
                bundle?.getParcelable(ARG_RECIPE)
            }
        }
        return recipe
    }

    private fun initUI(recipe: Recipe?) {
        val favourites = PreferencesUtils.getFavorites(sharedPref)
        binding.tvLabelRecipe.text = recipe?.title
        loadImageFromAssets(recipe?.imageUrl)
        isFavourite = favourites.contains(recipe?.id.toString())
        updateFavouriteButton(recipe?.title)
        binding.imageButtonFavourites.setOnClickListener {
            isFavourite = !isFavourite
            updateFavouriteButton(recipe?.title)
            if (isFavourite) {
                favourites.add(recipe?.id.toString())
            } else {
                favourites.remove(recipe?.id.toString())
            }
            PreferencesUtils.saveFavourites(sharedPref, favourites)
        }
    }

    private fun updateFavouriteButton(title: String?) {
        if (isFavourite) {
            binding.imageButtonFavourites.setImageResource(R.drawable.ic_heart)
            binding.imageButtonFavourites.contentDescription =
                getString(R.string.add_to_favourites, title)
        } else {
            binding.imageButtonFavourites.setImageResource(R.drawable.ic_heart_empty)
            binding.imageButtonFavourites.contentDescription =
                getString(R.string.remove_from_favourites, title)
        }

    }

    private fun loadImageFromAssets(imageFileName: String?) {
        if (imageFileName != null) {
            val drawable = try {
                requireContext().assets.open(imageFileName).use { stream ->
                    Drawable.createFromStream(stream, null)
                }
            } catch (e: Exception) {
                Log.e("RecipeFragment", "Error loading image: $imageFileName", e)
                null
            }
            binding.ivRecipeImageHeader.setImageDrawable(drawable)
        } else {
            Log.e("RecipeFragment", "Image file name is null")
            binding.ivRecipeImageHeader.setImageDrawable(null)
        }
    }

    private fun setDividerItemDecoration() {
        val dividerItemDecoration =
            MaterialDividerItemDecoration(
                requireContext(),
                LinearLayoutManager.VERTICAL
            ).apply {
                setDividerColor(ContextCompat.getColor(requireContext(), R.color.white_divider))
                isLastItemDecorated = false
                setDividerInsetStartResource(requireContext(), R.dimen.space_12)
                setDividerInsetEndResource(requireContext(), R.dimen.space_12)
            }
        binding.rvMethod.addItemDecoration(dividerItemDecoration)
        binding.rvIngredients.addItemDecoration(dividerItemDecoration)
    }
}