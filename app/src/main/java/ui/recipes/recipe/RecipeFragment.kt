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
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.divider.MaterialDividerItemDecoration
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
        val recipe: Recipe? = arguments?.let { recipeViewModel.loadRecipe(it.getInt(ARG_RECIPE)) }
        initRecycler(recipe)
        initUI()
        setDividerItemDecoration()
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

    private fun initUI() {
        recipeViewModel.recipeState.observe(viewLifecycleOwner) {
            val state: RecipeViewModel.RecipeState = it
            val recipe = state.recipe
            binding.ivRecipeImageHeader.setImageDrawable(it.recipeImage)
            binding.tvLabelRecipe.text = recipe?.title
            binding.imageButtonFavourites.setImageResource(
                if (state.isFavourite) R.drawable.ic_heart else R.drawable.ic_heart_empty)
            binding.imageButtonFavourites.contentDescription = getString(
                if (state.isFavourite) R.string.add_to_favourites else R.string.remove_from_favourites,
                state.recipe?.title
            )
            binding.imageButtonFavourites.setOnClickListener {
                recipeViewModel.onFavoritesClicked(recipe?.id.toString())
            }
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