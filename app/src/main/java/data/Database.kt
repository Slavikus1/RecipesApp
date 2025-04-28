package data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import model.Category
import model.Recipe

@TypeConverters(RecipeTypeConverter::class)
@Database(
    entities = [Category::class, Recipe::class],
    version = 2,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun categoriesDao(): CategoriesDao
    abstract fun recipesDao(): RecipeDao
}