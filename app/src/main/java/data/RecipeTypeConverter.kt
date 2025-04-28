package data

import androidx.room.TypeConverter
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import model.Ingredient

class RecipeTypeConverter {
    @TypeConverter
    fun fromIngredientsList(ingredientsList: List<Ingredient>): String{
        return Json.encodeToString(ingredientsList)
    }

    @TypeConverter
    fun toIngredients(ingredientsJson: String): List<Ingredient>{
        return Json.decodeFromString(ingredientsJson)
    }

    @TypeConverter
    fun fromMethod(method: List<String>): String {
        return Json.encodeToString(method)
    }

    @TypeConverter
    fun toMethod(methodString: String): List<String> {
        return Json.decodeFromString(methodString)
    }
}