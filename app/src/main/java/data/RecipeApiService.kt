package data
import model.Category
import model.Recipe
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RecipeApiService {
    @GET("category")
    suspend fun getCategories(): List<Category>

    @GET("category/{id}/recipes")
    suspend fun getRecipesWithCategoryId(@Path("id") id: Int): List<Recipe>

    @GET("category/{id}")
    suspend fun getCategoryWithId(@Path("id") id: Int): List<Category>

    @GET("recipe/{id}")
    suspend fun getRecipeWithId(@Path("id")id: Int): Recipe

    @GET("recipes")
    suspend fun getRecipesByIds(@Query("ids") ids: String): List<Recipe>
    
}