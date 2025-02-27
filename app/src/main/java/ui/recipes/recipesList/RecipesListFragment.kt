package ui.recipes.recipesList

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.fragment.app.viewModels
import ru.aliohin.recipesapp.R
import data.STUB
import ru.aliohin.recipesapp.databinding.FragmentRecipesListBinding
import ui.categories.CategoriesListFragment
import ui.recipes.recipe.RecipeFragment


class RecipesListFragment : Fragment() {

    private val recipeListViewModel: RecipesListViewModel by viewModels()

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
        requireArguments().let { args ->
            categoryId = args.getInt(CategoriesListFragment.ARG_CATEGORY_ID)
            categoryName = args.getString(CategoriesListFragment.ARG_CATEGORY_NAME)
            categoryImageUrl = args.getString(CategoriesListFragment.ARG_CATEGORY_IMAGE_URL)
        }
        recipeListViewModel.loadRecipesListState(categoryId, categoryName, categoryImageUrl)
        initRecycler()
    }

    private fun initRecycler() {
        val recipesListAdapter = RecipesListAdapter(emptyList())
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
            binding.imageHeaderRecipe.setImageDrawable(recipeListViewModel.recipeState.value?.categoryImage)
        }
    }

    private fun openRecipeByRecipeId(recipeId: Int) {
        val recipe = STUB.getRecipeById(recipeId)
        if (recipe != null) {
            val bundle = bundleOf(ARG_RECIPE to recipe.id)
            parentFragmentManager.commit {
                setReorderingAllowed(true)
                replace<RecipeFragment>(R.id.mainContainer, args = bundle)
                addToBackStack(null)
            }
        } else {
            throw IllegalStateException("Recipes id is not found")
        }
    }

    companion object {
        const val ARG_RECIPE = "arg_recipe"
    }
}