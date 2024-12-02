package ru.aliohin.recipesapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.add
import androidx.fragment.app.commit
import ru.aliohin.recipesapp.databinding.FragmentCategoriesListBinding

class CategoriesListFragment : Fragment(R.layout.fragment_categories_list) {
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
        initRecycler()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initRecycler() {
        val categoriesAdapter = CategoryListAdapter(STUB.getCategories())
        binding.rvCategory.adapter = categoriesAdapter
        categoriesAdapter.setOnItemClickListener(object : CategoryListAdapter.OnItemClickListener {
            override fun onItemClick() {
                openRecipesByCategoryId()
            }
        })
    }

    private fun openRecipesByCategoryId(){
        parentFragmentManager.commit {
            setReorderingAllowed(true)
            add<RecipesListFragment>(R.id.mainContainer)
            addToBackStack(null)
        }
    }
}