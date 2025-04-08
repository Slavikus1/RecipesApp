package ui.recipes.recipesList

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import ru.aliohin.recipesapp.databinding.FragmentRecipesListBinding

class RecipesListFragment : Fragment() {

    private val recipeListViewModel: RecipesListViewModel by viewModels()
    private val recipeListArgs: RecipesListFragmentArgs by navArgs()

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
        categoryId = recipeListArgs.Category.id
        categoryName = recipeListArgs.Category.title
        categoryImageUrl = recipeListArgs.Category.imageUrl

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
            if (it.isShowError) Toast.makeText(
                requireContext(),
                "Ошибка загрузки рецептов",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun openRecipeByRecipeId(recipeId: Int) {
        findNavController().navigate(
            RecipesListFragmentDirections.actionRecipesListFragmentToRecipeFragment(
                recipeId
            )
        )
    }
}