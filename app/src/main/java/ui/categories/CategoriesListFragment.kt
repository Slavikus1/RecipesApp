package ui.categories

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import ru.aliohin.recipesapp.R
import data.STUB
import ru.aliohin.recipesapp.databinding.FragmentCategoriesListBinding
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController

class CategoriesListFragment : Fragment(R.layout.fragment_categories_list) {

    private val categoryListViewModel: CategoriesListViewModel by viewModels()

    companion object {
        const val ARG_CATEGORY_ID = "arg_category_id"
        const val ARG_CATEGORY_NAME = "arg_category_name"
        const val ARG_CATEGORY_IMAGE_URL = "arg_category_image_url"
    }

    private var _binding: FragmentCategoriesListBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("Binding for FragmentCategoriesListBinding must not be null ")

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
        val categoriesAdapter = CategoryListAdapter(emptyList())
        binding.rvCategory.adapter = categoriesAdapter
        categoriesAdapter.setOnItemClickListener(object : CategoryListAdapter.OnItemClickListener {
            override fun onItemClick(categoryId: Int) {
                openRecipesByCategoryId(categoryId)
            }
        })
        categoryListViewModel.categoriesState.observe(viewLifecycleOwner) {
            it.list?.let { it1 -> categoriesAdapter.updateDataSet(it1) }
        }

    }

    private fun openRecipesByCategoryId(categoryId: Int) {
        val categoryName = STUB.getCategories().find { it.id == categoryId }?.title
        val categoryImageUrl = STUB.getCategories().find { it.id == categoryId }?.imageUrl
        val bundle = bundleOf(
            ARG_CATEGORY_ID to categoryId,
            ARG_CATEGORY_NAME to categoryName,
            ARG_CATEGORY_IMAGE_URL to categoryImageUrl,
        )
        findNavController().navigate(R.id.action_categoriesListFragment_to_recipesListFragment, bundle)
    }
}