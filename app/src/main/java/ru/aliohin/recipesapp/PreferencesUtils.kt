package ru.aliohin.recipesapp

import android.content.SharedPreferences
import ru.aliohin.recipesapp.RecipeFragment.Companion.KEY_FAVOURITES_RECIPE

object PreferencesUtils {
    fun saveFavourites(sharedPref: SharedPreferences, favourites: MutableSet<String>) {
        sharedPref.edit()?.putStringSet(KEY_FAVOURITES_RECIPE, favourites)?.apply()
    }

    fun getFavorites(sharedPref: SharedPreferences): MutableSet<String> {
        val newSet = sharedPref.getStringSet(KEY_FAVOURITES_RECIPE, setOf()) ?: setOf()
        return HashSet(newSet)
    }
}