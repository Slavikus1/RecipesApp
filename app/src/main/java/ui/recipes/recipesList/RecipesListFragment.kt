package ui.recipes.recipesList

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import ru.aliohin.recipesapp.R
import data.STUB
import ru.aliohin.recipesapp.databinding.FragmentRecipesListBinding
import ui.categories.CategoriesListFragment
import ui.recipes.recipe.RecipeFragment


class RecipesListFragment : Fragment() {

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
        binding.tvRecipes.text = categoryName
        val drawable = try {
            Drawable.createFromStream(
                view.context.assets.open(categoryImageUrl.toString()),
                null
            )
        } catch (e: Exception) {
            Log.d("!!!", "Image not found $categoryImageUrl")
            null
        }
        binding.imageHeaderRecipe.setImageDrawable(drawable)
        initRecycler()

    }

    private fun initRecycler() {
        categoryId = requireArguments().getInt(CategoriesListFragment.ARG_CATEGORY_ID)
        val recipesListAdapter = RecipesListAdapter(STUB.getRecipesByCategoryId(categoryId ?: 0))
        binding.rvRecipes.adapter = recipesListAdapter

        recipesListAdapter.setOnItemClickListener(object :
            RecipesListAdapter.OnItemClickListener {
            override fun onItemClick(recipeId: Int) {
                openRecipeByRecipeId(recipeId)
            }
        })
    }

    private fun openRecipeByRecipeId(recipeId: Int) {
        val recipe = STUB.getRecipeById(recipeId)
        if (recipe != null) {
             val bundle = bundleOf(ARG_RECIPE to recipe)
            parentFragmentManager.commit {
                setReorderingAllowed(true)
                replace<RecipeFragment>(R.id.mainContainer, args = bundle)
                addToBackStack(null)
            }
        } else {
            throw IllegalStateException ("Recipes id is not found")
        }
    }

    companion object {
        const val ARG_RECIPE = "arg_recipe"
    }


}