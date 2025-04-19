package ui.recipes.recipe

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.divider.MaterialDividerItemDecoration
import ru.aliohin.recipesapp.R
import ru.aliohin.recipesapp.databinding.FragmentRecipeBinding

class PortionSeekBarListener(val onChangeIngredients: (Int) -> Unit) : OnSeekBarChangeListener {
    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        onChangeIngredients(progress)
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {

    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {

    }

}

class RecipeFragment : Fragment() {
    private val recipeViewModel: RecipeViewModel by viewModels()
    private val recipeArgs: RecipeFragmentArgs by navArgs()

    companion object {
        const val SHARED_PREFERENCES = "MyPrefs"
        const val KEY_FAVOURITES_RECIPE = "MyFavourites"
    }

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
        val recipeId = recipeArgs.recipeId
        recipeViewModel.loadRecipe(recipeId)
        initUI()
    }

    private fun initUI() {
        recipeViewModel.recipeState.observe(viewLifecycleOwner) {
            val state: RecipeViewModel.RecipeState = it
            val recipe = state.recipe
            Glide.with(this)
                .load(state.recipeImageUrl)
                .placeholder(R.drawable.img_placeholder)
                .error(R.drawable.img_error)
                .into(binding.ivRecipeImageHeader)
            binding.tvLabelRecipe.text = recipe?.title
            binding.imageButtonFavourites.setImageResource(
                if (state.isFavourite) R.drawable.ic_heart else R.drawable.ic_heart_empty
            )
            binding.imageButtonFavourites.contentDescription = getString(
                if (state.isFavourite) R.string.add_to_favourites else R.string.remove_from_favourites,
                state.recipe?.title
            )
            binding.imageButtonFavourites.setOnClickListener {
                recipeViewModel.onFavoritesClicked(recipe?.id.toString())
            }
            binding.rvIngredients.adapter = recipeViewModel.recipeState.value?.recipe?.let {
                IngredientsAdapter(emptyList())
            }
            binding.rvMethod.adapter =
                recipeViewModel.recipeState.value?.recipe?.let { MethodAdapter(emptyList()) }

            if (recipe != null) {
                (binding.rvMethod.adapter as? MethodAdapter)?.updateDataset(state.recipe.method)
                (binding.rvIngredients.adapter as? IngredientsAdapter)?.updateDataset(state.recipe.ingredients)
                (binding.rvIngredients.adapter as? IngredientsAdapter)?.updateIngredients(
                    state.numberOfPortions
                )
                binding.tvNumberOfPortions.text = state.numberOfPortions.toString()
                if (state.isShowError) Toast.makeText(
                    requireContext(),
                    getString(R.string.toast_error_load_data),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        initSeekBar()
        setDividerItemDecoration()
    }

    private fun initSeekBar() {
        binding.SeekBar.progress = 1
        binding.SeekBar.setOnSeekBarChangeListener(PortionSeekBarListener { progress ->
            recipeViewModel.updatePortionsCount(progress)
        })

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