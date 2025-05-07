package data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import model.Recipe

@Dao
interface RecipeDao {

    @Query("Select * from recipes WHERE isFavourite = 1")
    suspend fun getFavouritesRecipes(): List<Recipe>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateFavouriteStatus(recipe: Recipe)

    @Query("SELECT * FROM recipes WHERE category_id =:categoryId")
    suspend fun getRecipesFromCacheByCategoryId(categoryId: Int): List<Recipe>

    @Query("SELECT * FROM recipes WHERE id =:recipeId")
    suspend fun getRecipeFromCacheById(recipeId: Int): Recipe?

    @Query("SELECT * FROM recipes")
    suspend fun getAllRecipesFromCache(): List<Recipe>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipe(recipe: Recipe)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipesList(recipesList: List<Recipe>)
}