package ru.aliohin.recipesapp

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
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
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.divider.MaterialDividerItemDecoration
import ru.aliohin.recipesapp.RecipesListFragment.Companion.ARG_RECIPE
import ru.aliohin.recipesapp.databinding.FragmentRecipeBinding

class RecipeFragment : Fragment() {
    private var favourites = getFavorites()
    private var isFavourite: Boolean = false
    private var _binding: FragmentRecipeBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("Binding for FragmentRecipeBinding must not be null ")

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
        setDividerItemDecoration(binding.rvIngredients)
        setDividerItemDecoration(binding.rvMethod)
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
        binding.tvLabelRecipe.text = recipe?.title
        loadImageFromAssets(recipe?.imageUrl)
        isFavourite = favourites.contains(recipe?.id.toString())
        updateFavouriteButton(recipe?.title)
        binding.imageButtonFavourites.setOnClickListener {
            isFavourite = !isFavourite
            updateFavouriteButton(recipe?.title)
            if (isFavourite) {
                favourites.add(recipe?.id.toString())
                saveFavourites(favourites)
            } else {
                favourites.remove(recipe?.id.toString())
                saveFavourites(favourites)
            }
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

    private fun setDividerItemDecoration(recycler: RecyclerView) {
        val dividerItemDecoration =
            MaterialDividerItemDecoration(recycler.context, DividerItemDecoration.VERTICAL)
        dividerItemDecoration.isLastItemDecorated = false
        val color = ContextCompat.getColor(requireContext(), R.color.white_divider)
        dividerItemDecoration.dividerColor = color
        recycler.addItemDecoration(dividerItemDecoration)
    }

    private fun saveFavourites(favourites: MutableSet<String>) {
        val sharedPrefs = context?.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        sharedPrefs?.edit()?.putStringSet("MyFavourites", favourites)?.apply()
    }

    private fun getFavorites(): MutableSet<String> {
        val sharedPref = context?.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val newSet = sharedPref?.getStringSet("MyFavourites", setOf()) ?: setOf()
        return HashSet(newSet)
    }
}