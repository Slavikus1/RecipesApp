package data
import model.Category
import model.Recipe
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RecipeApiService {
    @GET("category")
    fun getCategories(): Call<List<Category>>

    @GET("category/{id}/recipes")
    fun getRecipesWithCategoryId(@Path("id") id: Int): Call<List<Recipe>>

    @GET("category/{id}")
    fun getCategoryWithId(@Path("id") id: Int): Call<List<Category>>

    @GET("recipe/{id}")
    fun getRecipeWithId(@Path("id")id: Int): Call<Recipe>

    @GET("recipes")
    fun getRecipesByIds(@Query("ids") ids: String): Call<List<Recipe>>
    
}