package ui.categories

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import ru.aliohin.recipesapp.R
import ru.aliohin.recipesapp.databinding.FragmentCategoriesListBinding
import androidx.navigation.fragment.findNavController
import di.RecipeApplication

class CategoriesListFragment : Fragment(R.layout.fragment_categories_list) {

    private lateinit var categoryListViewModel: CategoriesListViewModel

    private var _binding: FragmentCategoriesListBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("Binding for FragmentCategoriesListBinding must not be null ")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val appContainer = (requireActivity().application as RecipeApplication).appContainer
        categoryListViewModel = appContainer.categoriesListViewModelFactory.create()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCategoriesListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        categoryListViewModel.loadCategories()
        initRecycler()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initRecycler() {
        val categoriesAdapter =
            CategoryListAdapter(emptyList(), requireActivity().application as RecipeApplication)
        binding.rvCategory.adapter = categoriesAdapter
        categoryListViewModel.categoriesState.observe(viewLifecycleOwner) {
            it.list?.let { it1 ->
                categoriesAdapter.updateDataSet(it1)
                if (it.isShowError) Toast.makeText(
                    requireContext(),
                    getString(R.string.toast_error_load_data),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        categoriesAdapter.setOnItemClickListener(object : CategoryListAdapter.OnItemClickListener {
            override fun onItemClick(categoryId: Int) {
                openRecipesByCategoryId(categoryId)
            }
        })

    }

    private fun openRecipesByCategoryId(categoryId: Int) {
        val category =
            categoryListViewModel.categoriesState.value?.list?.find { it.id == categoryId }
        if (category != null) {
            findNavController().navigate(
                CategoriesListFragmentDirections.actionCategoriesListFragmentToRecipesListFragment(
                    category
                )
            )
        } else throw IllegalArgumentException("Category must not be null")
    }
}