package ui.recipes.recipesList

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import data.RecipeRepository
import di.RecipeApplication
import kotlinx.coroutines.launch
import ru.aliohin.recipesapp.R
import ru.aliohin.recipesapp.databinding.FragmentRecipesListBinding

@AndroidEntryPoint
class RecipesListFragment: Fragment() {
    private val recipeListViewModel: RecipesListViewModel by viewModels()
    private val recipeListArgs: RecipesListFragmentArgs by navArgs()

    private lateinit var repository: RecipeRepository
    private var categoryId: Int? = null
    private var categoryName: String? = null
    private var categoryImageUrl: String? = null
    private var _binding: FragmentRecipesListBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("Binding for FragmentRecipesListBinding must not be null ")


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecipesListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        repository = recipeListViewModel.myRepository
        categoryId = recipeListArgs.Category.id
        categoryName = recipeListArgs.Category.title
        categoryImageUrl = recipeListArgs.Category.imageUrl

        recipeListViewModel.loadRecipesListState(categoryId, categoryName, categoryImageUrl)
        initRecycler()
    }

    private fun initRecycler() {
        val recipesListAdapter =
            RecipesListAdapter(emptyList(), requireActivity().application as RecipeApplication)
        binding.rvRecipes.adapter = recipesListAdapter
        recipesListAdapter.setOnItemClickListener(object :
            RecipesListAdapter.OnItemClickListener {
            override fun onItemClick(recipeId: Int) {
                openRecipeByRecipeId(recipeId)
            }
        })
        recipeListViewModel.recipeState.observe(viewLifecycleOwner) {
            it.listOfRecipes?.let { it1 -> recipesListAdapter.updateData(it1) }
            binding.tvRecipes.text = recipeListViewModel.recipeState.value?.categoryName
            Glide.with(this)
                .load(it.categoryImageUrl)
                .placeholder(R.drawable.img_placeholder)
                .error(R.drawable.img_error)
                .into(binding.imageHeaderRecipe)
            if (it.isShowError) Toast.makeText(
                requireContext(),
                getString(R.string.toast_error_load_data),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun openRecipeByRecipeId(recipeId: Int) {
        viewLifecycleOwner.lifecycleScope.launch {
            val cachedRecipe = repository.getRecipeFromCacheById(recipeId)
            if (cachedRecipe == null) {
                val loadedRecipe = repository.getRecipeById(recipeId)
                findNavController().navigate(
                    RecipesListFragmentDirections.actionRecipesListFragmentToRecipeFragment(
                        loadedRecipe!!
                    )
                )
                repository.insertRecipe(loadedRecipe)
            } else {
                findNavController().navigate(
                    RecipesListFragmentDirections.actionRecipesListFragmentToRecipeFragment(
                        cachedRecipe
                    )
                )
            }

        }
    }
}