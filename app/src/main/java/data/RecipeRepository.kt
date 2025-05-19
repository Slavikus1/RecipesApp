package data

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import model.Category
import model.Recipe
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

const val CONTENT_TYPE = "application/Json"
const val BASE_URL = "https://recipes.androidsprint.ru/api/"
const val BASE_IMAGE_URL = "https://recipes.androidsprint.ru/api/images/"

class RecipeRepository @Inject constructor(
    private val categoriesDao: CategoriesDao,
    private val recipeDao: RecipeDao,
    private val service: RecipeApiService,
) {

    private val dispatcher: CoroutineContext = Dispatchers.IO

    suspend fun getFavouritesRecipes(): List<Recipe> {
        return recipeDao.getFavouritesRecipes()
    }

    suspend fun updateFavouritesStatus(recipe: Recipe) {
        recipeDao.updateFavouriteStatus(recipe)
    }

    suspend fun getRecipesFromCacheByCategoryId(categoryId: Int): List<Recipe> {
        return recipeDao.getRecipesFromCacheByCategoryId(categoryId)
    }

    suspend fun getRecipeFromCacheById(recipeId: Int): Recipe? {
        return recipeDao.getRecipeFromCacheById(recipeId)
    }

    suspend fun getAllRecipesFromCache(): List<Recipe> {
        return recipeDao.getAllRecipesFromCache()
    }

    suspend fun insertRecipe(recipe: Recipe) {
        recipeDao.insertRecipe(recipe)
    }

    suspend fun insertRecipesList(recipesList: List<Recipe>) {
        recipeDao.insertRecipesList(recipesList)
    }

    suspend fun insertCategoriesInDataBase(categories: List<Category>) {
        categoriesDao.insertCategories(categories)
    }

    suspend fun getCategoriesFromCache(): List<Category> {
        return categoriesDao.getAll()
    }

    suspend fun getCategories(): List<Category>? {
        return withContext(dispatcher) {
            try {
                service.getCategories()
            } catch (e: Exception) {
                Log.i("Error fetching categories", e.message ?: "Unknown error")
                null
            }
        }
    }

    suspend fun getRecipesByCategoryId(categoryId: Int): List<Recipe>? {
        return withContext(dispatcher) {
            try {
                service.getRecipesWithCategoryId(categoryId)
            } catch (e: Exception) {
                Log.i("!!!", "${e.message}")
                null
            }
        }
    }

    suspend fun getRecipeById(recipeId: Int): Recipe? {
        return withContext(dispatcher) {
            try {
                service.getRecipeWithId(recipeId)
            } catch (e: Exception) {
                Log.i("!!!", "${e.message}")
                null
            }
        }
    }

    suspend fun getRecipesByIds(ids: String): List<Recipe>? {
        return withContext(dispatcher) {
            try {
                service.getRecipesByIds(ids)
            } catch (e: Exception) {
                Log.i("!!!", "${e.message}")
                null
            }
        }
    }
}

