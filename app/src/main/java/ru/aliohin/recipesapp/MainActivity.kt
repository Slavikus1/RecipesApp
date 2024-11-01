package ru.aliohin.recipesapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.aliohin.recipesapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val binding = ActivityMainBinding.inflate(layoutInflater)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}