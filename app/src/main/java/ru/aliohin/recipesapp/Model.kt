package ru.aliohin.recipesapp

data class Category(
    val id: Int,
    val title: String,
    val description: String,
    val imageUrl: String,
)

data class Ingredient(
    val quantity: Double,
    val unitOfMeasure: String,
    val description: String,
)

data class Recipe (
    val id: Int,
    val title: String,
    val ingredients: List<Ingredient>,
    val method: List<String>,
    val imageUrl: String,
)