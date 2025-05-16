package ui.recipes.favourites

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import di.RecipeApplication
import ru.aliohin.recipesapp.R
import ru.aliohin.recipesapp.databinding.FragmentFavoritesBinding
import ui.recipes.recipesList.RecipesListAdapter

class FavoritesFragment : Fragment() {
    private lateinit var favouritesViewModel: FavouritesViewModel
    private var _binding: FragmentFavoritesBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("Binding for FragmentFavoritesBinding must not be null")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val appContainer = (requireActivity().application as RecipeApplication).appContainer
        favouritesViewModel = appContainer.favouritesListViewModelFactory.create()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        favouritesViewModel.loadFavouritesState()
        initRecycler()
    }

    private fun initRecycler() {
        val adapter =
            RecipesListAdapter(emptyList(), requireActivity().application as RecipeApplication)
        binding.rvFavorites.adapter = adapter
        adapter.setOnItemClickListener(object : RecipesListAdapter.OnItemClickListener {
            override fun onItemClick(recipeId: Int) {
                openRecipeByRecipeId(recipeId)
            }
        })
        favouritesViewModel.favouritesState.observe(viewLifecycleOwner) {
            it.favouritesList?.let { favouritesList ->
                adapter.updateData(favouritesList)
            }
            if (it.favouritesList?.isEmpty() == true) {
                binding.tvFavorites.visibility = View.VISIBLE
                binding.rvFavorites.visibility = View.GONE
            } else {
                binding.tvFavorites.visibility = View.GONE
                binding.rvFavorites.visibility = View.VISIBLE
            }
            if (it.isShowError) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.toast_error_loading_data), Toast.LENGTH_SHORT
                )
                    .show()
            }
        }
    }

    private fun openRecipeByRecipeId(recipeId: Int) {
        val recipe =
            favouritesViewModel.favouritesState.value?.favouritesList?.find { it.id == recipeId }
        if (recipe != null) {
            val action =
                FavoritesFragmentDirections.actionFavoritesFragmentToRecipeFragment(recipe)
            findNavController().navigate(action)
        } else Log.e("RecipesListFragment", "Recipe not found for ID: $recipeId")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}